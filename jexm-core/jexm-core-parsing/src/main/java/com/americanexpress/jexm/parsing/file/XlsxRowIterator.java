/*
 * Copyright (c) 2018 American Express Travel Related Services Company, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.americanexpress.jexm.parsing.file;

import com.americanexpress.jexm.parsing.RawRowIterator;
import com.americanexpress.jexm.parsing.config.SheetConfig;
import com.americanexpress.jexm.parsing.exceptions.FileFormatException;
import com.americanexpress.jexm.parsing.exceptions.IllegalSheetException;
import com.americanexpress.jexm.parsing.exceptions.SheetNotFoundException;
import com.americanexpress.jexm.parsing.utils.ExcelParserUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.*;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Iterator class in charge of parsing XLSX files in a lazily evaluated manner. */
public class XlsxRowIterator extends RawRowIterator {

  private String currentCellReference;
  private SharedStringsTable sharedStringsTable;
  private StylesTable stylesTable;
  private XMLStreamReader sheetXmlReader;
  private InputStream inputStream;

  private static final Logger log = LoggerFactory.getLogger(XlsxRowIterator.class);

  private static final String CELL_TAG = "c";
  private static final String ROW_TAG = "row";
  private static final String SHEET_DATA_TAG = "sheetData";
  private static final String CELL_TYPE_SHARED_STRING = "s";

  public XlsxRowIterator(InputStream inputStream, SheetConfig sheetConfig) {
    Objects.requireNonNull(inputStream);

    try {
      OPCPackage opcPackage = OPCPackage.open(inputStream);
      XSSFReader reader = new XSSFReader(opcPackage);

      this.sharedStringsTable = reader.getSharedStringsTable();
      this.stylesTable = reader.getStylesTable();
      this.sheetXmlReader = findSheet(reader, sheetConfig);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    } catch (OpenXML4JException e) {
      throw new FileFormatException(e);
    }

    this.inputStream = inputStream;
    this.headerIndexes = RawRowIterator.createHeaderIndexes(readNextRow());
  }

  /**
   * Finds a sheet based on a configuration which specifies which sheet to select
   *
   * @param reader An {@link XSSFReader} with the contents of all sheets
   * @param sheetConfig Sheet configuration
   * @return A reader for the selected sheet
   * @throws SheetNotFoundException If a sheet was not found for the given configuration
   */
  private XMLStreamReader findSheet(XSSFReader reader, SheetConfig sheetConfig) {
    try {
      Iterator<InputStream> sheets = reader.getSheetsData();

      if (sheetConfig == null) {
        // if no sheet configuration was specified, simply use the first sheet
        return ExcelParserUtils.findFirstSheet(sheets);
      } else {
        if (sheetConfig.isIndexSet()) {
          return ExcelParserUtils.findSheetAtIndex(sheets, sheetConfig.getIndex());
        } else if (sheetConfig.isNameSet()) {
          return ExcelParserUtils.findSheetWithName(sheets, sheetConfig.getName());
        } else {
          throw new IllegalSheetException("Neither sheet name nor index were specified.");
        }
      }
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    } catch (OpenXML4JException | XMLStreamException e) {
      throw new FileFormatException(e);
    }
  }

  @Override
  public synchronized Map<Integer, String> readNextRow() {
    Map<Integer, String> cells = null;

    try {
      /* Iterate through the underlying XML of a specific XLSX sheet and produce
      the index to String value map for the next row */
      while (sheetXmlReader.hasNext()) {
        int event = sheetXmlReader.next();
        String elementName;

        switch (event) {
          case XMLStreamReader.START_ELEMENT:
            // if we have reached the start of an XML tag

            elementName = sheetXmlReader.getLocalName();

            if (elementName.equals(CELL_TAG)) { // if the starting tag is a cell
              // read the value inside that cell (which also updates 'currentCellReference'
              String cellValue = readCell();

              if (cellValue != null) {

                if (cells == null) {
                  // if the current row has not yet been initialised, do so
                  cells = new HashMap<>();
                }

                // find the index of the current cell from it's reference (eg. 'A' -> 0)
                int cellIndex = ExcelParserUtils.headerIndex(currentCellReference);

                // populate the row with the current cell index and its value
                if (cells.put(cellIndex, cellValue) != null) {
                  throw new IllegalStateException(
                      "Cell index " + cellIndex + " was found multiple times.");
                }
              }
            }
            break;
          case XMLStreamReader.END_ELEMENT:
            // if we have reached the end of an XML tag

            elementName = sheetXmlReader.getLocalName();

            if (elementName.equals(ROW_TAG) || elementName.equals(SHEET_DATA_TAG)) {
              // if we are closing the row or the entire sheet
              currentCellReference = null; // restart cell references for the next row

              // return the current row
              return cells == null ? null : Collections.unmodifiableMap(cells);
            }
          default:
            break;
        }
      }

    } catch (XMLStreamException e) {
      throw new FileFormatException(e);
    }

    // If went past </sheetData>, there are no more rows from here onwards, so return null.
    return null;
  }

  /**
   * Reads the contents of the current cell taking into account the XLSX cell type, style and String
   * index lookup.
   *
   * @return String value of the current cell, possibly formatted
   */
  private synchronized String readCell() {
    /* Cell attributes:
        r = reference     (eg. 'A1')
        t = type          (eg. 's' is String)
        s = style index   (eg. '1' points to style at index 1)

    more details at
        https://msdn.microsoft.com/en-us/library/office/documentformat.openxml.spreadsheet.cell.aspx
        https://msdn.microsoft.com/en-us/library/dd922181.aspx
    */

    /*
    The possible cell types are:
        b - boolean
        d - date in ISO8601 format
        e - error
        inlineStr - string that doesn't use the shared string table
        n - number
        s - shared string
        str - formula string
     */

    currentCellReference = sheetXmlReader.getAttributeValue(null, "r");
    String cellType = sheetXmlReader.getAttributeValue(null, "t");
    String cellStyle = sheetXmlReader.getAttributeValue(null, "s");

    // Read the contents of the next <v> tag, containing the value of the cell
    String v = readTextV();

    if (v == null || v.isEmpty()) {
      return null;
    }

    if (cellType != null && cellType.equals(CELL_TYPE_SHARED_STRING)) {
      // if attribute t = "s", we know the cell contains an index to the Shared Strings table for
      // lookup
      int idx = Integer.parseInt(v);
      v = sharedStringsTable.getEntryAt(idx).getT();
    } else if (cellStyle != null && !cellStyle.isEmpty()) {
      // if the cell does have a style associated to it, apply the style
      // this is used to determine for example if a cell is of a date type
      v = inStandardFormat(Integer.parseInt(cellStyle), v);
    }

    return v;
  }

  /** @return Exact contents of the next {@code <v>} tag as a String. */
  private synchronized String readTextV() {
    StringBuilder sb = new StringBuilder();

    try {
      // Iterate inside the <c> (cell) tag, reading the contents of the <v> (value) tag inside of it
      while (sheetXmlReader.hasNext()) {
        int event = sheetXmlReader.next();

        switch (event) {
          case XMLStreamReader.CHARACTERS:
          case XMLStreamReader.CDATA:
            // If we have just read character data, append it
            sb.append(sheetXmlReader.getText());
            break;
          case XMLStreamReader.END_ELEMENT:
            /* If we closed the tag without ever reading character data, return null,
            else return all character data read in this cell */
            return sb.length() == 0 ? null : sb.toString();
          default:
            break;
        }
      }

      throw new XMLStreamException("File ended unexpectedly");
    } catch (XMLStreamException e) {
      throw new FileFormatException(e);
    }
  }

  private String inStandardFormat(int cellXfIdx, String rawString) {

    CTXf cellXf = stylesTable.getCellXfAt(cellXfIdx);

    int numFmtId = (int) cellXf.getNumFmtId();

    if (DateUtil.isInternalDateFormat(numFmtId)) {
      // if the cell is a date format, transform it into an ISO date or time string
      // for easier adaptation to LocalDate, LocalTime, etc.

      double daysSince1Jan1900 = Double.parseDouble(rawString);
      return ExcelParserUtils.formatAsISODateString(numFmtId, daysSince1Jan1900);
    }

    return rawString;
  }

  @Override
  public synchronized void close() {
    try {
      if (sheetXmlReader != null) {
        sheetXmlReader.close();
      }
    } catch (XMLStreamException e) {
      log.error("Unable to close XML reader of XLSX resource", e);
    }

    try {
      if (inputStream != null) {
        inputStream.close();
      }
    } catch (IOException e) {
      log.error("Unable to close XLSX resource", e);
    }
  }
}
