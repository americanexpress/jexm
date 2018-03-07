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

public class CharacterArrayAdapterTest {

  @Test
  public void testAdaptingNullStringShouldReturnEmptyArray() {
    Assert.assertArrayEquals(new Character[0], Adapters.adapt(Character[].class, null));
  }

  @Test
  public void testAdaptingEmptyStringShouldReturnEmptyArray() {
    assertArrayEquals(new Character[0], Adapters.adapt(Character[].class, ""));
  }

  @Test
  public void testAdaptingEmptyBracketsShouldReturnEmptyArray() {
    assertArrayEquals(new Character[0], Adapters.adapt(Character[].class, "{}"));
  }

  @Test
  public void testAdaptingEmptyBracketsWithSpacesShouldReturnEmptyArray() {
    assertArrayEquals(new Character[0], Adapters.adapt(Character[].class, "  {  }  "));
  }

  @Test
  public void testAdaptingOnlySpacesShouldReturnEmptyArray() {
    assertArrayEquals(new Character[0], Adapters.adapt(Character[].class, "     "));
  }

  @Test
  public void testAdaptingCommaAloneShouldReturnTwoZeroElements() {
    assertArrayEquals(new Character[] {null, null}, Adapters.adapt(Character[].class, ","));
  }

  @Test
  public void testAdaptingCommaAloneWithSpacesShouldReturnTwoZeroElements() {
    assertArrayEquals(new Character[] {null, null}, Adapters.adapt(Character[].class, "  ,  "));
  }

  @Test
  public void testAdaptingTwoCommasShouldReturnThreeZeroElements() {
    assertArrayEquals(new Character[] {null, null, null}, Adapters.adapt(Character[].class, ",,"));
  }

  @Test
  public void testAdaptingSingleElementShouldReturnSingleElementArray() {
    assertArrayEquals(new Character[] {'A'}, Adapters.adapt(Character[].class, "A"));
  }

  @Test
  public void testAdaptingSingleElementWithSpacesShouldReturnSingleElementArray() {
    assertArrayEquals(new Character[] {'A'}, Adapters.adapt(Character[].class, "  A  "));
  }

  @Test
  public void testAdaptingSingleElementNegativeShouldReturnSingleElementArray() {
    assertArrayEquals(new Character[] {'a'}, Adapters.adapt(Character[].class, "a"));
  }

  @Test
  public void testAdaptingSingleElementDecimalShouldRoundDown() {
    assertArrayEquals(new Character[] {'b'}, Adapters.adapt(Character[].class, "b"));
  }

  @Test
  public void testAdaptingCommaSeparatedElementsShouldReturnArrayOfElements() {
    assertArrayEquals(new Character[] {'A', 'B', 'C'}, Adapters.adapt(Character[].class, "A,B,C"));
  }

  @Test
  public void testAdaptingCommaSeparatedElementsWithSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(
        new Character[] {'A', 'B', 'C'}, Adapters.adapt(Character[].class, "  A  ,  B  ,  C  "));
  }

  @Test
  public void testAdaptingMissingFirstElementShouldReturnArrayWithNullFirstElement() {
    assertArrayEquals(new Character[] {null, 'B', 'C'}, Adapters.adapt(Character[].class, ",B,C"));
  }

  @Test
  public void testAdaptingMissingFirstElementWithSpacesShouldReturnArrayWithNullFirstElement() {
    assertArrayEquals(
        new Character[] {null, 'B', 'C'}, Adapters.adapt(Character[].class, "   ,B,C"));
  }

  @Test
  public void testAdaptingMissingMiddleElementShouldReturnArrayWithNullMiddleElement() {
    assertArrayEquals(new Character[] {'A', null, 'C'}, Adapters.adapt(Character[].class, "A,,C"));
  }

  @Test
  public void testAdaptingMissingMiddleElementWithSpacesShouldReturnArrayWithNullMiddleElement() {
    assertArrayEquals(
        new Character[] {'A', null, 'C'}, Adapters.adapt(Character[].class, "A,   ,C"));
  }

  @Test
  public void testAdaptingMissingLastElementShouldReturnArrayWithNullLastElement() {
    assertArrayEquals(new Character[] {'A', 'B', null}, Adapters.adapt(Character[].class, "A,B,"));
  }

  @Test
  public void testAdaptingMissingLastElementWithSpacesShouldReturnArrayWithNullLastElement() {
    assertArrayEquals(
        new Character[] {'A', 'B', null}, Adapters.adapt(Character[].class, "A,B,  "));
  }

  @Test
  public void testAdaptingCommaSeparatedWithSquareBracketsShouldReturnArrayOfElements() {
    assertArrayEquals(
        new Character[] {'A', 'B', 'C'}, Adapters.adapt(Character[].class, "[A,B,C]"));
  }

  @Test
  public void testAdaptingCommaSeparatedWithSquareBracketsAndSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(
        new Character[] {'A', 'B', 'C'},
        Adapters.adapt(Character[].class, "    [   A   ,   B   ,   C  ]    "));
  }

  @Test
  public void testAdaptingCommaSeparatedWithRoundBracketsShouldReturnArrayOfElements() {
    assertArrayEquals(
        new Character[] {'A', 'B', 'C'}, Adapters.adapt(Character[].class, "(A,B,C)"));
  }

  @Test
  public void testAdaptingCommaSeparatedWithRoundBracketsAndSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(
        new Character[] {'A', 'B', 'C'},
        Adapters.adapt(Character[].class, "    (   A   ,   B   ,   C   )   "));
  }

  @Test
  public void testAdaptingCommaSeparatedWithCurlyBracketsShouldReturnArrayOfElements() {
    assertArrayEquals(
        new Character[] {'A', 'B', 'C'}, Adapters.adapt(Character[].class, "{A,B,C}"));
  }

  @Test
  public void testAdaptingCommaSeparatedWithCurlyBracketsAndSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(
        new Character[] {'A', 'B', 'C'},
        Adapters.adapt(Character[].class, "    {   A   ,   B   ,   C   }   "));
  }

  @Test
  public void testAdaptingCommaSeparatedElementsWithWordInMiddleShouldReturnFirstCharacter() {
    assertArrayEquals(
        new Character[] {'A', 'h', 'C'}, Adapters.adapt(Character[].class, "A,hello,C"));
  }

  @Test
  public void testAdaptingIllegalUseOfBracketsMiddleShouldReturnBracketButLogWarning() {
    assertArrayEquals(
        new Character[] {'A', '[', 'C'}, Adapters.adapt(Character[].class, "A,[B,C]"));
  }

  @Test
  public void testAdaptingIllegalUseOfBracketsNonClosingShouldReturnBracketButLogWarning() {
    assertArrayEquals(new Character[] {'[', 'B', 'C'}, Adapters.adapt(Character[].class, "[A,B,C"));
  }
}
