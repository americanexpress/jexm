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

package com.americanexpress.jexm.parsing;

import com.americanexpress.jexm.ExcelExtension;
import com.americanexpress.jexm.parsing.config.SheetConfig;
import com.americanexpress.jexm.parsing.exceptions.UnsupportedFileTypeException;
import com.americanexpress.jexm.parsing.file.CsvRowIterator;
import com.americanexpress.jexm.parsing.file.XlsxRowIterator;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RawRowIteratorFactory {

  private static final Logger log = LoggerFactory.getLogger(RawRowIteratorFactory.class);

  private RawRowIteratorFactory() {
    // not meant to be initialised
  }

  /**
   * Creates an instance of {@link RawRowIterator} for the given path based on the file extension
   * itself.
   *
   * @param path File path
   * @return Iterator for the given file
   */
  public static RawRowIterator createIterator(Path path) {
    return createIterator(path, null, null);
  }

  /**
   * Creates an instance of {@link RawRowIterator} for the given path based on the file extension
   * itself.
   *
   * @param path File path
   * @param sheetConfig The optional Excel sheet configuration
   * @return Iterator for the given file
   */
  public static RawRowIterator createIterator(Path path, SheetConfig sheetConfig) {
    return createIterator(path, null, sheetConfig);
  }

  /**
   * Creates an instance of {@link RawRowIterator} for the given path based on the enforced excel
   * extension, thus ignoring the file extension itself.
   *
   * @param path File path
   * @param excelExtension Excel type of the file
   * @return Iterator for the given file
   */
  public static RawRowIterator createIterator(Path path, ExcelExtension excelExtension) {
    return createIterator(path, excelExtension, null);
  }

  /**
   * Creates an instance of {@link RawRowIterator} for the given input stream based on the enforced
   * excel extension.
   *
   * @param path Input stream with contents of the Excel document
   * @param excelExtension Excel type of the contents
   * @param sheetConfig The optional Excel sheet configuration
   * @return Iterator for the given input stream
   */
  public static RawRowIterator createIterator(
      Path path, ExcelExtension excelExtension, SheetConfig sheetConfig) {
    Objects.requireNonNull(path);

    InputStream inputStream;
    try {
      inputStream = Files.newInputStream(path);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }

    if (excelExtension == null) {
      // if a file extension was not explicitly specified,
      // use the file's extension itself from the file name
      if (ExcelExtension.CSV.matches(path)) {
        return createIterator(inputStream, ExcelExtension.CSV, sheetConfig);
      } else if (ExcelExtension.XLSX.matches(path) || ExcelExtension.XLSM.matches(path)) {
        return createIterator(inputStream, ExcelExtension.XLSX, sheetConfig);
      }
    } else {
      // if a file extension was specified, use it and ignore the file name
      return createIterator(inputStream, excelExtension, sheetConfig);
    }

    throw new UnsupportedFileTypeException(path.toString());
  }

  /**
   * Creates an instance of {@link RawRowIterator} for the given input stream based on the enforced
   * excel extension.
   *
   * @param inputStream Input stream with contents of the Excel document
   * @param excelExtension Excel type of the contents
   * @return Iterator for the given input stream
   */
  public static RawRowIterator createIterator(
      InputStream inputStream, ExcelExtension excelExtension) {
    return createIterator(inputStream, excelExtension, null);
  }

  /**
   * Creates an instance of {@link RawRowIterator} for the given input stream based on the enforced
   * excel extension.
   *
   * @param inputStream Input stream with contents of the Excel document
   * @param excelExtension Excel type of the contents
   * @param sheetConfig The optional Excel sheet configuration
   * @return Iterator for the given input stream
   */
  public static RawRowIterator createIterator(
      InputStream inputStream, ExcelExtension excelExtension, SheetConfig sheetConfig) {
    Objects.requireNonNull(inputStream);
    Objects.requireNonNull(excelExtension);

    switch (excelExtension) {
      case CSV:
        if (sheetConfig != null) {
          log.warn(
              "Sheet configuration was specified for {} file type, but has no effect for on it.",
              ExcelExtension.CSV);
        }
        return new CsvRowIterator(inputStream);
      case XLSM: // XLSM and XLSX files have the same table structure
      case XLSX:
        return new XlsxRowIterator(inputStream, sheetConfig);
      default:
        throw new IllegalStateException(
            "Cannot generate a raw row iterator for uncovered case " + excelExtension);
    }
  }
}
