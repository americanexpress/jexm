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

import com.americanexpress.jexm.CloseableIterator;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class providing an iterator for each row of an Excel document containing their raw String cells.
 * Each row in the iterator is represented by a map of column indexes to their String values ({@code
 * Map<Integer, String>}. Example: {0 -> "Chuck", 1 -> "Smith"} where 1 is the first column and
 * "Smith" is the value of that column at the current row. Entries of the iterator are lazily
 * evaluated and are not parsed from disk until {@link RawRowIterator#hasNext()} or {@link
 * RawRowIterator#next()} are called. This is a low/mid-level class which does not perform any
 * mapping. It is used as an abstract class for any parser of Excel or table-based documents. The
 * {@link com.americanexpress.jexm.mapping.MappedRowIterator} makes use of this class to perform the
 * actual Java bean field mapping.
 */
public abstract class RawRowIterator implements CloseableIterator<Map<Integer, String>> {

  private static final Logger log = LoggerFactory.getLogger(RawRowIterator.class);

  protected Map<String, Integer> headerIndexes;
  private Map<Integer, String> nextRow = null;

  /**
   * Used to fetch the next row in the Excel document as a index-to-value map. If this returns
   * {@code null}, the iterator will assume there are no more items to follow and {@link
   * RawRowIterator#hasNext()} will return {@code false}.
   *
   * @return Index-to-value map of the next row
   */
  public abstract Map<Integer, String> readNextRow();

  @Override
  public final boolean hasNext() {
    if (nextRow == null) {
      return (nextRow = readNextRow()) != null;
    } else {
      return true;
    }
  }

  @Override
  public final Map<Integer, String> next() {
    if (nextRow != null || hasNext()) {
      Map<Integer, String> rawCells = nextRow;
      nextRow = null;
      return rawCells;
    } else {
      throw new NoSuchElementException();
    }
  }

  /**
   * Returns the header indexes of the Excel document.
   *
   * @return Header name to column index map. Example: {"Name" -> 0, "Middlename" -> 1, "Lastname"
   *     -> 2}
   */
  public final Map<String, Integer> headerIndexes() {
    return headerIndexes;
  }

  /**
   * Produces the header name to index map from an index to header name map. This means in a way it
   * flips keys with values of the original map. If multiple headers have the same name, a warning
   * is logged and only the first is used.
   *
   * @param headerIndexToName Index to header name map. Example: {0 -> "Name", 1 -> "Middlename", 2
   *     -> "Lastname"}
   * @return Header name to index map
   */
  protected static Map<String, Integer> createHeaderIndexes(
      Map<Integer, String> headerIndexToName) {
    if (headerIndexToName == null) {
      return Collections.emptyMap();
    }

    return Collections.unmodifiableMap(
        headerIndexToName
            .entrySet()
            .stream()
            .filter(e -> e.getValue() != null)
            .filter(e -> !e.getValue().isEmpty())
            .collect(
                Collectors.toMap(
                    Map.Entry::getValue,
                    Map.Entry::getKey,
                    (i1, i2) -> {
                      warnHeaderClash(headerIndexToName.get(i1));
                      return i1;
                    })));
  }

  private static void warnHeaderClash(String header) {
    log.warn(
        "Multiple occurrences of header '{}' were found. Using the first occurrence only.", header);
  }
}
