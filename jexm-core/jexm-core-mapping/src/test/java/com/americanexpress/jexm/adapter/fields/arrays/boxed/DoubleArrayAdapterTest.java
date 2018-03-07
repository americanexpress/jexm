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

public class DoubleArrayAdapterTest {

  @Test
  public void testAdaptingNullStringShouldReturnEmptyArray() {
    Assert.assertArrayEquals(new Double[0], Adapters.adapt(Double[].class, null));
  }

  @Test
  public void testAdaptingEmptyStringShouldReturnEmptyArray() {
    assertArrayEquals(new Double[0], Adapters.adapt(Double[].class, ""));
  }

  @Test
  public void testAdaptingEmptyBracketsShouldReturnEmptyArray() {
    assertArrayEquals(new Double[0], Adapters.adapt(Double[].class, "{}"));
  }

  @Test
  public void testAdaptingEmptyBracketsWithSpacesShouldReturnEmptyArray() {
    assertArrayEquals(new Double[0], Adapters.adapt(Double[].class, "  {  }  "));
  }

  @Test
  public void testAdaptingOnlySpacesShouldReturnEmptyArray() {
    assertArrayEquals(new Double[0], Adapters.adapt(Double[].class, "     "));
  }

  @Test
  public void testAdaptingCommaAloneShouldReturnTwoZeroElements() {
    assertArrayEquals(new Double[] {null, null}, Adapters.adapt(Double[].class, ","));
  }

  @Test
  public void testAdaptingCommaAloneWithSpacesShouldReturnTwoZeroElements() {
    assertArrayEquals(new Double[] {null, null}, Adapters.adapt(Double[].class, "  ,  "));
  }

  @Test
  public void testAdaptingTwoCommasShouldReturnThreeZeroElements() {
    assertArrayEquals(new Double[] {null, null, null}, Adapters.adapt(Double[].class, ",,"));
  }

  @Test
  public void testAdaptingSingleElementShouldReturnSingleElementArray() {
    assertArrayEquals(new Double[] {1.0}, Adapters.adapt(Double[].class, "1"));
  }

  @Test
  public void testAdaptingSingleElementWithSpacesShouldReturnSingleElementArray() {
    assertArrayEquals(new Double[] {1.0}, Adapters.adapt(Double[].class, "  1  "));
  }

  @Test
  public void testAdaptingSingleElementNegativeShouldReturnSingleElementArray() {
    assertArrayEquals(new Double[] {-1.0}, Adapters.adapt(Double[].class, "-1"));
  }

  @Test
  public void testAdaptingSingleElementDecimalShouldKeepDecimals() {
    assertArrayEquals(new Double[] {1.2}, Adapters.adapt(Double[].class, "1.2"));
  }

  @Test
  public void testAdaptingCommaSeparatedElementsShouldReturnArrayOfElements() {
    assertArrayEquals(new Double[] {1.0, 2.0, 3.0}, Adapters.adapt(Double[].class, "1,2,3"));
  }

  @Test
  public void testAdaptingCommaSeparatedElementsWithSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(
        new Double[] {1.0, 2.0, 3.0}, Adapters.adapt(Double[].class, "  1  ,  2  ,  3  "));
  }

  @Test
  public void testAdaptingMissingFirstElementShouldReturnArrayWithNullFirstElement() {
    assertArrayEquals(new Double[] {null, 2.0, 3.0}, Adapters.adapt(Double[].class, ",2,3"));
  }

  @Test
  public void testAdaptingMissingFirstElementWithSpacesShouldReturnArrayWithNullFirstElement() {
    assertArrayEquals(new Double[] {null, 2.0, 3.0}, Adapters.adapt(Double[].class, "   ,2,3"));
  }

  @Test
  public void testAdaptingMissingMiddleElementShouldReturnArrayWithNullMiddleElement() {
    assertArrayEquals(new Double[] {1.0, null, 3.0}, Adapters.adapt(Double[].class, "1,,3"));
  }

  @Test
  public void testAdaptingMissingMiddleElementWithSpacesShouldReturnArrayWithNullMiddleElement() {
    assertArrayEquals(new Double[] {1.0, null, 3.0}, Adapters.adapt(Double[].class, "1,   ,3"));
  }

  @Test
  public void testAdaptingMissingLastElementShouldReturnArrayWithNullLastElement() {
    assertArrayEquals(new Double[] {1.0, 2.0, null}, Adapters.adapt(Double[].class, "1,2,"));
  }

  @Test
  public void testAdaptingMissingLastElementWithSpacesShouldReturnArrayWithNullLastElement() {
    assertArrayEquals(new Double[] {1.0, 2.0, null}, Adapters.adapt(Double[].class, "1,2,  "));
  }

  @Test
  public void testAdaptingCommaSeparatedWithSquareBracketsShouldReturnArrayOfElements() {
    assertArrayEquals(new Double[] {1.0, 2.0, 3.0}, Adapters.adapt(Double[].class, "[1,2,3]"));
  }

  @Test
  public void testAdaptingCommaSeparatedWithSquareBracketsAndSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(
        new Double[] {1.0, 2.0, 3.0},
        Adapters.adapt(Double[].class, "    [   1   ,   2   ,   3  ]    "));
  }

  @Test
  public void testAdaptingCommaSeparatedWithRoundBracketsShouldReturnArrayOfElements() {
    assertArrayEquals(new Double[] {1.0, 2.0, 3.0}, Adapters.adapt(Double[].class, "(1,2,3)"));
  }

  @Test
  public void testAdaptingCommaSeparatedWithRoundBracketsAndSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(
        new Double[] {1.0, 2.0, 3.0},
        Adapters.adapt(Double[].class, "    (   1   ,   2   ,   3   )   "));
  }

  @Test
  public void testAdaptingCommaSeparatedWithCurlyBracketsShouldReturnArrayOfElements() {
    assertArrayEquals(new Double[] {1.0, 2.0, 3.0}, Adapters.adapt(Double[].class, "{1,2,3}"));
  }

  @Test
  public void testAdaptingCommaSeparatedWithCurlyBracketsAndSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(
        new Double[] {1.0, 2.0, 3.0},
        Adapters.adapt(Double[].class, "    {   1   ,   2   ,   3   }   "));
  }

  @Test
  public void testAdaptingCommaSeparatedElementsInvalidMiddleElementShouldThrowException() {
    assertArrayEquals(new Double[] {1.0, null, 3.0}, Adapters.adapt(Double[].class, "1,hello,3"));
  }

  @Test
  public void testAdaptingIllegalUseOfBracketsMiddleShouldThrowException() {
    assertArrayEquals(new Double[] {1.0, null, null}, Adapters.adapt(Double[].class, "1,[2,3]"));
  }

  @Test
  public void testAdaptingIllegalUseOfBracketsNonClosingShouldThrowException() {
    assertArrayEquals(new Double[] {null, 2.0, 3.0}, Adapters.adapt(Double[].class, "[1,2,3"));
  }
}
