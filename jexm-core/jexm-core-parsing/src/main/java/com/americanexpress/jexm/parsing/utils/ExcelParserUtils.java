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

package com.americanexpress.jexm.parsing.utils;

import com.americanexpress.jexm.parsing.exceptions.SheetNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.eventusermodel.XSSFReader;

/** Contains utilities to parse Excel files. */
public final class ExcelParserUtils {

  private static final Pattern CELL_REFERENCE_REGEX = Pattern.compile("^([A-Z]+)(\\d*)$");

  private static final DateFormat ISO_LOCAL_DATE = new SimpleDateFormat("YYYY-MM-dd");
  private static final DateFormat ISO_LOCAL_TIME = new SimpleDateFormat("HH:mm:ss");
  private static final DateFormat ISO_LOCAL_DATE_TIME =
      new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss");
  private static final DateFormat ISO_MONTH_DAY = new SimpleDateFormat("--MM-dd");
  private static final DateFormat ISO_YEAR_MONTH = new SimpleDateFormat("YYYY-MM");

  private static final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

  private ExcelParserUtils() {
    // not meant to be initialised
  }

  /**
   * Returns the index of a header/column based on its Excel reference (eg. 'AA'. Ignores the row
   * index if one is present (eg. 'AA1') Example: 'A' -> 0, 'B' -> 1, ..., 'Z' -> 25, 'AA' -> 26,
   * ...
   *
   * @param headerRef Header/column reference (eg. 'AA') starting with upper case letters,
   *     optionally followed by digits
   * @return Index of the header, starting from zero (ie. 'A' -> 0)
   */
  public static int headerIndex(String headerRef) {
    Objects.requireNonNull(headerRef);

    Matcher m = CELL_REFERENCE_REGEX.matcher(headerRef);

    if (m.matches()) { // Find if the header reference is in the right format
      // Get only the letters at the start
      String alph = m.group(1);

      if (alph == null) {
        throw new IllegalArgumentException(headerRef);
      } else {
        return CellReference.convertColStringToIndex(alph);
      }
    } else {
      throw new IllegalArgumentException(headerRef);
    }
  }

  /**
   * Takes the number of days since 1st Jan 1900 (decimal places indicate number of
   * hours/seconds/...) and transforms it into into an ISO date or time format according to the
   * provided XLSX number format id.
   *
   * <p>XLSX Built-in number formats:
   *
   * <p>**ID** **Format Code** 0 General 1 0 2 0.00 3 #,##0 4 #,##0.00 9 0% 10 0.00% 11 0.00E+00 12
   * # ?/? 13 # ??/?? 14 d/m/yyyy - LocalDate 15 d-mmm-yy - LocalDate 16 d-mmm - MonthDay 17 mmm-yy
   * - YearMonth 18 h:mm tt - LocalTime 19 h:mm:ss tt - LocalTime 20 H:mm - LocalTime 21 H:mm:ss -
   * LocalTime 22 m/d/yyyy H:mm - LocalDateTime 27 [$-404]e/m/d 30 m/d/yy 36 = [$-404]e/m/d 37 #,##0
   * ;(#,##0) 38 #,##0 ;[Red](#,##0) 39 #,##0.00;(#,##0.00) 40 #,##0.00;[Red](#,##0.00) 45 mm:ss -
   * LocalTime 46 [h]:mm:ss - LocalTime 47 mmss.0 48 ##0.0E+0 49 @ 50 [$-404]e/m/d 57 [$-404]e/m/d
   * 59 = t0 60 = t0.00 61 = t#,##0 62 = t#,##0.00 67 = t0% 68 = t0.00% 69 = t# ?/? 70 = t# ??/??
   *
   * <p>Number format codes (numFmtId) greater than 164 are custom.
   *
   * @param numFmtId XLSX number format of the cell
   * @param daysSince1Jan1900 Number of days since 1st Jan 1900
   * @return ISO formatted date or time
   */
  public static String formatAsISODateString(int numFmtId, double daysSince1Jan1900) {

    Date date = DateUtil.getJavaDate(daysSince1Jan1900);

    switch (numFmtId) {
      case 14:
      case 15:
        return ISO_LOCAL_DATE.format(date);
      case 16:
        return ISO_MONTH_DAY.format(date);
      case 17:
        return ISO_YEAR_MONTH.format(date);
      case 18:
      case 19:
      case 20:
      case 21:
      case 45:
      case 46:
      case 47:
        return ISO_LOCAL_TIME.format(date);
      case 22:
        return ISO_LOCAL_DATE_TIME.format(date);
      default:
        throw new IllegalArgumentException(
            "Number format id " + numFmtId + " is not an excel date format");
    }
  }

  /**
   * Returns an {@link XMLStreamReader} with the contents of the first excel sheet
   *
   * @param sheets Iterator of input streams with the Excel sheet data
   * @return An {@link XMLStreamReader} from the first item in the iterator
   * @throws XMLStreamException
   * @throws SheetNotFoundException If no sheets were found
   */
  public static XMLStreamReader findFirstSheet(Iterator<InputStream> sheets)
      throws XMLStreamException {
    Objects.requireNonNull(sheets);

    if (sheets.hasNext()) {
      return xmlInputFactory.createXMLStreamReader(sheets.next());
    } else {
      throw new SheetNotFoundException(0);
    }
  }

  /**
   * Returns an {@link XMLStreamReader} with the contents of the excel sheet at a given index
   *
   * @param sheets Iterator of input streams with the Excel sheet data
   * @param index Required sheet index
   * @return An {@link XMLStreamReader} from the item in the iterator at given index
   * @throws XMLStreamException
   * @throws SheetNotFoundException If a sheet was not found with the given index
   */
  public static XMLStreamReader findSheetAtIndex(Iterator<InputStream> sheets, int index)
      throws XMLStreamException {
    Objects.requireNonNull(sheets);

    int count = 0;
    while (sheets.hasNext() && count != index) {
      sheets.next();
      count++;
    }
    if (sheets.hasNext()) {
      InputStream s = sheets.next();
      return xmlInputFactory.createXMLStreamReader(s);
    } else {
      throw new SheetNotFoundException(index);
    }
  }

  /**
   * Returns an {@link XMLStreamReader} with the contents of the excel sheet with a given name
   *
   * @param sheets Iterator of input streams with the Excel sheet data
   * @param name Required sheet name
   * @return An {@link XMLStreamReader} from the item in the iterator with a given name
   * @throws XMLStreamException
   * @throws SheetNotFoundException If a sheet was not found with the given name
   */
  public static XMLStreamReader findSheetWithName(Iterator<InputStream> sheets, String name)
      throws XMLStreamException {
    Objects.requireNonNull(sheets);
    Objects.requireNonNull(name);

    XSSFReader.SheetIterator sheetIterator = (XSSFReader.SheetIterator) sheets;
    while (sheetIterator.hasNext()) {
      InputStream s = sheetIterator.next();
      if (name.equals(sheetIterator.getSheetName())) {
        return xmlInputFactory.createXMLStreamReader(s);
      }
    }

    throw new SheetNotFoundException(name);
  }
}
