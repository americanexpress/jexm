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

package com.americanexpress.jexm.integration;

import static org.junit.Assert.assertEquals;

import com.americanexpress.jexm.JEXMContext;
import com.americanexpress.jexm.parsing.RawRowIterator;
import com.americanexpress.jexm.resources.beans.people.PersonAge;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.Test;

public class TestUserDefinedIterator {

  @Test
  public void testSimpleUserDefinedIterator() {

    String input = "Name_Age_Chuck_77_Bruce_76_Michael_58";

    List<PersonAge> list =
        JEXMContext.newInstance(PersonAge.class)
            .read(new MyUnderscoreSeparatedRawRowIterator(input))
            .collect(Collectors.toList());

    assertEquals(
        Arrays.asList(
            new PersonAge("Chuck", 77), new PersonAge("Bruce", 76), new PersonAge("Michael", 58)),
        list);
  }

  private class MyUnderscoreSeparatedRawRowIterator extends RawRowIterator {

    private final String[] values;
    private int currentRowNumber = 0;

    private MyUnderscoreSeparatedRawRowIterator(String input) {
      this.values = Objects.requireNonNull(input).split("_");
      if (values.length < 2) {
        throw new IllegalArgumentException();
      }

      headerIndexes = RawRowIterator.createHeaderIndexes(readNextRow());
    }

    @Override
    public Map<Integer, String> readNextRow() {
      if (currentRowNumber * 2 >= values.length) {
        return null;
      }

      Map<Integer, String> map =
          IntStream.range(currentRowNumber * 2, (currentRowNumber + 1) * 2)
              .boxed()
              .collect(Collectors.toMap(i -> i % 2, i -> values[i]));

      currentRowNumber++;

      return map;
    }

    @Override
    public void close() {}
  }
}
