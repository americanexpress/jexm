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

public class LongArrayAdapterTest {

  @Test
  public void testAdaptingNullStringShouldReturnEmptyArray() {
    Assert.assertArrayEquals(new Long[0], Adapters.adapt(Long[].class, null));
  }

  @Test
  public void testAdaptingEmptyStringShouldReturnEmptyArray() {
    assertArrayEquals(new Long[0], Adapters.adapt(Long[].class, ""));
  }

  @Test
  public void testAdaptingEmptyBracketsShouldReturnEmptyArray() {
    assertArrayEquals(new Long[0], Adapters.adapt(Long[].class, "{}"));
  }

  @Test
  public void testAdaptingEmptyBracketsWithSpacesShouldReturnEmptyArray() {
    assertArrayEquals(new Long[0], Adapters.adapt(Long[].class, "  {  }  "));
  }

  @Test
  public void testAdaptingOnlySpacesShouldReturnEmptyArray() {
    assertArrayEquals(new Long[0], Adapters.adapt(Long[].class, "     "));
  }

  @Test
  public void testAdaptingCommaAloneShouldReturnTwoZeroElements() {
    assertArrayEquals(new Long[] {null, null}, Adapters.adapt(Long[].class, ","));
  }

  @Test
  public void testAdaptingCommaAloneWithSpacesShouldReturnTwoZeroElements() {
    assertArrayEquals(new Long[] {null, null}, Adapters.adapt(Long[].class, "  ,  "));
  }

  @Test
  public void testAdaptingTwoCommasShouldReturnThreeZeroElements() {
    assertArrayEquals(new Long[] {null, null, null}, Adapters.adapt(Long[].class, ",,"));
  }

  @Test
  public void testAdaptingSingleElementShouldReturnSingleElementArray() {
    assertArrayEquals(new Long[] {1L}, Adapters.adapt(Long[].class, "1"));
  }

  @Test
  public void testAdaptingSingleElementWithSpacesShouldReturnSingleElementArray() {
    assertArrayEquals(new Long[] {1L}, Adapters.adapt(Long[].class, "  1  "));
  }

  @Test
  public void testAdaptingSingleElementNegativeShouldReturnSingleElementArray() {
    assertArrayEquals(new Long[] {-1L}, Adapters.adapt(Long[].class, "-1"));
  }

  @Test
  public void testAdaptingSingleElementDecimalShouldRoundDown() {
    assertArrayEquals(new Long[] {1L}, Adapters.adapt(Long[].class, "1.2"));
  }

  @Test
  public void testAdaptingCommaSeparatedElementsShouldReturnArrayOfElements() {
    assertArrayEquals(new Long[] {1L, 2L, 3L}, Adapters.adapt(Long[].class, "1,2,3"));
  }

  @Test
  public void testAdaptingCommaSeparatedElementsWithSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(new Long[] {1L, 2L, 3L}, Adapters.adapt(Long[].class, "  1  ,  2  ,  3  "));
  }

  @Test
  public void testAdaptingMissingFirstElementShouldReturnArrayWithFirstElement() {
    assertArrayEquals(new Long[] {null, 2L, 3L}, Adapters.adapt(Long[].class, ",2,3"));
  }

  @Test
  public void testAdaptingMissingFirstElementWithSpacesShouldReturnArrayWithNullFirstElement() {
    assertArrayEquals(new Long[] {null, 2L, 3L}, Adapters.adapt(Long[].class, "   ,2,3"));
  }

  @Test
  public void testAdaptingMissingMiddleElementShouldReturnArrayWithNullMiddleElement() {
    assertArrayEquals(new Long[] {1L, null, 3L}, Adapters.adapt(Long[].class, "1,,3"));
  }

  @Test
  public void testAdaptingMissingMiddleElementWithSpacesShouldReturnArrayWithNullMiddleElement() {
    assertArrayEquals(new Long[] {1L, null, 3L}, Adapters.adapt(Long[].class, "1,   ,3"));
  }

  @Test
  public void testAdaptingMissingLastElementShouldReturnArrayWithNullLastElement() {
    assertArrayEquals(new Long[] {1L, 2L, null}, Adapters.adapt(Long[].class, "1,2,"));
  }

  @Test
  public void testAdaptingMissingLastElementWithSpacesShouldReturnArrayWithNullLastElement() {
    assertArrayEquals(new Long[] {1L, 2L, null}, Adapters.adapt(Long[].class, "1,2,  "));
  }

  @Test
  public void testAdaptingCommaSeparatedWithSquareBracketsShouldReturnArrayOfElements() {
    assertArrayEquals(new Long[] {1L, 2L, 3L}, Adapters.adapt(Long[].class, "[1,2,3]"));
  }

  @Test
  public void testAdaptingCommaSeparatedWithSquareBracketsAndSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(
        new Long[] {1L, 2L, 3L}, Adapters.adapt(Long[].class, "    [   1   ,   2   ,   3  ]    "));
  }

  @Test
  public void testAdaptingCommaSeparatedWithRoundBracketsShouldReturnArrayOfElements() {
    assertArrayEquals(new Long[] {1L, 2L, 3L}, Adapters.adapt(Long[].class, "(1,2,3)"));
  }

  @Test
  public void testAdaptingCommaSeparatedWithRoundBracketsAndSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(
        new Long[] {1L, 2L, 3L}, Adapters.adapt(Long[].class, "    (   1   ,   2   ,   3   )   "));
  }

  @Test
  public void testAdaptingCommaSeparatedWithCurlyBracketsShouldReturnArrayOfElements() {
    assertArrayEquals(new Long[] {1L, 2L, 3L}, Adapters.adapt(Long[].class, "{1,2,3}"));
  }

  @Test
  public void testAdaptingCommaSeparatedWithCurlyBracketsAndSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(
        new Long[] {1L, 2L, 3L}, Adapters.adapt(Long[].class, "    {   1   ,   2   ,   3   }   "));
  }

  @Test
  public void testAdaptingCommaSeparatedElementsInvalidMiddleElementShouldThrowException() {
    assertArrayEquals(new Long[] {1L, null, 3L}, Adapters.adapt(Long[].class, "1,hello,3"));
  }

  @Test
  public void testAdaptingIllegalUseOfBracketsMiddleShouldThrowException() {
    assertArrayEquals(new Long[] {1L, null, null}, Adapters.adapt(Long[].class, "1,[2,3]"));
  }

  @Test
  public void testAdaptingIllegalUseOfBracketsNonClosingShouldThrowException() {
    assertArrayEquals(new Long[] {null, 2L, 3L}, Adapters.adapt(Long[].class, "[1,2,3"));
  }
}
