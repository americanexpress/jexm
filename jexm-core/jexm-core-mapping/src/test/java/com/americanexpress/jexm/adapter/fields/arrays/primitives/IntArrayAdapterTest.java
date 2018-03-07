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

package com.americanexpress.jexm.adapter.fields.arrays.primitives;

import static org.junit.Assert.assertArrayEquals;

import com.americanexpress.jexm.adapter.Adapters;
import org.junit.Assert;
import org.junit.Test;

public class IntArrayAdapterTest {

  @Test
  public void testAdaptingNullStringShouldReturnEmptyArray() {
    Assert.assertArrayEquals(new int[0], Adapters.adapt(int[].class, null));
  }

  @Test
  public void testAdaptingEmptyStringShouldReturnEmptyArray() {
    assertArrayEquals(new int[0], Adapters.adapt(int[].class, ""));
  }

  @Test
  public void testAdaptingEmptyBracketsShouldReturnEmptyArray() {
    assertArrayEquals(new int[0], Adapters.adapt(int[].class, "{}"));
  }

  @Test
  public void testAdaptingEmptyBracketsWithSpacesShouldReturnEmptyArray() {
    assertArrayEquals(new int[0], Adapters.adapt(int[].class, "  {  }  "));
  }

  @Test
  public void testAdaptingOnlySpacesShouldReturnEmptyArray() {
    assertArrayEquals(new int[0], Adapters.adapt(int[].class, "     "));
  }

  @Test
  public void testAdaptingCommaAloneShouldReturnTwoZeroElements() {
    assertArrayEquals(new int[] {0, 0}, Adapters.adapt(int[].class, ","));
  }

  @Test
  public void testAdaptingCommaAloneWithSpacesShouldReturnTwoZeroElements() {
    assertArrayEquals(new int[] {0, 0}, Adapters.adapt(int[].class, "  ,  "));
  }

  @Test
  public void testAdaptingTwoCommasShouldReturnThreeZeroElements() {
    assertArrayEquals(new int[] {0, 0, 0}, Adapters.adapt(int[].class, ",,"));
  }

  @Test
  public void testAdaptingSingleElementShouldReturnSingleElementArray() {
    assertArrayEquals(new int[] {1}, Adapters.adapt(int[].class, "1"));
  }

  @Test
  public void testAdaptingSingleElementWithSpacesShouldReturnSingleElementArray() {
    assertArrayEquals(new int[] {1}, Adapters.adapt(int[].class, "  1  "));
  }

  @Test
  public void testAdaptingSingleElementNegativeShouldReturnSingleElementArray() {
    assertArrayEquals(new int[] {-1}, Adapters.adapt(int[].class, "-1"));
  }

  @Test
  public void testAdaptingSingleElementDecimalShouldRoundDown() {
    assertArrayEquals(new int[] {1}, Adapters.adapt(int[].class, "1.2"));
  }

  @Test
  public void testAdaptingCommaSeparatedElementsShouldReturnArrayOfElements() {
    assertArrayEquals(new int[] {1, 2, 3}, Adapters.adapt(int[].class, "1,2,3"));
  }

  @Test
  public void testAdaptingCommaSeparatedElementsWithSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(new int[] {1, 2, 3}, Adapters.adapt(int[].class, "  1  ,  2  ,  3  "));
  }

  @Test
  public void testAdaptingMissingFirstElementShouldReturnArrayWithZeroFirstElement() {
    assertArrayEquals(new int[] {0, 2, 3}, Adapters.adapt(int[].class, ",2,3"));
  }

  @Test
  public void testAdaptingMissingFirstElementWithSpacesShouldReturnArrayWithZeroFirstElement() {
    assertArrayEquals(new int[] {0, 2, 3}, Adapters.adapt(int[].class, "   ,2,3"));
  }

  @Test
  public void testAdaptingMissingMiddleElementShouldReturnArrayWithZeroMiddleElement() {
    assertArrayEquals(new int[] {1, 0, 3}, Adapters.adapt(int[].class, "1,,3"));
  }

  @Test
  public void testAdaptingMissingMiddleElementWithSpacesShouldReturnArrayWithZeroMiddleElement() {
    assertArrayEquals(new int[] {1, 0, 3}, Adapters.adapt(int[].class, "1,   ,3"));
  }

  @Test
  public void testAdaptingMissingLastElementShouldReturnArrayWithZeroLastElement() {
    assertArrayEquals(new int[] {1, 2, 0}, Adapters.adapt(int[].class, "1,2,"));
  }

  @Test
  public void testAdaptingMissingLastElementWithSpacesShouldReturnArrayWithZeroLastElement() {
    assertArrayEquals(new int[] {1, 2, 0}, Adapters.adapt(int[].class, "1,2,  "));
  }

  @Test
  public void testAdaptingCommaSeparatedWithSquareBracketsShouldReturnArrayOfElements() {
    assertArrayEquals(new int[] {1, 2, 3}, Adapters.adapt(int[].class, "[1,2,3]"));
  }

  @Test
  public void testAdaptingCommaSeparatedWithSquareBracketsAndSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(
        new int[] {1, 2, 3}, Adapters.adapt(int[].class, "    [   1   ,   2   ,   3  ]    "));
  }

  @Test
  public void testAdaptingCommaSeparatedWithRoundBracketsShouldReturnArrayOfElements() {
    assertArrayEquals(new int[] {1, 2, 3}, Adapters.adapt(int[].class, "(1,2,3)"));
  }

  @Test
  public void testAdaptingCommaSeparatedWithRoundBracketsAndSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(
        new int[] {1, 2, 3}, Adapters.adapt(int[].class, "    (   1   ,   2   ,   3   )   "));
  }

  @Test
  public void testAdaptingCommaSeparatedWithCurlyBracketsShouldReturnArrayOfElements() {
    assertArrayEquals(new int[] {1, 2, 3}, Adapters.adapt(int[].class, "{1,2,3}"));
  }

  @Test
  public void testAdaptingCommaSeparatedWithCurlyBracketsAndSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(
        new int[] {1, 2, 3}, Adapters.adapt(int[].class, "    {   1   ,   2   ,   3   }   "));
  }

  @Test
  public void testAdaptingCommaSeparatedElementsInvalidMiddleElementShouldThrowException() {
    assertArrayEquals(new int[] {1, 0, 3}, Adapters.adapt(int[].class, "1,hello,3"));
  }

  @Test
  public void testAdaptingIllegalUseOfBracketsMiddleShouldThrowException() {
    assertArrayEquals(new int[] {1, 0, 0}, Adapters.adapt(int[].class, "1,[2,3]"));
  }

  @Test
  public void testAdaptingIllegalUseOfBracketsNonClosingShouldThrowException() {
    assertArrayEquals(new int[] {0, 2, 3}, Adapters.adapt(int[].class, "[1,2,3"));
  }
}
