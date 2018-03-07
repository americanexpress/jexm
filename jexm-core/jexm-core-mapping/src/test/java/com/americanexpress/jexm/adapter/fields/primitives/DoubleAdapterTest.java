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

public class DoubleAdapterTest {

  @Test
  public void testAdaptingDouble100() {
    assertEquals(100.0d, Adapters.adapt(double.class, "100"), 0.0001);
  }

  @Test
  public void testAdaptingDouble100WithSpacesAndTabs() {
    assertEquals(100.0d, Adapters.adapt(double.class, "     100     "), 0.0001);
  }

  @Test
  public void testAdaptingDoubleNegative100() {
    assertEquals(-100.0d, Adapters.adapt(double.class, "-100"), 0.0001);
  }

  @Test
  public void testAdaptingDouble100WithDecimalsZero() {
    assertEquals(100.0d, Adapters.adapt(double.class, "100.00"), 0.0001);
  }

  @Test
  public void testAdaptingDouble100WithDecimals() {
    assertEquals(100.75d, Adapters.adapt(double.class, "100.75"), 0.0001);
  }

  @Test(expected = NumberFormatException.class)
  public void testAdaptingDoubleInvalidInteger() {
    Adapters.adapt(double.class, "hello");
  }

  @Test
  public void testAdaptingDoubleNull() {
    assertNull(Adapters.adapt(double.class, null));
  }
}
