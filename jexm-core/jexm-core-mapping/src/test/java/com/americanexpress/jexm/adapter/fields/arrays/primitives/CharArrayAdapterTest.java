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

public class CharArrayAdapterTest {

  @Test
  public void testAdaptingNullStringShouldReturnEmptyArray() {
    Assert.assertArrayEquals(new char[0], Adapters.adapt(char[].class, null));
  }

  @Test
  public void testAdaptingEmptyStringShouldReturnEmptyArray() {
    assertArrayEquals(new char[0], Adapters.adapt(char[].class, ""));
  }

  @Test
  public void testAdaptingEmptyBracketsShouldReturnEmptyArray() {
    assertArrayEquals(new char[0], Adapters.adapt(char[].class, "{}"));
  }

  @Test
  public void testAdaptingEmptyBracketsWithSpacesShouldReturnEmptyArray() {
    assertArrayEquals(new char[0], Adapters.adapt(char[].class, "  {  }  "));
  }

  @Test
  public void testAdaptingOnlySpacesShouldReturnEmptyArray() {
    assertArrayEquals(new char[0], Adapters.adapt(char[].class, "     "));
  }

  @Test
  public void testAdaptingCommaAloneShouldReturnTwoZeroElements() {
    assertArrayEquals(new char[] {'\0', '\0'}, Adapters.adapt(char[].class, ","));
  }

  @Test
  public void testAdaptingCommaAloneWithSpacesShouldReturnTwoZeroElements() {
    assertArrayEquals(new char[] {'\0', '\0'}, Adapters.adapt(char[].class, "  ,  "));
  }

  @Test
  public void testAdaptingTwoCommasShouldReturnThreeZeroElements() {
    assertArrayEquals(new char[] {'\0', '\0', '\0'}, Adapters.adapt(char[].class, ",,"));
  }

  @Test
  public void testAdaptingSingleElementShouldReturnSingleElementArray() {
    assertArrayEquals(new char[] {'A'}, Adapters.adapt(char[].class, "A"));
  }

  @Test
  public void testAdaptingSingleElementWithSpacesShouldReturnSingleElementArray() {
    assertArrayEquals(new char[] {'A'}, Adapters.adapt(char[].class, "  A  "));
  }

  @Test
  public void testAdaptingSingleElementNegativeShouldReturnSingleElementArray() {
    assertArrayEquals(new char[] {'a'}, Adapters.adapt(char[].class, "a"));
  }

  @Test
  public void testAdaptingSingleElementDecimalShouldRoundDown() {
    assertArrayEquals(new char[] {'b'}, Adapters.adapt(char[].class, "b"));
  }

  @Test
  public void testAdaptingCommaSeparatedElementsShouldReturnArrayOfElements() {
    assertArrayEquals(new char[] {'A', 'B', 'C'}, Adapters.adapt(char[].class, "A,B,C"));
  }

  @Test
  public void testAdaptingCommaSeparatedElementsWithSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(
        new char[] {'A', 'B', 'C'}, Adapters.adapt(char[].class, "  A  ,  B  ,  C  "));
  }

  @Test
  public void testAdaptingMissingFirstElementShouldReturnArrayWithZeroFirstElement() {
    assertArrayEquals(new char[] {0, 'B', 'C'}, Adapters.adapt(char[].class, ",B,C"));
  }

  @Test
  public void testAdaptingMissingFirstElementWithSpacesShouldReturnArrayWithZeroFirstElement() {
    assertArrayEquals(new char[] {'\0', 'B', 'C'}, Adapters.adapt(char[].class, "   ,B,C"));
  }

  @Test
  public void testAdaptingMissingMiddleElementShouldReturnArrayWithZeroMiddleElement() {
    assertArrayEquals(new char[] {'A', '\0', 'C'}, Adapters.adapt(char[].class, "A,,C"));
  }

  @Test
  public void testAdaptingMissingMiddleElementWithSpacesShouldReturnArrayWithZeroMiddleElement() {
    assertArrayEquals(new char[] {'A', '\0', 'C'}, Adapters.adapt(char[].class, "A,   ,C"));
  }

  @Test
  public void testAdaptingMissingLastElementShouldReturnArrayWithZeroLastElement() {
    assertArrayEquals(new char[] {'A', 'B', '\0'}, Adapters.adapt(char[].class, "A,B,"));
  }

  @Test
  public void testAdaptingMissingLastElementWithSpacesShouldReturnArrayWithZeroLastElement() {
    assertArrayEquals(new char[] {'A', 'B', '\0'}, Adapters.adapt(char[].class, "A,B,  "));
  }

  @Test
  public void testAdaptingCommaSeparatedWithSquareBracketsShouldReturnArrayOfElements() {
    assertArrayEquals(new char[] {'A', 'B', 'C'}, Adapters.adapt(char[].class, "[A,B,C]"));
  }

  @Test
  public void testAdaptingCommaSeparatedWithSquareBracketsAndSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(
        new char[] {'A', 'B', 'C'},
        Adapters.adapt(char[].class, "    [   A   ,   B   ,   C  ]    "));
  }

  @Test
  public void testAdaptingCommaSeparatedWithRoundBracketsShouldReturnArrayOfElements() {
    assertArrayEquals(new char[] {'A', 'B', 'C'}, Adapters.adapt(char[].class, "(A,B,C)"));
  }

  @Test
  public void testAdaptingCommaSeparatedWithRoundBracketsAndSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(
        new char[] {'A', 'B', 'C'},
        Adapters.adapt(char[].class, "    (   A   ,   B   ,   C   )   "));
  }

  @Test
  public void testAdaptingCommaSeparatedWithCurlyBracketsShouldReturnArrayOfElements() {
    assertArrayEquals(new char[] {'A', 'B', 'C'}, Adapters.adapt(char[].class, "{A,B,C}"));
  }

  @Test
  public void testAdaptingCommaSeparatedWithCurlyBracketsAndSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(
        new char[] {'A', 'B', 'C'},
        Adapters.adapt(char[].class, "    {   A   ,   B   ,   C   }   "));
  }

  @Test
  public void testAdaptingCommaSeparatedElementsWithWordInMiddleShouldReturnFirstChar() {
    assertArrayEquals(new char[] {'A', 'h', 'C'}, Adapters.adapt(char[].class, "A,hello,C"));
  }

  @Test
  public void testAdaptingIllegalUseOfBracketsMiddleShouldReturnBracketButLogWarning() {
    assertArrayEquals(new char[] {'A', '[', 'C'}, Adapters.adapt(char[].class, "A,[B,C]"));
  }

  @Test
  public void testAdaptingIllegalUseOfBracketsNonClosingShouldReturnBracketButLogWarning() {
    assertArrayEquals(new char[] {'[', 'B', 'C'}, Adapters.adapt(char[].class, "[A,B,C"));
  }
}
