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
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeParseException;
import org.junit.Test;

public class LocalDateAdapterTest {

  @Test
  public void testAdaptingLocalDateNull() {
    assertNull(Adapters.adapt(LocalDate.class, null));
  }

  @Test
  public void testAdaptingLocalDateSimple() {
    assertEquals(
        LocalDate.of(2000, Month.JANUARY, 1), Adapters.adapt(LocalDate.class, "2000-01-01"));
  }

  @Test
  public void testAdaptingLocalDateSimpleWithSpacesAndTabs() {
    assertEquals(
        LocalDate.of(2000, Month.JANUARY, 1),
        Adapters.adapt(LocalDate.class, "     2000-01-01   "));
  }

  @Test(expected = DateTimeParseException.class)
  public void testAdaptingLocalDateInvalidInput() {
    Adapters.adapt(LocalDate.class, "hello");
  }

  @Test(expected = DateTimeParseException.class)
  public void testAdaptingLocalDateInvalidDate() {
    Adapters.adapt(LocalDate.class, "2000-13-01");
  }
}
