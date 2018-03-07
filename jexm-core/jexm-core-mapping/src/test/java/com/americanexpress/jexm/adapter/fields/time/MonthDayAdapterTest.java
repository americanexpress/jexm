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

package com.americanexpress.jexm.adapter.fields.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.americanexpress.jexm.adapter.Adapters;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.format.DateTimeParseException;
import org.junit.Test;

public class MonthDayAdapterTest {

  @Test
  public void testAdaptingMonthDayNull() {
    assertNull(Adapters.adapt(MonthDay.class, null));
  }

  @Test
  public void testAdaptingMonthDaySimple() {
    assertEquals(MonthDay.of(6, 15), Adapters.adapt(MonthDay.class, "--06-15"));
  }

  @Test
  public void testAdaptingMonthDaySimpleWithSpacesAndTabs() {
    assertEquals(MonthDay.of(6, 15), Adapters.adapt(MonthDay.class, "     --06-15    "));
  }

  @Test(expected = DateTimeParseException.class)
  public void testAdaptingMonthDayOnlyInvalidMonthDay() {
    assertEquals(MonthDay.of(6, 15), Adapters.adapt(MonthDay.class, "--15-6"));
  }

  @Test(expected = DateTimeParseException.class)
  public void testAdaptingLocalTimeInvalidInput() {
    Adapters.adapt(LocalTime.class, "hello");
  }
}
