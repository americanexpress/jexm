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

package com.americanexpress.jexm.adapter.fields.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.americanexpress.jexm.adapter.Adapters;
import java.time.DayOfWeek;
import java.time.Month;
import org.junit.Assert;
import org.junit.Test;

public class EnumAdapterTest {

  @Test
  public void testAdaptingEnumMatchingExactlyMonday() {
    Assert.assertEquals(DayOfWeek.MONDAY, Adapters.adapt(DayOfWeek.class, "MONDAY"));
  }

  @Test
  public void testAdaptingEnumMatchingExactlyTuesday() {
    assertEquals(DayOfWeek.TUESDAY, Adapters.adapt(DayOfWeek.class, "TUESDAY"));
  }

  @Test
  public void testAdaptingEnumMatchingExactlyJune() {
    assertEquals(Month.JUNE, Adapters.adapt(Month.class, "JUNE"));
  }

  @Test
  public void testAdaptingEnumWithTrailingSpacesAndTabs() {
    assertEquals(DayOfWeek.MONDAY, Adapters.adapt(DayOfWeek.class, "MONDAY     "));
  }

  @Test
  public void testAdaptingEnumWithPrecedingSpacesAndTabs() {
    assertEquals(DayOfWeek.MONDAY, Adapters.adapt(DayOfWeek.class, "     MONDAY"));
  }

  @Test
  public void testAdaptingEnumWithTrailingAndPrecedingSpacesAndTabs() {
    assertEquals(DayOfWeek.MONDAY, Adapters.adapt(DayOfWeek.class, "     MONDAY     "));
  }

  @Test
  public void testAdaptingEnumFirstLetterUppercase() {
    assertEquals(DayOfWeek.MONDAY, Adapters.adapt(DayOfWeek.class, "Monday"));
  }

  @Test
  public void testAdaptingEnumAllLowercase() {
    assertEquals(DayOfWeek.MONDAY, Adapters.adapt(DayOfWeek.class, "monday"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAdaptingEnumIllegalEnum() {
    Adapters.adapt(DayOfWeek.class, "hello");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAdaptingEnumMisspelling() {
    Adapters.adapt(DayOfWeek.class, "MOONDAY");
  }

  @Test
  public void testAdaptingEnumNull() {
    assertNull(Adapters.adapt(DayOfWeek.class, null));
  }
}
