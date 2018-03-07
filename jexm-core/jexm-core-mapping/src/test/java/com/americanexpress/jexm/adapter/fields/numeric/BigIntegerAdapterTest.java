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

package com.americanexpress.jexm.adapter.fields.numeric;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.americanexpress.jexm.adapter.Adapters;
import java.math.BigInteger;
import org.junit.Assert;
import org.junit.Test;

public class BigIntegerAdapterTest {
  @Test
  public void testAdaptingBigInteger9876543210987654321() {
    Assert.assertEquals(
        new BigInteger("9876543210987654321"),
        Adapters.adapt(BigInteger.class, "9876543210987654321"));
  }

  @Test
  public void testAdaptingBigInteger9876543210987654321WithSpacesAndTabs() {
    assertEquals(
        new BigInteger("9876543210987654321"),
        Adapters.adapt(BigInteger.class, "     9876543210987654321     "));
  }

  @Test
  public void testAdaptingBigIntegerNegative9876543210987654321() {
    assertEquals(
        new BigInteger("-9876543210987654321"),
        Adapters.adapt(BigInteger.class, "-9876543210987654321"));
  }

  @Test
  public void testAdaptingBigInteger100WithDecimalsZero() {
    assertEquals(
        new BigInteger("9876543210987654321"),
        Adapters.adapt(BigInteger.class, "9876543210987654321.00"));
  }

  @Test
  public void testAdaptingBigInteger100WithDecimalsRoundDown() {
    assertEquals(
        new BigInteger("9876543210987654321"),
        Adapters.adapt(BigInteger.class, "9876543210987654321.75"));
  }

  @Test(expected = NumberFormatException.class)
  public void testAdaptingBigIntegerInvalidInteger() {
    Adapters.adapt(BigInteger.class, "hello");
  }

  @Test
  public void testAdaptingBigIntegerNull() {
    assertNull(Adapters.adapt(BigInteger.class, null));
  }
}
