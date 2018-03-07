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

public class DoubleArrayAdapterTest {

  @Test
  public void testAdaptingNullStringShouldReturnEmptyArray() {
    Assert.assertArrayEquals(new double[0], Adapters.adapt(double[].class, null), 0.0001);
  }

  @Test
  public void testAdaptingEmptyStringShouldReturnEmptyArray() {
    assertArrayEquals(new double[0], Adapters.adapt(double[].class, ""), 0.0001);
  }

  @Test
  public void testAdaptingEmptyBracketsShouldReturnEmptyArray() {
    assertArrayEquals(new double[0], Adapters.adapt(double[].class, "{}"), 0.0001);
  }

  @Test
  public void testAdaptingEmptyBracketsWithSpacesShouldReturnEmptyArray() {
    assertArrayEquals(new double[0], Adapters.adapt(double[].class, "  {  }  "), 0.0001);
  }

  @Test
  public void testAdaptingOnlySpacesShouldReturnEmptyArray() {
    assertArrayEquals(new double[0], Adapters.adapt(double[].class, "     "), 0.0001);
  }

  @Test
  public void testAdaptingCommaAloneShouldReturnTwoZeroElements() {
    assertArrayEquals(new double[] {0, 0}, Adapters.adapt(double[].class, ","), 0.0001);
  }

  @Test
  public void testAdaptingCommaAloneWithSpacesShouldReturnTwoZeroElements() {
    assertArrayEquals(new double[] {0, 0}, Adapters.adapt(double[].class, "  ,  "), 0.0001);
  }

  @Test
  public void testAdaptingTwoCommasShouldReturnThreeZeroElements() {
    assertArrayEquals(new double[] {0, 0, 0}, Adapters.adapt(double[].class, ",,"), 0.0001);
  }

  @Test
  public void testAdaptingSingleElementShouldReturnSingleElementArray() {
    assertArrayEquals(new double[] {1}, Adapters.adapt(double[].class, "1"), 0.0001);
  }

  @Test
  public void testAdaptingSingleElementWithSpacesShouldReturnSingleElementArray() {
    assertArrayEquals(new double[] {1}, Adapters.adapt(double[].class, "  1  "), 0.0001);
  }

  @Test
  public void testAdaptingSingleElementNegativeShouldReturnSingleElementArray() {
    assertArrayEquals(new double[] {-1}, Adapters.adapt(double[].class, "-1"), 0.0001);
  }

  @Test
  public void testAdaptingSingleElementDecimalShouldKeepDecimals() {
    assertArrayEquals(new double[] {1.2}, Adapters.adapt(double[].class, "1.2"), 0.0001);
  }

  @Test
  public void testAdaptingCommaSeparatedElementsShouldReturnArrayOfElements() {
    assertArrayEquals(new double[] {1, 2, 3}, Adapters.adapt(double[].class, "1,2,3"), 0.0001);
  }

  @Test
  public void testAdaptingCommaSeparatedElementsWithSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(
        new double[] {1, 2, 3}, Adapters.adapt(double[].class, "  1  ,  2  ,  3  "), 0.0001);
  }

  @Test
  public void testAdaptingMissingFirstElementShouldReturnArrayWithZeroFirstElement() {
    assertArrayEquals(new double[] {0, 2, 3}, Adapters.adapt(double[].class, ",2,3"), 0.0001);
  }

  @Test
  public void testAdaptingMissingFirstElementWithSpacesShouldReturnArrayWithZeroFirstElement() {
    assertArrayEquals(new double[] {0, 2, 3}, Adapters.adapt(double[].class, "   ,2,3"), 0.0001);
  }

  @Test
  public void testAdaptingMissingMiddleElementShouldReturnArrayWithZeroMiddleElement() {
    assertArrayEquals(new double[] {1, 0, 3}, Adapters.adapt(double[].class, "1,,3"), 0.0001);
  }

  @Test
  public void testAdaptingMissingMiddleElementWithSpacesShouldReturnArrayWithZeroMiddleElement() {
    assertArrayEquals(new double[] {1, 0, 3}, Adapters.adapt(double[].class, "1,   ,3"), 0.0001);
  }

  @Test
  public void testAdaptingMissingLastElementShouldReturnArrayWithZeroLastElement() {
    assertArrayEquals(new double[] {1, 2, 0}, Adapters.adapt(double[].class, "1,2,"), 0.0001);
  }

  @Test
  public void testAdaptingMissingLastElementWithSpacesShouldReturnArrayWithZeroLastElement() {
    assertArrayEquals(new double[] {1, 2, 0}, Adapters.adapt(double[].class, "1,2,  "), 0.0001);
  }

  @Test
  public void testAdaptingCommaSeparatedWithSquareBracketsShouldReturnArrayOfElements() {
    assertArrayEquals(new double[] {1, 2, 3}, Adapters.adapt(double[].class, "[1,2,3]"), 0.0001);
  }

  @Test
  public void testAdaptingCommaSeparatedWithSquareBracketsAndSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(
        new double[] {1, 2, 3},
        Adapters.adapt(double[].class, "    [   1   ,   2   ,   3  ]    "),
        0.0001);
  }

  @Test
  public void testAdaptingCommaSeparatedWithRoundBracketsShouldReturnArrayOfElements() {
    assertArrayEquals(new double[] {1, 2, 3}, Adapters.adapt(double[].class, "(1,2,3)"), 0.0001);
  }

  @Test
  public void testAdaptingCommaSeparatedWithRoundBracketsAndSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(
        new double[] {1, 2, 3},
        Adapters.adapt(double[].class, "    (   1   ,   2   ,   3   )   "),
        0.0001);
  }

  @Test
  public void testAdaptingCommaSeparatedWithCurlyBracketsShouldReturnArrayOfElements() {
    assertArrayEquals(new double[] {1, 2, 3}, Adapters.adapt(double[].class, "{1,2,3}"), 0.0001);
  }

  @Test
  public void testAdaptingCommaSeparatedWithCurlyBracketsAndSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(
        new double[] {1, 2, 3},
        Adapters.adapt(double[].class, "    {   1   ,   2   ,   3   }   "),
        0.0001);
  }

  @Test
  public void testAdaptingCommaSeparatedElementsInvalidMiddleElementShouldThrowException() {
    assertArrayEquals(new double[] {1, 0, 3}, Adapters.adapt(double[].class, "1,hello,3"), 0.0001);
  }

  @Test
  public void testAdaptingIllegalUseOfBracketsMiddleShouldThrowException() {
    assertArrayEquals(new double[] {1, 0, 0}, Adapters.adapt(double[].class, "1,[2,3]"), 0.0001);
  }

  @Test
  public void testAdaptingIllegalUseOfBracketsNonClosingShouldThrowException() {
    assertArrayEquals(new double[] {0, 2, 3}, Adapters.adapt(double[].class, "[1,2,3"), 0.0001);
  }
}
