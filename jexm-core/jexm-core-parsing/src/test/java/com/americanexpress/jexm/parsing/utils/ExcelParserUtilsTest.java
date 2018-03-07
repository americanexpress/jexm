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

package com.americanexpress.jexm.parsing.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ExcelParserUtilsTest {

  @Test(expected = NullPointerException.class)
  public void testHeaderIndexOfNullShouldThrowException() {
    ExcelParserUtils.headerIndex(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testHeaderIndexOfEmptyShouldThrowException() {
    ExcelParserUtils.headerIndex("");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testHeaderIndexOfLowerCaseShouldThrowException() {
    ExcelParserUtils.headerIndex("ab");
  }

  @Test
  public void testHeaderIndexOfAShouldReturn0() {
    assertEquals(0, ExcelParserUtils.headerIndex("A"));
  }

  @Test
  public void testHeaderIndexOfA1ShouldReturn0() {
    assertEquals(0, ExcelParserUtils.headerIndex("A1"));
  }

  @Test
  public void testHeaderIndexOfA2ShouldReturn0() {
    assertEquals(0, ExcelParserUtils.headerIndex("A2"));
  }

  @Test
  public void testHeaderIndexOfZShouldReturn25() {
    assertEquals(25, ExcelParserUtils.headerIndex("Z"));
  }

  @Test
  public void testHeaderIndexOfAAShouldReturn26() {
    assertEquals(26, ExcelParserUtils.headerIndex("AA"));
  }

  @Test
  public void testHeaderIndexOfAZShouldReturn51() {
    assertEquals(51, ExcelParserUtils.headerIndex("AZ"));
  }

  @Test
  public void testHeaderIndexOfBAShouldReturn52() {
    assertEquals(52, ExcelParserUtils.headerIndex("BA"));
  }

  @Test
  public void testHeaderIndexOfBZShouldReturn77() {
    assertEquals(77, ExcelParserUtils.headerIndex("BZ"));
  }

  @Test
  public void testHeaderIndexOfCAShouldReturn78() {
    assertEquals(78, ExcelParserUtils.headerIndex("CA"));
  }
}
