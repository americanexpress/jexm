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

import static org.junit.Assert.*;

import com.americanexpress.jexm.adapter.Adapters;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeParseException;
import java.util.Date;
import org.junit.Test;

public class DateAdapterTest {

  @Test
  public void testAdaptingLocalDateTimeNull() {
    assertNull(Adapters.adapt(Date.class, null));
  }

  @Test
  public void testAdaptingLocalDateTimeWithoutTime() {
    assertEquals(toDate("2000-01-01"), Adapters.adapt(Date.class, "2000-01-01"));
  }

  @Test
  public void testAdaptingLocalDateTimeSimpleWithSpacesAndTabs() {
    assertEquals(toDate("2000-01-01"), Adapters.adapt(Date.class, "     2000-01-01   "));
  }

  @Test(expected = DateTimeParseException.class)
  public void testAdaptingLocalDateInvalidInput() {
    Adapters.adapt(Date.class, "hello");
  }

  /*
  @Test(expected = DateTimeParseException.class)
  public void testAdaptingLocalDateInvalidDate(){
      Adapters.adapt(Date.class, "2000-01-32");
  }
  */

  private static final DateFormat ISO_8601 = new SimpleDateFormat("yyyy-MM-dd");

  private static Date toDate(String s) {
    try {
      return ISO_8601.parse(s);
    } catch (ParseException e) {
      fail(e.getMessage());
      return null;
    }
  }
}
