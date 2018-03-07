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
import com.americanexpress.jexm.parsing.exceptions.SheetNotFoundException;
import com.americanexpress.jexm.resources.beans.date.MonthDay_dmmm;
import com.americanexpress.jexm.resources.beans.date.YearMonth_mmmyy;
import com.americanexpress.jexm.resources.beans.date.localdate.LocalDate_dmmmyy;
import com.americanexpress.jexm.resources.beans.date.localdate.LocalDate_dmyyy;
import com.americanexpress.jexm.resources.beans.date.localdatetime.LocalDateTime_ddmmyyyyhhss;
import com.americanexpress.jexm.resources.beans.date.localtime.*;
import com.americanexpress.jexm.resources.beans.people.PersonFirstName;
import com.americanexpress.jexm.resources.beans.people.PersonFullnameSecondSheetByIndex;
import com.americanexpress.jexm.resources.beans.people.PersonFullnameSecondSheetByName;
import java.time.*;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class TestJexmXlsx extends TestJexmAbstract {

  @Override
  public ExcelExtension extension() {
    return ExcelExtension.XLSX;
  }

  // Test for multiple XLSX sheets in one document
  @Test
  public final void testPeopleNamesWithMultipleSheetsShouldReadFirstSheetByDefault() {
    List<PersonFirstName> expected =
        Arrays.asList(
            new PersonFirstName("Chuck 1"),
            new PersonFirstName("Bruce 1"),
            new PersonFirstName("Michael 1"));

    assertStreamProducesResult(
        PersonFirstName.class, FileToTest.PERSON_FIRST_NAMES_MULTIPLE_SHEETS, expected);
  }

  // Test for multiple XLSX sheets in one document
  @Test
  public final void testPeopleNamesWithMultipleSheetsShouldReadCorrectSheetIfSpecifiedByName() {
    List<PersonFullnameSecondSheetByName> expected =
        Arrays.asList(
            new PersonFullnameSecondSheetByName("Chuck 2"),
            new PersonFullnameSecondSheetByName("Bruce 2"),
            new PersonFullnameSecondSheetByName("Michael 2"));

    assertStreamProducesResult(
        PersonFullnameSecondSheetByName.class,
        FileToTest.PERSON_FIRST_NAMES_MULTIPLE_SHEETS,
        expected);
  }

  @Test(expected = SheetNotFoundException.class)
  public final void testPeopleNamesWithMultipleSheetsShouldThrowExceptionIfSheetNotFoundByName() {
    streamToList(PersonFullnameSecondSheetByName.class, FileToTest.PERSON_FIRST_NAMES);
  }

  // Test for multiple XLSX sheets in one document
  @Test
  public final void testPeopleNamesWithMultipleSheetsShouldReadCorrectSheetIfSpecifiedByIndex() {
    List<PersonFullnameSecondSheetByIndex> expected =
        Arrays.asList(
            new PersonFullnameSecondSheetByIndex("Chuck 2"),
            new PersonFullnameSecondSheetByIndex("Bruce 2"),
            new PersonFullnameSecondSheetByIndex("Michael 2"));

    assertStreamProducesResult(
        PersonFullnameSecondSheetByIndex.class,
        FileToTest.PERSON_FIRST_NAMES_MULTIPLE_SHEETS,
        expected);
  }

  @Test(expected = SheetNotFoundException.class)
  public final void testPeopleNamesWithMultipleSheetsShouldThrowExceptionIfSheetNotFoundByIndex() {
    streamToList(PersonFullnameSecondSheetByIndex.class, FileToTest.PERSON_FIRST_NAMES);
  }

  // Test for XLSX specific styling of cells
  @Test
  public final void testPeopleNamesInStyledSheetShouldIgnoreStyling() {
    List<PersonFirstName> expected =
        Arrays.asList(
            new PersonFirstName("Chuck"),
            new PersonFirstName("Bruce"),
            new PersonFirstName("Michael"));

    assertStreamProducesResult(
        PersonFirstName.class, FileToTest.PERSON_FIRST_NAMES_STYLED, expected);
  }

  // Tests for XLSX specific date formats
  @Test
  public final void testLocalDate_dmmmyy() {
    List<LocalDate_dmmmyy> expected =
        Arrays.asList(new LocalDate_dmmmyy(LocalDate.of(2017, Month.JUNE, 1)));

    assertStreamProducesResult(LocalDate_dmmmyy.class, FileToTest.DATES_AND_TIMES, expected);
  }

  @Test
  public final void testLocalDate_dmyyy() {
    List<LocalDate_dmyyy> expected =
        Arrays.asList(new LocalDate_dmyyy(LocalDate.of(2017, Month.JUNE, 1)));

    assertStreamProducesResult(LocalDate_dmyyy.class, FileToTest.DATES_AND_TIMES, expected);
  }

  @Test
  public final void testLocalDateTime_dmyyy() {
    List<LocalDateTime_ddmmyyyyhhss> expected =
        Arrays.asList(
            new LocalDateTime_ddmmyyyyhhss(LocalDateTime.of(2017, Month.JUNE, 1, 18, 0, 30)));

    assertStreamProducesResult(
        LocalDateTime_ddmmyyyyhhss.class, FileToTest.DATES_AND_TIMES, expected);
  }

  @Test
  public final void testLocalTime_Hmm() {
    List<LocalTime_Hmm> expected = Arrays.asList(new LocalTime_Hmm(LocalTime.of(18, 0, 30)));

    assertStreamProducesResult(LocalTime_Hmm.class, FileToTest.DATES_AND_TIMES, expected);
  }

  @Test
  public final void testLocalTime_Hmmss() {
    List<LocalTime_Hmmss> expected = Arrays.asList(new LocalTime_Hmmss(LocalTime.of(18, 0, 30)));

    assertStreamProducesResult(LocalTime_Hmmss.class, FileToTest.DATES_AND_TIMES, expected);
  }

  @Test
  public final void testLocalTime_hmmsstt() {
    List<LocalTime_hmmsstt> expected =
        Arrays.asList(new LocalTime_hmmsstt(LocalTime.of(18, 0, 30)));

    assertStreamProducesResult(LocalTime_hmmsstt.class, FileToTest.DATES_AND_TIMES, expected);
  }

  @Test
  public final void testLocalTime_hmmtt() {
    List<LocalTime_hmmtt> expected = Arrays.asList(new LocalTime_hmmtt(LocalTime.of(18, 0, 30)));

    assertStreamProducesResult(LocalTime_hmmtt.class, FileToTest.DATES_AND_TIMES, expected);
  }

  @Test
  public final void testLocalTime_lower_hmmss() {
    List<LocalTime_lower_hmmss> expected =
        Arrays.asList(new LocalTime_lower_hmmss(LocalTime.of(18, 0, 30)));

    assertStreamProducesResult(LocalTime_lower_hmmss.class, FileToTest.DATES_AND_TIMES, expected);
  }

  @Test
  public final void testLocalTime_mmss() {
    List<LocalTime_mmss> expected = Arrays.asList(new LocalTime_mmss(LocalTime.of(18, 0, 30)));

    assertStreamProducesResult(LocalTime_mmss.class, FileToTest.DATES_AND_TIMES, expected);
  }

  @Test
  public final void testLocalTime_mmss0() {
    List<LocalTime_mmss0> expected = Arrays.asList(new LocalTime_mmss0(LocalTime.of(18, 0, 30)));

    assertStreamProducesResult(LocalTime_mmss0.class, FileToTest.DATES_AND_TIMES, expected);
  }

  @Test
  public final void testLocalTime_sq_h_sqmmss() {
    List<LocalTime_sq_h_sqmmss> expected =
        Arrays.asList(new LocalTime_sq_h_sqmmss(LocalTime.of(18, 0, 30)));

    assertStreamProducesResult(LocalTime_sq_h_sqmmss.class, FileToTest.DATES_AND_TIMES, expected);
  }

  @Test
  public final void testMonthDay_dmmm() {
    List<MonthDay_dmmm> expected = Arrays.asList(new MonthDay_dmmm(MonthDay.of(Month.JUNE, 1)));

    assertStreamProducesResult(MonthDay_dmmm.class, FileToTest.DATES_AND_TIMES, expected);
  }

  @Test
  public final void testYearMonth_mmmyy() {
    List<YearMonth_mmmyy> expected =
        Arrays.asList(new YearMonth_mmmyy(YearMonth.of(2017, Month.JUNE)));

    assertStreamProducesResult(YearMonth_mmmyy.class, FileToTest.DATES_AND_TIMES, expected);
  }
}
