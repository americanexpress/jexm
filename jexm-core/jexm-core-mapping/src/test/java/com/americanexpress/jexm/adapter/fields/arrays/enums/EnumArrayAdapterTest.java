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

package com.americanexpress.jexm.adapter.fields.arrays.enums;

import static org.junit.Assert.assertArrayEquals;

import com.americanexpress.jexm.adapter.Adapters;
import java.time.DayOfWeek;
import org.junit.Assert;
import org.junit.Test;

public class EnumArrayAdapterTest {

  @Test
  public void testAdaptingNullDayOfWeekShouldReturnEmptyArray() {
    Assert.assertArrayEquals(new DayOfWeek[0], Adapters.adapt(DayOfWeek[].class, null));
  }

  @Test
  public void testAdaptingEmptyDayOfWeekShouldReturnEmptyArray() {
    assertArrayEquals(new DayOfWeek[0], Adapters.adapt(DayOfWeek[].class, ""));
  }

  @Test
  public void testAdaptingEmptyBracketsShouldReturnEmptyArray() {
    assertArrayEquals(new DayOfWeek[0], Adapters.adapt(DayOfWeek[].class, "{}"));
  }

  @Test
  public void testAdaptingEmptyBracketsWithSpacesShouldReturnEmptyArray() {
    assertArrayEquals(new DayOfWeek[0], Adapters.adapt(DayOfWeek[].class, "  {  }  "));
  }

  @Test
  public void testAdaptingOnlySpacesShouldReturnEmptyArray() {
    assertArrayEquals(new DayOfWeek[0], Adapters.adapt(DayOfWeek[].class, "     "));
  }

  @Test
  public void testAdaptingCommaAloneShouldReturnTwoZeroElements() {
    assertArrayEquals(new DayOfWeek[] {null, null}, Adapters.adapt(DayOfWeek[].class, ","));
  }

  @Test
  public void testAdaptingCommaAloneWithSpacesShouldReturnTwoZeroElements() {
    assertArrayEquals(new DayOfWeek[] {null, null}, Adapters.adapt(DayOfWeek[].class, "  ,  "));
  }

  @Test
  public void testAdaptingTwoCommasShouldReturnThreeZeroElements() {
    assertArrayEquals(new DayOfWeek[] {null, null, null}, Adapters.adapt(DayOfWeek[].class, ",,"));
  }

  @Test
  public void testAdaptingSingleElementShouldReturnSingleElementArray() {
    assertArrayEquals(
        new DayOfWeek[] {DayOfWeek.MONDAY}, Adapters.adapt(DayOfWeek[].class, "Monday"));
  }

  @Test
  public void testAdaptingAnotherSingleElementeShouldReturnSingleElementArray() {
    assertArrayEquals(
        new DayOfWeek[] {DayOfWeek.TUESDAY}, Adapters.adapt(DayOfWeek[].class, "Tuesday"));
  }

  @Test
  public void testAdaptingSingleElementWithSpacesShouldReturnSingleElementArray() {
    assertArrayEquals(
        new DayOfWeek[] {DayOfWeek.MONDAY}, Adapters.adapt(DayOfWeek[].class, "  Monday  "));
  }

  @Test
  public void testAdaptingSingleElementShouldMatchCase() {
    assertArrayEquals(
        new DayOfWeek[] {DayOfWeek.MONDAY}, Adapters.adapt(DayOfWeek[].class, "MONDAY"));
  }

  @Test
  public void testAdaptingCommaSeparatedElementsShouldReturnArrayOfElements() {
    assertArrayEquals(
        new DayOfWeek[] {DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY},
        Adapters.adapt(DayOfWeek[].class, "Monday,Tuesday,Wednesday"));
  }

  @Test
  public void testAdaptingCommaSeparatedElementsWithSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(
        new DayOfWeek[] {DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY},
        Adapters.adapt(DayOfWeek[].class, "  Monday  ,  Tuesday  ,  Wednesday  "));
  }

  @Test
  public void testAdaptingMissingFirstElementShouldReturnArrayWithNullFirstElement() {
    assertArrayEquals(
        new DayOfWeek[] {null, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY},
        Adapters.adapt(DayOfWeek[].class, ",Tuesday,Wednesday"));
  }

  @Test
  public void testAdaptingMissingFirstElementWithSpacesShouldReturnArrayWithNullFirstElement() {
    assertArrayEquals(
        new DayOfWeek[] {null, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY},
        Adapters.adapt(DayOfWeek[].class, "   ,Tuesday,Wednesday"));
  }

  @Test
  public void testAdaptingMissingMiddleElementShouldReturnArrayWithNullMiddleElement() {
    assertArrayEquals(
        new DayOfWeek[] {DayOfWeek.MONDAY, null, DayOfWeek.WEDNESDAY},
        Adapters.adapt(DayOfWeek[].class, "Monday,,Wednesday"));
  }

  @Test
  public void testAdaptingMissingMiddleElementWithSpacesShouldReturnArrayWithNullMiddleElement() {
    assertArrayEquals(
        new DayOfWeek[] {DayOfWeek.MONDAY, null, DayOfWeek.WEDNESDAY},
        Adapters.adapt(DayOfWeek[].class, "Monday,   ,Wednesday"));
  }

  @Test
  public void testAdaptingMissingLastElementShouldReturnArrayWithNullLastElement() {
    assertArrayEquals(
        new DayOfWeek[] {DayOfWeek.MONDAY, DayOfWeek.TUESDAY, null},
        Adapters.adapt(DayOfWeek[].class, "Monday,Tuesday,"));
  }

  @Test
  public void testAdaptingMissingLastElementWithSpacesShouldReturnArrayWithNullLastElement() {
    assertArrayEquals(
        new DayOfWeek[] {DayOfWeek.MONDAY, DayOfWeek.TUESDAY, null},
        Adapters.adapt(DayOfWeek[].class, "Monday,Tuesday,  "));
  }

  @Test
  public void testAdaptingCommaSeparatedWithSquareBracketsShouldReturnArrayOfElements() {
    assertArrayEquals(
        new DayOfWeek[] {DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY},
        Adapters.adapt(DayOfWeek[].class, "[Monday,Tuesday,Wednesday]"));
  }

  @Test
  public void testAdaptingCommaSeparatedWithSquareBracketsAndSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(
        new DayOfWeek[] {DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY},
        Adapters.adapt(DayOfWeek[].class, "    [   Monday   ,   Tuesday   ,   Wednesday  ]    "));
  }

  @Test
  public void testAdaptingCommaSeparatedWithRoundBracketsShouldReturnArrayOfElements() {
    assertArrayEquals(
        new DayOfWeek[] {DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY},
        Adapters.adapt(DayOfWeek[].class, "(Monday,Tuesday,Wednesday)"));
  }

  @Test
  public void testAdaptingCommaSeparatedWithRoundBracketsAndSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(
        new DayOfWeek[] {DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY},
        Adapters.adapt(DayOfWeek[].class, "    (   Monday   ,   Tuesday   ,   Wednesday   )   "));
  }

  @Test
  public void testAdaptingCommaSeparatedWithCurlyBracketsShouldReturnArrayOfElements() {
    assertArrayEquals(
        new DayOfWeek[] {DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY},
        Adapters.adapt(DayOfWeek[].class, "{Monday,Tuesday,Wednesday}"));
  }

  @Test
  public void testAdaptingCommaSeparatedWithCurlyBracketsAndSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(
        new DayOfWeek[] {DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY},
        Adapters.adapt(DayOfWeek[].class, "    {   Monday   ,   Tuesday   ,   Wednesday   }   "));
  }
}
