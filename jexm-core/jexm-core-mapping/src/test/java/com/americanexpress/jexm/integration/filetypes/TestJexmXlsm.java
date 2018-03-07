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

package com.americanexpress.jexm.integration.filetypes;

import static org.junit.Assert.assertEquals;

import com.americanexpress.jexm.FileToTest;
import com.americanexpress.jexm.JEXMContext;
import com.americanexpress.jexm.resources.beans.people.PersonFirstName;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Test;

public class TestJexmXlsm {
  // no need for extensive testing here, as XLXM follows the same standard as XLSX,
  // apart from the macros

  @Test
  public final void testPeopleLookingUpByHeaderNameWithSingleHeaderShouldPopulateValues() {
    List<PersonFirstName> expected =
        Arrays.asList(
            new PersonFirstName("Chuck"),
            new PersonFirstName("Bruce"),
            new PersonFirstName("Michael"));

    try (Stream<PersonFirstName> s =
        JEXMContext.newInstance(PersonFirstName.class)
            .read(FileToTest.PERSON_FIRST_NAMES.path("xlsm"))) {
      assertEquals(expected, s.collect(Collectors.toList()));
    }
  }
}
