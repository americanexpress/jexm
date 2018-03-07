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
import org.junit.Test;

public class BooleanArrayAdapterTest {

  @Test
  public void testAdaptingNullStringShouldReturnEmptyArray() {
    assertArrayEquals(new Boolean[0], Adapters.adapt(Boolean[].class, null));
  }

  @Test
  public void testAdaptingEmptyStringShouldReturnEmptyArray() {
    assertArrayEquals(new Boolean[0], Adapters.adapt(Boolean[].class, ""));
  }

  @Test
  public void testAdaptingEmptyBracketsShouldReturnEmptyArray() {
    assertArrayEquals(new Boolean[0], Adapters.adapt(Boolean[].class, "{}"));
  }

  @Test
  public void testAdaptingEmptyBracketsWithSpacesShouldReturnEmptyArray() {
    assertArrayEquals(new Boolean[0], Adapters.adapt(Boolean[].class, "  {  }  "));
  }

  @Test
  public void testAdaptingOnlySpacesShouldReturnEmptyArray() {
    assertArrayEquals(new Boolean[0], Adapters.adapt(Boolean[].class, "     "));
  }

  @Test
  public void testAdaptingCommaAloneShouldReturnTwoNullElements() {
    assertArrayEquals(new Boolean[] {null, null}, Adapters.adapt(Boolean[].class, ","));
  }

  @Test
  public void testAdaptingCommaAloneWithSpacesShouldReturnTwoNullElements() {
    assertArrayEquals(new Boolean[] {null, null}, Adapters.adapt(Boolean[].class, "  ,  "));
  }

  @Test
  public void testAdaptingTwoCommasShouldReturnThreeNullElements() {
    assertArrayEquals(new Boolean[] {null, null, null}, Adapters.adapt(Boolean[].class, ",,"));
  }

  @Test
  public void testAdaptingSingleTrueElementShouldReturnSingleElementArray() {
    assertArrayEquals(new Boolean[] {true}, Adapters.adapt(Boolean[].class, "true"));
  }

  @Test
  public void testAdaptingSingleFalseElementShouldReturnSingleElementArray() {
    assertArrayEquals(new Boolean[] {false}, Adapters.adapt(Boolean[].class, "false"));
  }

  @Test
  public void testAdaptingSingleElementWithSpacesShouldReturnSingleElementArray() {
    assertArrayEquals(new Boolean[] {true}, Adapters.adapt(Boolean[].class, "  true  "));
  }

  @Test
  public void testAdaptingSingleElementOneShouldReturnSingleElementArray() {
    assertArrayEquals(new Boolean[] {true}, Adapters.adapt(Boolean[].class, "1"));
  }

  @Test
  public void testAdaptingSingleElementUppercaseShouldReturnTrue() {
    assertArrayEquals(new Boolean[] {true}, Adapters.adapt(Boolean[].class, "TRUE"));
  }

  @Test
  public void testAdaptingSingleElementUppercaseShouldReturnFalse() {
    assertArrayEquals(new Boolean[] {false}, Adapters.adapt(Boolean[].class, "FALSE"));
  }

  @Test
  public void testAdaptingCommaSeparatedElementsShouldReturnArrayOfElements() {
    assertArrayEquals(
        new Boolean[] {true, false, true}, Adapters.adapt(Boolean[].class, "true,false,true"));
  }

  @Test
  public void testAdaptingCommaSeparatedElementsWithSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(
        new Boolean[] {true, false, true},
        Adapters.adapt(Boolean[].class, "  true  ,  false  ,  true  "));
  }

  @Test
  public void testAdaptingMissingFirstElementShouldReturnArrayWithNullFirstElement() {
    assertArrayEquals(
        new Boolean[] {null, false, true}, Adapters.adapt(Boolean[].class, ",false,true"));
  }

  @Test
  public void testAdaptingMissingFirstElementWithSpacesShouldReturnArrayWithNullFirstElement() {
    assertArrayEquals(
        new Boolean[] {null, false, true}, Adapters.adapt(Boolean[].class, "   ,false,true"));
  }

  @Test
  public void testAdaptingMissingMiddleElementShouldReturnArrayWithNullMiddleElement() {
    assertArrayEquals(
        new Boolean[] {true, null, true}, Adapters.adapt(Boolean[].class, "true,,true"));
  }

  @Test
  public void testAdaptingMissingMiddleElementWithSpacesShouldReturnArrayWithNullMiddleElement() {
    assertArrayEquals(
        new Boolean[] {true, null, true}, Adapters.adapt(Boolean[].class, "true,   ,true"));
  }

  @Test
  public void testAdaptingMissingLastElementShouldReturnArrayWithNullLastElement() {
    assertArrayEquals(
        new Boolean[] {true, false, null}, Adapters.adapt(Boolean[].class, "true,false,"));
  }

  @Test
  public void testAdaptingMissingLastElementWithSpacesShouldReturnArrayWithNullLastElement() {
    assertArrayEquals(
        new Boolean[] {true, false, null}, Adapters.adapt(Boolean[].class, "true,false,  "));
  }

  @Test
  public void testAdaptingCommaSeparatedWithSquareBracketsShouldReturnArrayOfElements() {
    assertArrayEquals(
        new Boolean[] {true, false, true}, Adapters.adapt(Boolean[].class, "[true,false,true]"));
  }

  @Test
  public void testAdaptingCommaSeparatedWithSquareBracketsAndSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(
        new Boolean[] {true, false, true},
        Adapters.adapt(Boolean[].class, "    [   true   ,   false   ,   true  ]    "));
  }

  @Test
  public void testAdaptingCommaSeparatedWithRoundBracketsShouldReturnArrayOfElements() {
    assertArrayEquals(
        new Boolean[] {true, false, true}, Adapters.adapt(Boolean[].class, "(true,false,true)"));
  }

  @Test
  public void testAdaptingCommaSeparatedWithRoundBracketsAndSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(
        new Boolean[] {true, false, true},
        Adapters.adapt(Boolean[].class, "    (   true   ,   false   ,   true   )   "));
  }

  @Test
  public void testAdaptingCommaSeparatedWithCurlyBracketsShouldReturnArrayOfElements() {
    assertArrayEquals(
        new Boolean[] {true, false, true}, Adapters.adapt(Boolean[].class, "{true,false,true}"));
  }

  @Test
  public void testAdaptingCommaSeparatedWithCurlyBracketsAndSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(
        new Boolean[] {true, false, true},
        Adapters.adapt(Boolean[].class, "    {   true   ,   false   ,   true   }   "));
  }

  @Test
  public void testAdaptingCommaSeparatedElementsInvalidMiddleElementShouldThrowException() {
    assertArrayEquals(
        new Boolean[] {true, null, true}, Adapters.adapt(Boolean[].class, "true,hello,true"));
  }

  @Test
  public void testAdaptingIllegalUseOfBracketsMiddleShouldThrowException() {
    assertArrayEquals(
        new Boolean[] {true, null, null}, Adapters.adapt(Boolean[].class, "true,[false,true]"));
  }

  @Test
  public void testAdaptingIllegalUseOfBracketsNonClosingShouldThrowException() {
    assertArrayEquals(
        new Boolean[] {null, false, true}, Adapters.adapt(Boolean[].class, "[true,false,true"));
  }
}
