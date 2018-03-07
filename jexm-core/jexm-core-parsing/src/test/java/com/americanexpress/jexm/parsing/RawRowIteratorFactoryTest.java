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

import static org.junit.Assert.assertTrue;

import com.americanexpress.jexm.ExcelExtension;
import com.americanexpress.jexm.parsing.exceptions.UnsupportedFileTypeException;
import com.americanexpress.jexm.parsing.file.CsvRowIterator;
import com.americanexpress.jexm.parsing.file.XlsxRowIterator;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Test;

public class RawRowIteratorFactoryTest {

  private static Path path(String filename) {
    return Paths.get("src", "test", "resources", filename);
  }

  private static final Path CSV_FILE = path("people_first_names.csv");
  private static final Path XLSX_FILE = path("people_first_names.xlsx");
  private static final Path UNSUPPORTED_FILE = path("unsupported_file.unsupported");
  private static final Path NON_EXISTENT_FILE = path("not_found");
  private static final Path CSV_FILE_WITHOUT_EXTENSION = path("csv_without_extension");
  private static final Path XLSX_FILE_WITHOUT_EXTENSION = path("xlsx_without_extension");

  @Test
  public void testGettingInstanceFromCsvPathShouldBeCsvRowIteratorType() {
    assertTrue(RawRowIteratorFactory.createIterator(CSV_FILE) instanceof CsvRowIterator);
  }

  @Test
  public void testGettingInstanceFromXlsxPathShouldBeXlsxRowIteratorType() {
    assertTrue(RawRowIteratorFactory.createIterator(XLSX_FILE) instanceof XlsxRowIterator);
  }

  @Test(expected = UnsupportedFileTypeException.class)
  public void testGettingInstanceFromUnsupportedPathShouldThrowException() {
    RawRowIteratorFactory.createIterator(UNSUPPORTED_FILE);
  }

  @Test(expected = UncheckedIOException.class)
  public void testGettingInstanceOfPathNotFoundShouldThrowException() {
    RawRowIteratorFactory.createIterator(NON_EXISTENT_FILE);
  }

  @Test(expected = NullPointerException.class)
  public void testGettingInstanceFromNullPathSpecifyingExtensionShouldThrowException()
      throws Exception {
    RawRowIteratorFactory.createIterator((Path) null, ExcelExtension.CSV);
  }

  @Test(expected = NullPointerException.class)
  public void testGettingInstanceFromNullPathShouldThrowException() throws Exception {
    RawRowIteratorFactory.createIterator(null);
  }

  @Test
  public void
      testGettingInstanceFromUnsupportedPathExplicitlyChoosingCsvShouldBeCsvRowIteratorType() {
    assertTrue(
        RawRowIteratorFactory.createIterator(CSV_FILE_WITHOUT_EXTENSION, ExcelExtension.CSV)
            instanceof CsvRowIterator);
  }

  @Test
  public void
      testGettingInstanceFromUnsupportedPathExplicitlyChoosingXlsxShouldBeXlsxRowIteratorType() {
    assertTrue(
        RawRowIteratorFactory.createIterator(XLSX_FILE_WITHOUT_EXTENSION, ExcelExtension.XLSX)
            instanceof XlsxRowIterator);
  }

  @Test(expected = UnsupportedFileTypeException.class)
  public void
      testGettingInstanceFromUnsupportedPathWithoutExplicitlyChoosingFileTypeShouldThrowException() {
    RawRowIteratorFactory.createIterator(CSV_FILE_WITHOUT_EXTENSION);
  }

  @Test
  public void testGettingInstanceFromInputStreamExplicitlyChoosingCsvShouldBeCsvRowIteratorType()
      throws Exception {
    InputStream in = Files.newInputStream(CSV_FILE);

    assertTrue(
        RawRowIteratorFactory.createIterator(in, ExcelExtension.CSV) instanceof CsvRowIterator);
  }

  @Test
  public void testGettingInstanceFromInputStreamExplicitlyChoosingXlsxShouldBeXlsxRowIteratorType()
      throws Exception {
    InputStream in = Files.newInputStream(XLSX_FILE);

    assertTrue(
        RawRowIteratorFactory.createIterator(in, ExcelExtension.XLSX) instanceof XlsxRowIterator);
  }

  @Test(expected = NullPointerException.class)
  public void
      testGettingInstanceFromInputStreamWithoutExplicitlyChoosingFileTypeShouldThrowException()
          throws Exception {
    InputStream in = Files.newInputStream(CSV_FILE);

    RawRowIteratorFactory.createIterator(in, null);
  }

  @Test(expected = NullPointerException.class)
  public void testGettingInstanceFromNullInputStreamShouldThrowException() throws Exception {
    RawRowIteratorFactory.createIterator((InputStream) null, ExcelExtension.CSV);
  }
}
