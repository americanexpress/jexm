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

public class BooleanArrayAdapterTest {

  @Test
  public void testAdaptingNullStringShouldReturnEmptyArray() {
    Assert.assertArrayEquals(new boolean[0], Adapters.adapt(boolean[].class, null));
  }

  @Test
  public void testAdaptingEmptyStringShouldReturnEmptyArray() {
    assertArrayEquals(new boolean[0], Adapters.adapt(boolean[].class, ""));
  }

  @Test
  public void testAdaptingEmptyBracketsShouldReturnEmptyArray() {
    assertArrayEquals(new boolean[0], Adapters.adapt(boolean[].class, "{}"));
  }

  @Test
  public void testAdaptingEmptyBracketsWithSpacesShouldReturnEmptyArray() {
    assertArrayEquals(new boolean[0], Adapters.adapt(boolean[].class, "  {  }  "));
  }

  @Test
  public void testAdaptingOnlySpacesShouldReturnEmptyArray() {
    assertArrayEquals(new boolean[0], Adapters.adapt(boolean[].class, "     "));
  }

  @Test
  public void testAdaptingCommaAloneShouldReturnTwoFalseElements() {
    assertArrayEquals(new boolean[] {false, false}, Adapters.adapt(boolean[].class, ","));
  }

  @Test
  public void testAdaptingCommaAloneWithSpacesShouldReturnTwoFalseElements() {
    assertArrayEquals(new boolean[] {false, false}, Adapters.adapt(boolean[].class, "  ,  "));
  }

  @Test
  public void testAdaptingTwoCommasShouldReturnThreeZeroElements() {
    assertArrayEquals(new boolean[] {false, false, false}, Adapters.adapt(boolean[].class, ",,"));
  }

  @Test
  public void testAdaptingSingleTrueElementShouldReturnSingleElementArray() {
    assertArrayEquals(new boolean[] {true}, Adapters.adapt(boolean[].class, "true"));
  }

  @Test
  public void testAdaptingSingleFalseElementShouldReturnSingleElementArray() {
    assertArrayEquals(new boolean[] {false}, Adapters.adapt(boolean[].class, "false"));
  }

  @Test
  public void testAdaptingSingleElementWithSpacesShouldReturnSingleElementArray() {
    assertArrayEquals(new boolean[] {true}, Adapters.adapt(boolean[].class, "  true  "));
  }

  @Test
  public void testAdaptingSingleElementOneShouldReturnSingleElementArray() {
    assertArrayEquals(new boolean[] {true}, Adapters.adapt(boolean[].class, "1"));
  }

  @Test
  public void testAdaptingSingleElementUppercaseShouldReturnTrue() {
    assertArrayEquals(new boolean[] {true}, Adapters.adapt(boolean[].class, "TRUE"));
  }

  @Test
  public void testAdaptingSingleElementUppercaseShouldReturnFalse() {
    assertArrayEquals(new boolean[] {false}, Adapters.adapt(boolean[].class, "FALSE"));
  }

  @Test
  public void testAdaptingCommaSeparatedElementsShouldReturnArrayOfElements() {
    assertArrayEquals(
        new boolean[] {true, false, true}, Adapters.adapt(boolean[].class, "true,false,true"));
  }

  @Test
  public void testAdaptingCommaSeparatedElementsWithSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(
        new boolean[] {true, false, true},
        Adapters.adapt(boolean[].class, "  true  ,  false  ,  true  "));
  }

  @Test
  public void testAdaptingMissingFirstElementShouldReturnArrayWithZeroFirstElement() {
    assertArrayEquals(
        new boolean[] {false, false, true}, Adapters.adapt(boolean[].class, ",false,true"));
  }

  @Test
  public void testAdaptingMissingFirstElementWithSpacesShouldReturnArrayWithZeroFirstElement() {
    assertArrayEquals(
        new boolean[] {false, false, true}, Adapters.adapt(boolean[].class, "   ,false,true"));
  }

  @Test
  public void testAdaptingMissingMiddleElementShouldReturnArrayWithZeroMiddleElement() {
    assertArrayEquals(
        new boolean[] {true, false, true}, Adapters.adapt(boolean[].class, "true,,true"));
  }

  @Test
  public void testAdaptingMissingMiddleElementWithSpacesShouldReturnArrayWithZeroMiddleElement() {
    assertArrayEquals(
        new boolean[] {true, false, true}, Adapters.adapt(boolean[].class, "true,   ,true"));
  }

  @Test
  public void testAdaptingMissingLastElementShouldReturnArrayWithZeroLastElement() {
    assertArrayEquals(
        new boolean[] {true, false, false}, Adapters.adapt(boolean[].class, "true,false,"));
  }

  @Test
  public void testAdaptingMissingLastElementWithSpacesShouldReturnArrayWithZeroLastElement() {
    assertArrayEquals(
        new boolean[] {true, false, false}, Adapters.adapt(boolean[].class, "true,false,  "));
  }

  @Test
  public void testAdaptingCommaSeparatedWithSquareBracketsShouldReturnArrayOfElements() {
    assertArrayEquals(
        new boolean[] {true, false, true}, Adapters.adapt(boolean[].class, "[true,false,true]"));
  }

  @Test
  public void testAdaptingCommaSeparatedWithSquareBracketsAndSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(
        new boolean[] {true, false, true},
        Adapters.adapt(boolean[].class, "    [   true   ,   false   ,   true  ]    "));
  }

  @Test
  public void testAdaptingCommaSeparatedWithRoundBracketsShouldReturnArrayOfElements() {
    assertArrayEquals(
        new boolean[] {true, false, true}, Adapters.adapt(boolean[].class, "(true,false,true)"));
  }

  @Test
  public void testAdaptingCommaSeparatedWithRoundBracketsAndSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(
        new boolean[] {true, false, true},
        Adapters.adapt(boolean[].class, "    (   true   ,   false   ,   true   )   "));
  }

  @Test
  public void testAdaptingCommaSeparatedWithCurlyBracketsShouldReturnArrayOfElements() {
    assertArrayEquals(
        new boolean[] {true, false, true}, Adapters.adapt(boolean[].class, "{true,false,true}"));
  }

  @Test
  public void testAdaptingCommaSeparatedWithCurlyBracketsAndSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(
        new boolean[] {true, false, true},
        Adapters.adapt(boolean[].class, "    {   true   ,   false   ,   true   }   "));
  }

  @Test
  public void testAdaptingCommaSeparatedElementsInvalidMiddleElementShouldThrowException() {
    assertArrayEquals(
        new boolean[] {true, false, true}, Adapters.adapt(boolean[].class, "true,hello,true"));
  }

  @Test
  public void testAdaptingIllegalUseOfBracketsMiddleShouldThrowException() {
    assertArrayEquals(
        new boolean[] {true, false, false}, Adapters.adapt(boolean[].class, "true,[false,true]"));
  }

  @Test
  public void testAdaptingIllegalUseOfBracketsNonClosingShouldThrowException() {
    assertArrayEquals(
        new boolean[] {false, false, true}, Adapters.adapt(boolean[].class, "[true,false,true"));
  }
}
