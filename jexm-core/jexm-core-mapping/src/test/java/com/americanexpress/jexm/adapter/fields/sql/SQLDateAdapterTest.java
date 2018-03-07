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

package com.americanexpress.jexm.adapter.fields.sql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.americanexpress.jexm.adapter.Adapters;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;
import org.junit.Test;

public class SQLDateAdapterTest {

  @Test
  public void testAdaptingSQLDateNull() {
    assertNull(Adapters.adapt(Date.class, null));
  }

  @Test
  public void testAdaptingSQLDateSimple() {
    assertEquals(
        Date.valueOf(LocalDate.of(2000, Month.JANUARY, 1)),
        Adapters.adapt(Date.class, "2000-01-01"));
  }

  @Test
  public void testAdaptingSQLSimpleWithSpacesAndTabs() {
    assertEquals(
        Date.valueOf(LocalDate.of(2000, Month.JANUARY, 1)),
        Adapters.adapt(Date.class, "     2000-01-01   "));
  }

  @Test
  public void testAdaptingSQLDateSimpleWithSpacesAndTabs() {
    assertEquals(
        Date.valueOf(LocalDate.of(2000, Month.JANUARY, 1)),
        Adapters.adapt(Date.class, "     2000-01-01   "));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAdaptingSQLDateInvalidInput() {
    Adapters.adapt(Date.class, "hello");
  }
}
