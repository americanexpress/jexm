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

package com.americanexpress.jexm.adapter.fields.arrays.boxed;

import static org.junit.Assert.assertArrayEquals;

import com.americanexpress.jexm.adapter.Adapters;
import org.junit.Assert;
import org.junit.Test;

public class ByteArrayAdapterTest {

  @Test
  public void testAdaptingNullStringShouldReturnEmptyArray() {
    Assert.assertArrayEquals(new Byte[0], Adapters.adapt(Byte[].class, null));
  }

  @Test
  public void testAdaptingEmptyStringShouldReturnEmptyArray() {
    assertArrayEquals(new Byte[0], Adapters.adapt(Byte[].class, ""));
  }

  @Test
  public void testAdaptingEmptyBracketsShouldReturnEmptyArray() {
    assertArrayEquals(new Byte[0], Adapters.adapt(Byte[].class, "{}"));
  }

  @Test
  public void testAdaptingEmptyBracketsWithSpacesShouldReturnEmptyArray() {
    assertArrayEquals(new Byte[0], Adapters.adapt(Byte[].class, "  {  }  "));
  }

  @Test
  public void testAdaptingOnlySpacesShouldReturnEmptyArray() {
    assertArrayEquals(new Byte[0], Adapters.adapt(Byte[].class, "     "));
  }

  @Test
  public void testAdaptingCommaAloneShouldReturnTwoZeroElements() {
    assertArrayEquals(new Byte[] {null, null}, Adapters.adapt(Byte[].class, ","));
  }

  @Test
  public void testAdaptingCommaAloneWithSpacesShouldReturnTwoZeroElements() {
    assertArrayEquals(new Byte[] {null, null}, Adapters.adapt(Byte[].class, "  ,  "));
  }

  @Test
  public void testAdaptingTwoCommasShouldReturnThreeZeroElements() {
    assertArrayEquals(new Byte[] {null, null, null}, Adapters.adapt(Byte[].class, ",,"));
  }

  @Test
  public void testAdaptingSingleElementShouldReturnSingleElementArray() {
    assertArrayEquals(new Byte[] {1}, Adapters.adapt(Byte[].class, "1"));
  }

  @Test
  public void testAdaptingSingleElementWithSpacesShouldReturnSingleElementArray() {
    assertArrayEquals(new Byte[] {1}, Adapters.adapt(Byte[].class, "  1  "));
  }

  @Test
  public void testAdaptingSingleElementNegativeShouldReturnSingleElementArray() {
    assertArrayEquals(new Byte[] {-1}, Adapters.adapt(Byte[].class, "-1"));
  }

  @Test
  public void testAdaptingSingleElementDecimalShouldRoundDown() {
    assertArrayEquals(new Byte[] {1}, Adapters.adapt(Byte[].class, "1.2"));
  }

  @Test
  public void testAdaptingCommaSeparatedElementsShouldReturnArrayOfElements() {
    assertArrayEquals(new Byte[] {1, 2, 3}, Adapters.adapt(Byte[].class, "1,2,3"));
  }

  @Test
  public void testAdaptingCommaSeparatedElementsWithSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(new Byte[] {1, 2, 3}, Adapters.adapt(Byte[].class, "  1  ,  2  ,  3  "));
  }

  @Test
  public void testAdaptingMissingFirstElementShouldReturnArrayWithNullFirstElement() {
    assertArrayEquals(new Byte[] {null, 2, 3}, Adapters.adapt(Byte[].class, ",2,3"));
  }

  @Test
  public void testAdaptingMissingFirstElementWithSpacesShouldReturnArrayWithNullFirstElement() {
    assertArrayEquals(new Byte[] {null, 2, 3}, Adapters.adapt(Byte[].class, "   ,2,3"));
  }

  @Test
  public void testAdaptingMissingMiddleElementShouldReturnArrayWithNullMiddleElement() {
    assertArrayEquals(new Byte[] {1, null, 3}, Adapters.adapt(Byte[].class, "1,,3"));
  }

  @Test
  public void testAdaptingMissingMiddleElementWithSpacesShouldReturnArrayWithNullMiddleElement() {
    assertArrayEquals(new Byte[] {1, null, 3}, Adapters.adapt(Byte[].class, "1,   ,3"));
  }

  @Test
  public void testAdaptingMissingLastElementShouldReturnArrayWithNullLastElement() {
    assertArrayEquals(new Byte[] {1, 2, null}, Adapters.adapt(Byte[].class, "1,2,"));
  }

  @Test
  public void testAdaptingMissingLastElementWithSpacesShouldReturnArrayWithNullLastElement() {
    assertArrayEquals(new Byte[] {1, 2, null}, Adapters.adapt(Byte[].class, "1,2,  "));
  }

  @Test
  public void testAdaptingCommaSeparatedWithSquareBracketsShouldReturnArrayOfElements() {
    assertArrayEquals(new Byte[] {1, 2, 3}, Adapters.adapt(Byte[].class, "[1,2,3]"));
  }

  @Test
  public void testAdaptingCommaSeparatedWithSquareBracketsAndSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(
        new Byte[] {1, 2, 3}, Adapters.adapt(Byte[].class, "    [   1   ,   2   ,   3  ]    "));
  }

  @Test
  public void testAdaptingCommaSeparatedWithRoundBracketsShouldReturnArrayOfElements() {
    assertArrayEquals(new Byte[] {1, 2, 3}, Adapters.adapt(Byte[].class, "(1,2,3)"));
  }

  @Test
  public void testAdaptingCommaSeparatedWithRoundBracketsAndSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(
        new Byte[] {1, 2, 3}, Adapters.adapt(Byte[].class, "    (   1   ,   2   ,   3   )   "));
  }

  @Test
  public void testAdaptingCommaSeparatedWithCurlyBracketsShouldReturnArrayOfElements() {
    assertArrayEquals(new Byte[] {1, 2, 3}, Adapters.adapt(Byte[].class, "{1,2,3}"));
  }

  @Test
  public void testAdaptingCommaSeparatedWithCurlyBracketsAndSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(
        new Byte[] {1, 2, 3}, Adapters.adapt(Byte[].class, "    {   1   ,   2   ,   3   }   "));
  }

  @Test
  public void testAdaptingCommaSeparatedElementsInvalidMiddleElementShouldThrowException() {
    assertArrayEquals(new Byte[] {1, null, 3}, Adapters.adapt(Byte[].class, "1,hello,3"));
  }

  @Test
  public void testAdaptingIllegalUseOfBracketsMiddleShouldThrowException() {
    assertArrayEquals(new Byte[] {1, null, null}, Adapters.adapt(Byte[].class, "1,[2,3]"));
  }

  @Test
  public void testAdaptingIllegalUseOfBracketsNonClosingShouldThrowException() {
    assertArrayEquals(new Byte[] {null, 2, 3}, Adapters.adapt(Byte[].class, "[1,2,3"));
  }
}
