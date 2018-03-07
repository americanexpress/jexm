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

package com.americanexpress.jexm.adapter.utils;

import static org.junit.Assert.*;

import com.americanexpress.jexm.adapter.exceptions.ParserException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import org.junit.Test;

public class AdapterUtilsTest {

  @Test
  public void testParseStringTrueShouldReturnTrue() throws Exception {
    assertTrue(AdapterUtils.parseBoolean("True"));
  }

  @Test
  public void testParseStringtrueShouldReturnTrue() throws Exception {
    assertTrue(AdapterUtils.parseBoolean("true"));
  }

  @Test
  public void testParseString1ShouldReturnTrue() throws Exception {
    assertTrue(AdapterUtils.parseBoolean("1"));
  }

  @Test
  public void testParseStringyesShouldReturnTrue() throws Exception {
    assertTrue(AdapterUtils.parseBoolean("yes"));
  }

  @Test
  public void testParseStringYesShouldReturnTrue() throws Exception {
    assertTrue(AdapterUtils.parseBoolean("Yes"));
  }

  @Test
  public void testParseStringYESShouldReturnTrue() throws Exception {
    assertTrue(AdapterUtils.parseBoolean("YES"));
  }

  @Test
  public void testParseStringTShouldReturnTrue() throws Exception {
    assertTrue(AdapterUtils.parseBoolean("T"));
  }

  @Test
  public void testParseStringFalseShouldReturnFalse() throws Exception {
    assertFalse(AdapterUtils.parseBoolean("False"));
  }

  @Test
  public void testParseStringfalseShouldReturnFalse() throws Exception {
    assertFalse(AdapterUtils.parseBoolean("false"));
  }

  @Test
  public void testParseString0ShouldReturnFalse() throws Exception {
    assertFalse(AdapterUtils.parseBoolean("0"));
  }

  @Test
  public void testParseStringnoShouldReturnFalse() throws Exception {
    assertFalse(AdapterUtils.parseBoolean("no"));
  }

  @Test
  public void testParseStringNoShouldReturnFalse() throws Exception {
    assertFalse(AdapterUtils.parseBoolean("No"));
  }

  @Test
  public void testParseStringFShouldReturnFalse() throws Exception {
    assertFalse(AdapterUtils.parseBoolean("F"));
  }

  @Test(expected = ParserException.class)
  public void testParseInvalidStringShouldThrowException() {
    AdapterUtils.parseBoolean("hello");
  }

  @Test(expected = NullPointerException.class)
  public void testParseNullStringShouldThrowException() {
    AdapterUtils.parseBoolean(null);
  }

  @Test
  public void testRemoveDecimals100ShouldLeaveIntact() throws Exception {
    assertEquals("100", AdapterUtils.withoutDecimals("100"));
  }

  @Test
  public void testRemoveDecimals100_0ShouldRemoveDecimals() throws Exception {
    assertEquals("100", AdapterUtils.withoutDecimals("100"));
  }

  @Test
  public void testRemoveDecimals100_123ShouldRemoveDecimals() throws Exception {
    assertEquals("100", AdapterUtils.withoutDecimals("100.123"));
  }

  @Test
  public void testRemoveDecimalsABCS_123ShouldRemoveDecimals() throws Exception {
    assertEquals("ABC", AdapterUtils.withoutDecimals("ABC.123"));
  }

  @Test(expected = NullPointerException.class)
  public void testRemoveDecimalsNullInputShouldThrowException() throws Exception {
    AdapterUtils.withoutDecimals(null);
  }

  @Test
  public void testFindMondayInDayOfWeekShouldReturnMONDAY() throws Exception {
    assertEquals(DayOfWeek.MONDAY, AdapterUtils.findEnumIgnoreCase(DayOfWeek.class, "Monday"));
  }

  @Test
  public void testFindMONDAYInDayOfWeekShouldReturnMONDAY() throws Exception {
    assertEquals(DayOfWeek.MONDAY, AdapterUtils.findEnumIgnoreCase(DayOfWeek.class, "MONDAY"));
  }

  @Test
  public void testFindmondayInDayOfWeekShouldReturnMONDAY() throws Exception {
    assertEquals(DayOfWeek.MONDAY, AdapterUtils.findEnumIgnoreCase(DayOfWeek.class, "monday"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindJanuaryInDayOfWeekShouldThrowException() throws Exception {
    AdapterUtils.findEnumIgnoreCase(DayOfWeek.class, "January");
  }

  @Test(expected = NullPointerException.class)
  public void testFindNullInDayOfWeekShouldThrowException() throws Exception {
    AdapterUtils.findEnumIgnoreCase(DayOfWeek.class, null);
  }

  @Test(expected = NullPointerException.class)
  public void testFindMondayInNullEnumShouldThrowException() throws Exception {
    AdapterUtils.findEnumIgnoreCase(null, "Monday");
  }

  @Test
  public void testParseDate2010_06_20ShouldReturnCorrectDate() throws Exception {
    assertEquals(new Date(110, 5, 20), AdapterUtils.parseDate("2010-06-20"));
  }

  @Test
  public void testParseLocalDateFromDatetimeShouldReturnCorrectDate() throws Exception {
    assertEquals(LocalDate.of(2010, 6, 20), AdapterUtils.parseLocalDate("2010-06-20T12:30:00"));
  }

  @Test
  public void testParseLocalDateFromDatetimeWithZShouldReturnCorrectDate() throws Exception {
    assertEquals(LocalDate.of(2010, 6, 20), AdapterUtils.parseLocalDate("2010-06-20T12:30:00Z"));
  }

  @Test
  public void testParseLocalDateTimeWithZShouldReturnCorrectDate() throws Exception {
    assertEquals(
        LocalDateTime.of(2010, 6, 20, 12, 30),
        AdapterUtils.parseLocalDateTime("2010-06-20T12:30:00Z"));
  }

  @Test
  public void testParseLocalDateTimeWithoutZShouldReturnCorrectDateTime() throws Exception {
    assertEquals(
        LocalDateTime.of(2010, 6, 20, 12, 30),
        AdapterUtils.parseLocalDateTime("2010-06-20T12:30:00"));
  }

  @Test
  public void testParseLocalDateTimeWithoutTimeShouldReturnCorrectDateTime() throws Exception {
    assertEquals(
        LocalDateTime.of(2010, 6, 20, 0, 0), AdapterUtils.parseLocalDateTime("2010-06-20"));
  }

  @Test(expected = RuntimeException.class)
  public void testParseInvalidDateShouldThrowException() throws Exception {
    AdapterUtils.parseDate("ABC_2010-06-20");
  }

  @Test(expected = NullPointerException.class)
  public void testParseNullDateShouldThrowException() throws Exception {
    AdapterUtils.parseDate(null);
  }
}
