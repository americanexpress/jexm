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
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Iterator class in charge of parsing CSV files in a lazily evaluated manner. */
public class CsvRowIterator extends RawRowIterator {

  private BufferedReader reader;

  private static final char SEPARATOR = ',';
  private static final char QUOTES = '\"';
  private static final Pattern EMPTY_LINE_PATTERN = Pattern.compile("^[,\\s]*$");

  private static final Logger log = LoggerFactory.getLogger(CsvRowIterator.class);

  public CsvRowIterator(InputStream inputStream) {
    Objects.requireNonNull(inputStream);

    this.reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    this.headerIndexes = RawRowIterator.createHeaderIndexes(readNextRow());
  }

  @Override
  public synchronized Map<Integer, String> readNextRow() {
    return lineAsIndexToValueMap(readNextNonEmptyLine());
  }

  private static Map<Integer, String> lineAsIndexToValueMap(String line) {
    if (line == null) {
      return null;
    }

    List<String> valueList = splitLine(line);

    return Collections.unmodifiableMap(
        IntStream.range(0, valueList.size())
            .boxed()
            .filter(i -> valueList.get(i) != null)
            .filter(i -> !valueList.get(i).isEmpty())
            .collect(Collectors.toMap(Function.identity(), valueList::get)));
  }

  /**
   * Splits a line in a similar manner as {@code string.split(",", -1)}, but taking into
   * consideration that, as commas can be genuinely part of a String, such String can be wrapped
   * around quotes. Example: {@code "Hello,\"World,!\""} will return the equivalent of {@code
   * Arrays.asList("Hello", "World,!"}
   *
   * @param line String to split on the comma character
   * @return List of Strings split by the comma separator
   */
  public static List<String> splitLine(String line) {
    Objects.requireNonNull(line);

    List<String> list = new ArrayList<>();

    StringBuilder sb = new StringBuilder();
    boolean insideQuotes = false;
    for (char c : line.toCharArray()) {
      switch (c) {
        case SEPARATOR:
          if (insideQuotes) {
            sb.append(c);
          } else {
            list.add(sb.toString());
            sb.setLength(0); // reset String builder
          }
          break;
        case QUOTES:
          insideQuotes = !insideQuotes;
          break;
        default:
          sb.append(c);
          break;
      }
    }

    if (insideQuotes) {
      // if we have reached the end of the line without closing the quotes,
      // add opening quotes where they were supposed to be
      sb.insert(0, QUOTES);
    }

    list.add(sb.toString());

    return list;
  }

  /**
   * @param line String to be tested
   * @return {@code true} if the line is null, empty or contains only commas and spaces (ie. is
   *     useless), {@code false} otherwise.
   */
  private static boolean lineIsEmpty(String line) {
    return line == null || EMPTY_LINE_PATTERN.matcher(line).matches();
  }

  /**
   * Reads the next line in the CSV file, skipping any lines which contain no data or all empty
   * fields.
   *
   * @return Next non-empty, non-useless line in the CSV file
   */
  private synchronized String readNextNonEmptyLine() {

    String line;

    try {
      do {
        /* read the first line
        if that line is null (ie. EOF)
             do nothing
        if that line is empty (ie. no relevant data besides ","),
             look for the next relevant line and use it */
        line = reader.readLine();
      } while (line != null && lineIsEmpty(line));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }

    return line;
  }

  @Override
  public synchronized void close() {
    try {
      if (reader != null) {
        reader.close();
      }
    } catch (IOException e) {
      log.error("Unable to close CSV resource", e);
    }
  }
}
