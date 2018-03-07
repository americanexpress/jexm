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

public class IntegerAdapterTest {

  @Test
  public void testAdaptingInteger100() {
    assertEquals(100, (int) Adapters.adapt(int.class, "100"));
  }

  @Test
  public void testAdaptingInteger100WithSpacesAndTabs() {
    assertEquals(100, (int) Adapters.adapt(int.class, "     100     "));
  }

  @Test
  public void testEmpty() {
    assertNull(Adapters.adapt(int.class, " "));
  }

  @Test
  public void testAdaptingIntegerNegative100() {
    assertEquals(-100, (int) Adapters.adapt(int.class, "-100"));
  }

  @Test
  public void testAdaptingInteger100WithDecimalsZero() {
    assertEquals(100, (int) Adapters.adapt(int.class, "100.00"));
  }

  @Test
  public void testAdaptingInteger100WithDecimalsRoundDown() {
    assertEquals(100, (int) Adapters.adapt(int.class, "100.75"));
  }

  @Test(expected = NumberFormatException.class)
  public void testAdaptingIntegerInvalidInteger() {
    Adapters.adapt(int.class, "hello");
  }

  @Test
  public void testAdaptingIntegerNull() {
    assertNull(Adapters.adapt(int.class, null));
  }
}
