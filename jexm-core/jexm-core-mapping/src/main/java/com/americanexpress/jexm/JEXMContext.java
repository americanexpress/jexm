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

package com.americanexpress.jexm;

import com.americanexpress.jexm.mapping.MappedRowIterator;
import com.americanexpress.jexm.parsing.RawRowIterator;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JEXM is an Open-Source library to facilitate reading from Excel files in an efficient and
 * intuitive way. The JEXM API is simple to call, making use the latest Java 8 Stream library to
 * allow for low memory footprint, parallel execution and powerful functional programming
 * constructs. It allows mapping rows of Excel documents into Java beans in a similar way as JPA
 * (for database entries) and JAXB (for XML entries), by using class-level and field-level
 * annotations. Rows are iteratively and individually provided by a {@link Stream} and
 * lazily-evaluated, meaning the row data is only read from disk as the read is consumed. No state
 * is kept from previous rows, making the library memory efficient. This acts in a similar way as
 * {@link Files#lines(Path)}. If disposal of IO resources is required, it is the caller's
 * responsibility to do so, which can be performed using the try-with-resources construct to close
 * the read upon completion.
 *
 * <p>Currently the following file types are supported: CSV - Comma Separated Values XLSX - Open XML
 * Spreadsheet for Microsoft Office 2007
 *
 * <p>Support for other table-type file extensions will be added in future releases.
 */
public final class JEXMContext<T extends Serializable> {

  private static final Logger log = LoggerFactory.getLogger(JEXMContext.class);
  private Class<T> clazz;

  private JEXMContext(Class<T> clazz) {
    this.clazz = Objects.requireNonNull(clazz);
  }

  /**
   * Produces a new instance of {@link JEXMContext} which allows parsing rows of Excel-based
   * documents into instances of type {@param clazz}.
   *
   * @param clazz POJO class to be used for marshalling. Expected to contain fields annotated by
   *     {@link com.americanexpress.jexm.annotation.Header} to allow mapping of Excel headers.
   * @param <T> Serializable type
   * @return A new instance of {@link JEXMContext} for the given class.
   */
  public static <T extends Serializable> JEXMContext<T> newInstance(Class<T> clazz) {
    return new JEXMContext<>(clazz);
  }

  /**
   * Provides a stream of objects representing rows in the given Excel document. It is the caller's
   * responsibility to close the stream upon its consumption, which can be done using the
   * try-with-resources construct.
   *
   * @param filepath Path of the file to be parsed lazily. For this method, the file format will be
   *     deduced based on the file extension, meaning "file.csv" will be parsed as a CSV type. If
   *     such behaviour is undesired, method signatures containing {@link ExcelExtension} can be
   *     used.
   * @throws java.io.UncheckedIOException If an IO error occurs while opening, closing or reading
   *     the file.
   * @return Stream of row objects
   */
  public Stream<T> read(String filepath) {
    return read(Paths.get(filepath));
  }

  /**
   * @see {@link JEXMContext#read(String)}
   * @param excelExtension The explicit file type. If this is {@code null}, the file type will be
   *     deduced
   */
  public Stream<T> read(String filepath, ExcelExtension excelExtension) {
    return read(Paths.get(filepath), excelExtension);
  }

  /** @see {@link JEXMContext#read(String)} */
  public Stream<T> read(File file) {
    Objects.requireNonNull(file);
    return read(file.toPath());
  }

  /**
   * @see {@link JEXMContext#read(String)}
   * @param excelExtension The explicit file type. If this is {@code null}, the file type will be
   *     deduced
   */
  public Stream<T> read(File file, ExcelExtension excelExtension) {
    Objects.requireNonNull(file);
    return read(file.toPath(), excelExtension);
  }

  /** @see {@link JEXMContext#read(String)} */
  public Stream<T> read(Path filepath) {
    Objects.requireNonNull(filepath);

    log.info("Streaming over file {} mapping to bean {}", filepath, clazz);

    return streamFromCloseableIterator(new MappedRowIterator<>(clazz, filepath));
  }

  /**
   * @see {@link JEXMContext#read(String)}
   * @param excelExtension The explicit file type. If this is {@code null}, the file type will be
   *     deduced from the file extension itself.
   */
  public Stream<T> read(Path filepath, ExcelExtension excelExtension) {
    Objects.requireNonNull(filepath);

    if (excelExtension == null) {
      // if extension is not explicitly specified, JEXMContext will figure it out from the file
      // extension
      log.info("Streaming over file {}, mapping to bean {}", filepath, clazz);
    } else {
      log.info(
          "Streaming over file {}, enforcing type {}, mapping to bean {}",
          filepath,
          excelExtension,
          clazz);
    }

    return streamFromCloseableIterator(new MappedRowIterator<>(clazz, filepath, excelExtension));
  }

  /**
   * @see {@link JEXMContext#read(String)}
   * @param excelExtension The explicit file type. This cannot be null for the {@link InputStream}
   *     parameter, because there is no simple way to tell the file format otherwise.
   */
  public Stream<T> read(InputStream inputStream, ExcelExtension excelExtension) {
    Objects.requireNonNull(inputStream);
    Objects.requireNonNull(excelExtension, "Excel file extension is mandatory for input read.");
    // The excel extension must be specified for the InputStream,
    // as there is no simple way to figure out the file type from here

    log.info("Streaming over input read of type {} mapping to bean {}", excelExtension, clazz);

    return streamFromCloseableIterator(new MappedRowIterator<>(clazz, inputStream, excelExtension));
  }

  /**
   * @see {@link JEXMContext#read(String)} This method allows the user to define his own low-level
   *     iterator for any given file, {@link InputStream} or any other input data. This gives users
   *     of the JEXMContext framework the ability of writting their own code to parse the underlying
   *     data, leaving JEXMContext to only perform the mapping to the Java bean. This is
   *     particularly useful if JEXMContext does not yet support the file type wanted.
   * @param rawRowIterator The low-level iterator to be used when producing the {@link Stream}
   */
  public Stream<T> read(RawRowIterator rawRowIterator) {
    Objects.requireNonNull(rawRowIterator);

    log.info("Streaming over user-defined RawRowIterator, mapping to bean {}", clazz);

    return streamFromCloseableIterator(new MappedRowIterator<>(clazz, rawRowIterator));
  }

  private Stream<T> streamFromCloseableIterator(CloseableIterator<T> closeableIterator) {
    int properties = Spliterator.ORDERED | Spliterator.NONNULL | Spliterator.IMMUTABLE;

    return StreamSupport.stream(
            Spliterators.spliteratorUnknownSize(closeableIterator, properties), false)
        .onClose(closeableIterator::close); // add onClose hook to close IO resources
  }
}
