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

package com.americanexpress.jexm.adapter.fields.arrays;

import static org.junit.Assert.assertArrayEquals;

import com.americanexpress.jexm.adapter.Adapters;
import com.americanexpress.jexm.adapter.exceptions.UnsupportedAdapterFieldException;
import org.junit.Assert;
import org.junit.Test;

public class StringArrayAdapterTest {

  @Test
  public void testAdaptingNullStringShouldReturnEmptyArray() {
    Assert.assertArrayEquals(new String[0], Adapters.adapt(String[].class, null));
  }

  @Test
  public void testAdaptingEmptyStringShouldReturnEmptyArray() {
    assertArrayEquals(new String[0], Adapters.adapt(String[].class, ""));
  }

  @Test
  public void testAdaptingEmptyBracketsShouldReturnEmptyArray() {
    assertArrayEquals(new String[0], Adapters.adapt(String[].class, "{}"));
  }

  @Test
  public void testAdaptingEmptyBracketsWithSpacesShouldReturnEmptyArray() {
    assertArrayEquals(new String[0], Adapters.adapt(String[].class, "  {  }  "));
  }

  @Test
  public void testAdaptingOnlySpacesShouldReturnEmptyArray() {
    assertArrayEquals(new String[0], Adapters.adapt(String[].class, "     "));
  }

  @Test
  public void testAdaptingCommaAloneShouldReturnTwoZeroElements() {
    assertArrayEquals(new String[] {null, null}, Adapters.adapt(String[].class, ","));
  }

  @Test
  public void testAdaptingCommaAloneWithSpacesShouldReturnTwoZeroElements() {
    assertArrayEquals(new String[] {null, null}, Adapters.adapt(String[].class, "  ,  "));
  }

  @Test
  public void testAdaptingTwoCommasShouldReturnThreeZeroElements() {
    assertArrayEquals(new String[] {null, null, null}, Adapters.adapt(String[].class, ",,"));
  }

  @Test
  public void testAdaptingSingleElementShouldReturnSingleElementArray() {
    assertArrayEquals(new String[] {"Hello"}, Adapters.adapt(String[].class, "Hello"));
  }

  @Test
  public void testAdaptingAnotherSingleElementeShouldReturnSingleElementArray() {
    assertArrayEquals(new String[] {"World"}, Adapters.adapt(String[].class, "World"));
  }

  @Test(expected = UnsupportedAdapterFieldException.class)
  public void testAdaptingMultiDimensionalArrayShouldThrowException() {
    Adapters.adapt(String[][].class, "Hello,World");
  }

  @Test
  public void testAdaptingSingleElementWithSpacesShouldReturnSingleElementArray() {
    assertArrayEquals(
        new String[] {"Hello World"}, Adapters.adapt(String[].class, "  Hello World  "));
  }

  @Test
  public void testAdaptingSingleElementShouldMatchCase() {
    assertArrayEquals(new String[] {"HELLO"}, Adapters.adapt(String[].class, "HELLO"));
  }

  @Test
  public void testAdaptingCommaSeparatedElementsShouldReturnArrayOfElements() {
    assertArrayEquals(
        new String[] {"Hello", "My", "World"}, Adapters.adapt(String[].class, "Hello,My,World"));
  }

  @Test
  public void testAdaptingCommaSeparatedElementsWithSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(
        new String[] {"Hello", "My", "World"},
        Adapters.adapt(String[].class, "  Hello  ,  My  ,  World  "));
  }

  @Test
  public void testAdaptingMissingFirstElementShouldReturnArrayWithNullFirstElement() {
    assertArrayEquals(
        new String[] {null, "My", "World"}, Adapters.adapt(String[].class, ",My,World"));
  }

  @Test
  public void testAdaptingMissingFirstElementWithSpacesShouldReturnArrayWithNullFirstElement() {
    assertArrayEquals(
        new String[] {null, "My", "World"}, Adapters.adapt(String[].class, "   ,My,World"));
  }

  @Test
  public void testAdaptingMissingMiddleElementShouldReturnArrayWithNullMiddleElement() {
    assertArrayEquals(
        new String[] {"Hello", null, "World"}, Adapters.adapt(String[].class, "Hello,,World"));
  }

  @Test
  public void testAdaptingMissingMiddleElementWithSpacesShouldReturnArrayWithNullMiddleElement() {
    assertArrayEquals(
        new String[] {"Hello", null, "World"}, Adapters.adapt(String[].class, "Hello,   ,World"));
  }

  @Test
  public void testAdaptingMissingLastElementShouldReturnArrayWithNullLastElement() {
    assertArrayEquals(
        new String[] {"Hello", "My", null}, Adapters.adapt(String[].class, "Hello,My,"));
  }

  @Test
  public void testAdaptingMissingLastElementWithSpacesShouldReturnArrayWithNullLastElement() {
    assertArrayEquals(
        new String[] {"Hello", "My", null}, Adapters.adapt(String[].class, "Hello,My,  "));
  }

  @Test
  public void testAdaptingCommaSeparatedWithSquareBracketsShouldReturnArrayOfElements() {
    assertArrayEquals(
        new String[] {"Hello", "My", "World"}, Adapters.adapt(String[].class, "[Hello,My,World]"));
  }

  @Test
  public void testAdaptingCommaSeparatedWithSquareBracketsAndSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(
        new String[] {"Hello", "My", "World"},
        Adapters.adapt(String[].class, "    [   Hello   ,   My   ,   World  ]    "));
  }

  @Test
  public void testAdaptingCommaSeparatedWithRoundBracketsShouldReturnArrayOfElements() {
    assertArrayEquals(
        new String[] {"Hello", "My", "World"}, Adapters.adapt(String[].class, "(Hello,My,World)"));
  }

  @Test
  public void testAdaptingCommaSeparatedWithRoundBracketsAndSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(
        new String[] {"Hello", "My", "World"},
        Adapters.adapt(String[].class, "    (   Hello   ,   My   ,   World   )   "));
  }

  @Test
  public void testAdaptingCommaSeparatedWithCurlyBracketsShouldReturnArrayOfElements() {
    assertArrayEquals(
        new String[] {"Hello", "My", "World"}, Adapters.adapt(String[].class, "{Hello,My,World}"));
  }

  @Test
  public void testAdaptingCommaSeparatedWithCurlyBracketsAndSpacesShouldReturnArrayOfElements() {
    assertArrayEquals(
        new String[] {"Hello", "My", "World"},
        Adapters.adapt(String[].class, "    {   Hello   ,   My   ,   World   }   "));
  }
}
