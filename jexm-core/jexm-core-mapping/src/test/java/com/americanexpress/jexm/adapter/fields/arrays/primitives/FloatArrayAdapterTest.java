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

public class FloatArrayAdapterTest {

  @Test
  public void testAdaptingNullStringShouldReturnEmptyArray() {
    Assert.assertArrayEquals(new float[0], Adapters.adapt(float[].class, null), 0.0001f);
  }

  @Test
  public void testAdaptingEmptyStringShouldReturnEmptyArray() {
    assertArrayEquals(new float[0], Adapters.adapt(float[].class, ""), 0.0001f);
  }

  @Test
  public void testAdaptingEmptyBracketsShouldReturnEmptyArray() {
    assertArrayEquals(new float[0], Adapters.adapt(float[].class, "{}"), 0.0001f);
  }

  @Test
  public void testAdaptingEmptyBracketsWithSpacesShouldReturnEmptyArray() {
    assertArrayEquals(new float[0], Adapters.adapt(float[].class, "  {  }  "), 0.0001f);
  }

  @Test
  public void testAdaptingOnlySpacesShouldReturnEmptyArray() {
    assertArrayEquals(new float[0], Adapters.adapt(float[].class, "     "), 0.0001f);
  }

  @Test
  public void testAdaptingCommaAloneShouldReturnTwoZeroElements() {
    assertArrayEquals(new float[] {0, 0}, Adapters.adapt(float[].class, ","), 0.0001f);
  }

  @Test
  public void testAdaptingCommaAloneWithSpacesShouldReturnTwoZeroElements() {
    assertArrayEquals(new float[] {0, 0}, Adapters.adapt(float[].class, "  ,  "), 0.0001f);
  }

  @Test
  public void testAdaptingTwoCommasShouldReturnThreeZeroElements() {
    assertArrayEquals(new float[] {0, 0, 0}, Adapters.adapt(float[].class, ",,"), 0.0001f);
  }

  @Test
  public void testAdaptingSingleElementShouldReturnSingleElementArray() {
    assertArrayEquals(new float[] {1}, Adapters.adapt(float[].class, "1"), 0.0001f);
  }

  @Test
  public void testAdaptingSingleElementWithSpacesShouldReturnSingleElementArray() {
    assertArrayEquals(new float[] {1}, Adapters.adapt(float[].class, "  1  "), 0.0001f);
  }

  @Test
  public void testAdaptingSingleElementNegativeShouldReturnSingleElementArray() {
    assertArrayEquals(new float[] {-1}, Adapters.adapt(float[].class, "-1"), 0.0001f);
  }

  @Test
  public void testAdaptingSingleElementDecimalShouldKeepDecimals() {
    assertArrayEquals(new float[] {1.2f}, Adapters.adapt(float[].class, "1.2"), 0.0001f);
  }

  @Test
  public void testAdaptingCommaSeparatedElementsShouldReturnArrayOfElements() {
    assertArrayEquals(new float[] {1, 2, 3}, Adapters.adapt(float[].class, "1,2,3"), 0.0001f);
  }

  @Test
  public void testAdaptingCommaSeparatedElementsWithSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(
        new float[] {1, 2, 3}, Adapters.adapt(float[].class, "  1  ,  2  ,  3  "), 0.0001f);
  }

  @Test
  public void testAdaptingMissingFirstElementShouldReturnArrayWithZeroFirstElement() {
    assertArrayEquals(new float[] {0, 2, 3}, Adapters.adapt(float[].class, ",2,3"), 0.0001f);
  }

  @Test
  public void testAdaptingMissingFirstElementWithSpacesShouldReturnArrayWithZeroFirstElement() {
    assertArrayEquals(new float[] {0, 2, 3}, Adapters.adapt(float[].class, "   ,2,3"), 0.0001f);
  }

  @Test
  public void testAdaptingMissingMiddleElementShouldReturnArrayWithZeroMiddleElement() {
    assertArrayEquals(new float[] {1, 0, 3}, Adapters.adapt(float[].class, "1,,3"), 0.0001f);
  }

  @Test
  public void testAdaptingMissingMiddleElementWithSpacesShouldReturnArrayWithZeroMiddleElement() {
    assertArrayEquals(new float[] {1, 0, 3}, Adapters.adapt(float[].class, "1,   ,3"), 0.0001f);
  }

  @Test
  public void testAdaptingMissingLastElementShouldReturnArrayWithZeroLastElement() {
    assertArrayEquals(new float[] {1, 2, 0}, Adapters.adapt(float[].class, "1,2,"), 0.0001f);
  }

  @Test
  public void testAdaptingMissingLastElementWithSpacesShouldReturnArrayWithZeroLastElement() {
    assertArrayEquals(new float[] {1, 2, 0}, Adapters.adapt(float[].class, "1,2,  "), 0.0001f);
  }

  @Test
  public void testAdaptingCommaSeparatedWithSquareBracketsShouldReturnArrayOfElements() {
    assertArrayEquals(new float[] {1, 2, 3}, Adapters.adapt(float[].class, "[1,2,3]"), 0.0001f);
  }

  @Test
  public void testAdaptingCommaSeparatedWithSquareBracketsAndSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(
        new float[] {1, 2, 3},
        Adapters.adapt(float[].class, "    [   1   ,   2   ,   3  ]    "),
        0.0001f);
  }

  @Test
  public void testAdaptingCommaSeparatedWithRoundBracketsShouldReturnArrayOfElements() {
    assertArrayEquals(new float[] {1, 2, 3}, Adapters.adapt(float[].class, "(1,2,3)"), 0.0001f);
  }

  @Test
  public void testAdaptingCommaSeparatedWithRoundBracketsAndSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(
        new float[] {1, 2, 3},
        Adapters.adapt(float[].class, "    (   1   ,   2   ,   3   )   "),
        0.0001f);
  }

  @Test
  public void testAdaptingCommaSeparatedWithCurlyBracketsShouldReturnArrayOfElements() {
    assertArrayEquals(new float[] {1, 2, 3}, Adapters.adapt(float[].class, "{1,2,3}"), 0.0001f);
  }

  @Test
  public void testAdaptingCommaSeparatedWithCurlyBracketsAndSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(
        new float[] {1, 2, 3},
        Adapters.adapt(float[].class, "    {   1   ,   2   ,   3   }   "),
        0.0001f);
  }

  @Test
  public void testAdaptingCommaSeparatedElementsInvalidMiddleElementShouldThrowException() {
    assertArrayEquals(new float[] {1, 0, 3}, Adapters.adapt(float[].class, "1,hello,3"), 0.0001f);
  }

  @Test
  public void testAdaptingIllegalUseOfBracketsMiddleShouldThrowException() {
    assertArrayEquals(new float[] {1, 0, 0}, Adapters.adapt(float[].class, "1,[2,3]"), 0.0001f);
  }

  @Test
  public void testAdaptingIllegalUseOfBracketsNonClosingShouldThrowException() {
    assertArrayEquals(new float[] {0, 2, 3}, Adapters.adapt(float[].class, "[1,2,3"), 0.0001f);
  }
}
