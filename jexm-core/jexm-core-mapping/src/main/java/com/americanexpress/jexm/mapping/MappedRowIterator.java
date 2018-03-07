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

package com.americanexpress.jexm.mapping;

import com.americanexpress.jexm.CloseableIterator;
import com.americanexpress.jexm.ExcelExtension;
import com.americanexpress.jexm.adapter.Adapters;
import com.americanexpress.jexm.adapter.CellAdapter;
import com.americanexpress.jexm.adapter.exceptions.CellAdapterException;
import com.americanexpress.jexm.adapter.exceptions.UnsupportedAdapterFieldException;
import com.americanexpress.jexm.annotation.Adapter;
import com.americanexpress.jexm.annotation.Header;
import com.americanexpress.jexm.annotation.Sheet;
import com.americanexpress.jexm.mapping.exceptions.IllegalHeaderException;
import com.americanexpress.jexm.parsing.RawRowIterator;
import com.americanexpress.jexm.parsing.RawRowIteratorFactory;
import com.americanexpress.jexm.parsing.config.SheetConfig;
import com.americanexpress.jexm.parsing.utils.ExcelParserUtils;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class providing an iterator for each row of an Excel document given the Java bean for mapping.
 * Entries of the iterator will be of the given bean class (which contains fields annotated by
 * {@link Header} with its fields already mapped according to adapter rules. These are lazily
 * evaluated and are not pulled from disk until {@link MappedRowIterator#hasNext} or {@link
 * MappedRowIterator#next} are called. This is a high-level class which does not concern with
 * specific file types to be parsed. The {@link RawRowIterator} is responsible for handling
 * different file types and abstracting them to this class.
 *
 * @param <T> Bean type produced by this iterator
 */
public class MappedRowIterator<T extends Serializable> implements CloseableIterator<T> {

  private static final Logger log = LoggerFactory.getLogger(MappedRowIterator.class);

  // Bean class representing each row
  private Class<T> rowClass;
  private SheetConfig sheetConfig;

  // Iterator with lazily evaluated contents of the file
  private RawRowIterator rawRowIterator;

  private MappedRowIterator(Class<T> rowClass) {
    this.rowClass = Objects.requireNonNull(rowClass);
    this.sheetConfig = SheetConfig.fromAnnotation(rowClass.getAnnotation(Sheet.class));
  }

  public MappedRowIterator(Class<T> rowClass, RawRowIterator rawRowIterator) {
    this(rowClass);
    this.rawRowIterator = Objects.requireNonNull(rawRowIterator);
    verifyHeaderNamesExist();
  }

  public MappedRowIterator(Class<T> rowClass, Path path) {
    this(rowClass);
    this.rawRowIterator = RawRowIteratorFactory.createIterator(path, sheetConfig);
    verifyHeaderNamesExist();
  }

  public MappedRowIterator(Class<T> rowClass, Path path, ExcelExtension excelExtension) {
    this(rowClass);
    this.rawRowIterator = RawRowIteratorFactory.createIterator(path, excelExtension, sheetConfig);
    verifyHeaderNamesExist();
  }

  public MappedRowIterator(
      Class<T> rowClass, InputStream inputStream, ExcelExtension excelExtension) {
    this(rowClass);
    this.rawRowIterator =
        RawRowIteratorFactory.createIterator(inputStream, excelExtension, sheetConfig);
    verifyHeaderNamesExist();
  }

  /** Close disk resources opened by this iterator. */
  @Override
  public void close() {
    rawRowIterator.close();
  }

  /**
   * Evaluate whether a next row is available for read in the Excel document
   *
   * @return {@code true} if the document contains a next, non-empty row, {@code false} otherwise.
   */
  @Override
  public boolean hasNext() {
    return rawRowIterator.hasNext();
  }

  /**
   * Loads the next row from the Excel document and transforms it into the given bean class
   * according to built-in or specified adapter rules.
   *
   * @return Instance of bean class representing the row.
   */
  @Override
  public T next() {
    return createRowObject(rawRowIterator.next());
  }

  /**
   * Creates an instance of the bean class representing a row. It uses a map with the column index
   * to it's String value, provided by the {@link RawRowIterator}, the header line and the {@link
   * Header} annotation to figure out how to populate each field of the bean.
   *
   * @param rawRow Header index to String value map for the current row
   * @return Instance of bean class with all its header fields adapted
   */
  private T createRowObject(Map<Integer, String> rawRow) {

    // create a new instance of the given bean class representing each row
    T t = ReflectionUtils.newInstance(rowClass);

    /* Retrieve the map of header name to its String value for the current row
    Example: {"Name" -> "Chuck", "Lastname" -> "Smith"} */
    Map<String, String> headerToCellValueMap = createHeaderNameToCellValueMap(rawRow);

    // Iterate through every field in the bean class that is annotated by @Header
    for (Field f : rowClass.getDeclaredFields()) {
      Header h = f.getAnnotation(Header.class);

      if (h != null) { // If there is a header annotation, perform the mapping

        // Figure out the raw String value for that field
        String rawCellValue = rawCellValue(h, f, rawRow, headerToCellValueMap);

        // Convert the String type to the type defined by the field
        Object adaptedCellValue = adaptedCellValue(f, rawCellValue);

        // Update the field in the bean object with the new, adapted value
        ReflectionUtils.update(t, f, adaptedCellValue);
      }
      // else if header annotation is not found, do nothing with this field
    }

    return t;
  }

  /**
   * Adapts/converts a String value from the low-level parser ({@link RawRowIterator}) into an
   * object of class defined by the given Field type.
   *
   * @param f The field used used for the adapting operator
   * @param rawCellValue The String value to be adapted to the given field type
   * @return An instance of {@code field.getType()} as the adapted value of the String
   * @throws CellAdapterException if the built-in or provided adapter fails to adapt the String
   *     value and the {@link Adapter#suppressAdapterException()} is set to {@code false}.
   */
  private Object adaptedCellValue(Field f, String rawCellValue) {

    Class<? extends CellAdapter> cellAdapter = null;

    Adapter a = f.getAnnotation(Adapter.class);
    if (a != null && a.value() != Adapter.DEFAULT.class) {
      // If a custom adapter was specified by the user, use it instead
      cellAdapter = a.value();
    }

    try {
      return Adapters.adapt(f.getType(), f.getGenericType(), rawCellValue, cellAdapter);
    } catch (UnsupportedAdapterFieldException e) {
      // Users cannot suppress UnsupportedAdapterFieldException
      throw e;
    } catch (Exception e) {
      /* An exception will be caught here if the built-in or provided adapter fails to adapt/convert
      the String field. For example if we pass "Hello World" to the built-in int adapter, it will
      throw a NumberFormatException to be caught here.
      Possible future additions will include allowing users to pass in their own exception handlers.*/

      if (a == null || a.suppressAdapterException()) {
        // If the user decided to suppress Adapter exceptions, log them and move on
        log.warn(
            "Unable to map raw value \"{}\" to instance of {}. "
                + "Resulted in exception {} with message \"{}\".",
            rawCellValue,
            f.getType(),
            e.getClass(),
            e.getMessage());
      } else {
        /* if user chooses to throw the exception upon an adapter failure,
        wrap it around a CellAdapterException and rethrow */
        throw new CellAdapterException(e);
      }

      /* else if user chooses to suppress a exceptions, no exception will be thrown
      when cell mapping fails. For example, if Integer.parseInt throws a NumberFormatException,
      that will be ignored and number will be defaulted to its original value (zero).
      Possible future additions will include allowing users to pass in their own exception handlers. */

      return null;
    }
  }

  /**
   * Retrieves the String value of the current row given a {@link Header}. The {@link Header#name()}
   * and {@link Header#index()} are used to figure out how to perform the lookup of the column on
   * the Excel document. If neither the name nor index are set, the Java field name itself is used
   * as header name.
   *
   * @param h Header used to perform lookup of the String value
   * @param f Field used for lookup if both {@link Header#name()} and {@link Header#index()} are not
   *     set.
   * @param rawRow Column/header index to String value lookup using {@link Header#index()}
   * @param headerToCellValueMap Column/header name to String value lookup using {@link
   *     Header#name()}
   * @return String value retrieved from the lookup
   * @throws IllegalHeaderException if both the {@link Header#name()} and {@link Header#index()} are
   *     set
   */
  private String rawCellValue(
      Header h, Field f, Map<Integer, String> rawRow, Map<String, String> headerToCellValueMap) {

    if (h.name().isEmpty()) {
      // if no name was specified in @Header annotation
      if (h.index() >= 0) { // see if a column index was specified
        if (!h.ref().isEmpty()) { // if both header index and ref are specified, there is ambiguity
          throw new IllegalHeaderException(
              rowClass,
              f,
              String.format(
                  "Only one of header name (\"%s\"), index (%d) or ref (\"%s\") can be "
                      + "specified in %s annotation of field \"%s\".",
                  h.name(), h.index(), h.ref(), Header.class.getName(), f.getName()));
        }
        // otherwise use index
        return rawRow.get(h.index());
      } else if (!h.ref().isEmpty()) {
        // otherwise use reference
        return rawRow.get(ExcelParserUtils.headerIndex(h.ref()));
      } else { // if neither header name, ref nor index are specified, use field name as header name
        return headerToCellValueMap.get(f.getName());
      }
    } else {
      if (h.index() >= 0
          || !h.ref()
              .isEmpty()) { // if both header name and index are specified, there is ambiguity
        throw new IllegalHeaderException(
            rowClass,
            f,
            String.format(
                "Only one of header name (\"%s\") or index (%d) or ref (\"%s\") can be "
                    + "specified in %s annotation of field \"%s\".",
                h.name(), h.index(), h.ref(), Header.class.getName(), f.getName()));
      }

      // if only the header name is set, use it
      return headerToCellValueMap.get(h.name());
    }
  }

  /**
   * @param rawRow Map of column/header indexes to their String values in the current row.
   * @return Map of header names to their values in the current row.
   */
  private Map<String, String> createHeaderNameToCellValueMap(Map<Integer, String> rawRow) {
    return rawRowIterator
        .headerIndexes()
        .entrySet()
        .stream()
        .filter(e -> rawRow.get(e.getValue()) != null)
        .collect(Collectors.toMap(Map.Entry::getKey, e -> rawRow.get(e.getValue())));
  }

  /**
   * Logs a warning for each header name specified in the Java bean which does not exist in the
   * excel file itself.
   */
  private void verifyHeaderNamesExist() {
    Arrays.stream(rowClass.getDeclaredFields())
        .map(
            f -> {
              Header h = f.getAnnotation(Header.class);

              if (h != null) {
                if (!h.name().isEmpty()) {
                  // if the header name was specified
                  return h.name();
                } else if (h.index() < 0 && h.ref().isEmpty()) {
                  // if no other lookup will be specified, use the field name
                  return f.getName();
                }
              }

              return null;
            })
        .filter(Objects::nonNull)
        .filter(n -> !rawRowIterator.headerIndexes().containsKey(n))
        .forEach(n -> log.warn("Header named \"{}\" was not found in the file.", n));
  }
}
