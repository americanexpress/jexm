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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import org.junit.Test;

public class TimeStampAdapterTest {

  @Test
  public void testAdaptingTimeStampNull() {
    assertNull(Adapters.adapt(Timestamp.class, null));
  }

  @Test
  public void testAdaptingTimeStampSimple() {
    assertEquals(
        Timestamp.valueOf(LocalDateTime.of(2011, 10, 2, 18, 48, 5, 123456000)),
        Adapters.adapt(Timestamp.class, "2011-10-02 18:48:05.123456"));
  }

  @Test
  public void testAdaptingTimeStampSimpleWithSpacesAndTabs() {
    assertEquals(
        Timestamp.valueOf(LocalDateTime.of(2011, 10, 2, 18, 48, 5, 123456000)),
        Adapters.adapt(Timestamp.class, "     2011-10-02 18:48:05.123456     "));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAdaptingTimeStampInvalidInput() {
    Adapters.adapt(Timestamp.class, "hello");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAdaptingTimeStampInvalidTime() {
    Adapters.adapt(Timestamp.class, "2011-10-0218:48:05.123456");
  }
}
