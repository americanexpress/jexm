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

import com.americanexpress.jexm.ExcelExtension;
import com.americanexpress.jexm.FileToTest;
import com.americanexpress.jexm.integration.TestJexmAbstract;
import com.americanexpress.jexm.resources.beans.people.PersonFullnameSecondSheetByName;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class TestJexmCsv extends TestJexmAbstract {

  @Override
  public ExcelExtension extension() {
    return ExcelExtension.CSV;
  }

  // Test for multiple XLSX sheets in one document. Should be ignored by CSV.
  @Test
  public final void testPeopleNamesWithMultipleSheetsShouldReadCorrectSheetIfSpecifiedByName() {
    List<PersonFullnameSecondSheetByName> expected =
        Arrays.asList(
            new PersonFullnameSecondSheetByName("Chuck"),
            new PersonFullnameSecondSheetByName("Bruce"),
            new PersonFullnameSecondSheetByName("Michael"));

    assertStreamProducesResult(
        PersonFullnameSecondSheetByName.class, FileToTest.PERSON_FIRST_NAMES, expected);
  }
}
