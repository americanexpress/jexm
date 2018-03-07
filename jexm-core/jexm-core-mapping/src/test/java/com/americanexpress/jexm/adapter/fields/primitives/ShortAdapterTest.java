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

package com.americanexpress.jexm.adapter.fields.primitives;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.americanexpress.jexm.adapter.Adapters;
import org.junit.Test;

public class ShortAdapterTest {

  @Test
  public void testAdaptingShort100() {
    assertEquals(100, (short) Adapters.adapt(short.class, "100"));
  }

  @Test
  public void testAdaptingShort100WithSpacesAndTabs() {
    assertEquals(100, (short) Adapters.adapt(short.class, "     100     "));
  }

  @Test
  public void testAdaptingShortNegative100() {
    assertEquals(-100, (short) Adapters.adapt(short.class, "-100"));
  }

  @Test
  public void testAdaptingShort100WithDecimalsZero() {
    assertEquals(100, (short) Adapters.adapt(short.class, "100.00"));
  }

  @Test
  public void testAdaptingShort100WithDecimalsRoundDown() {
    assertEquals(100, (short) Adapters.adapt(short.class, "100.75"));
  }

  @Test(expected = NumberFormatException.class)
  public void testAdaptingShortInvalidInteger() {
    Adapters.adapt(short.class, "hello");
  }

  @Test
  public void testAdaptingShortNull() {
    assertNull(Adapters.adapt(short.class, null));
  }
}
