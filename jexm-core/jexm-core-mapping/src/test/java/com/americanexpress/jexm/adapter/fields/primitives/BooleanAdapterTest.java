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

package com.americanexpress.jexm.adapter.fields.primitives;

import static org.junit.Assert.*;

import com.americanexpress.jexm.adapter.Adapters;
import com.americanexpress.jexm.adapter.exceptions.ParserException;
import org.junit.Test;

public class BooleanAdapterTest {

  @Test(expected = ParserException.class)
  public void testAdaptingBooleanInvalid() {
    Adapters.adapt(boolean.class, "hello");
  }

  @Test
  public void testAdaptingBooleanNull() {
    assertNull(Adapters.adapt(boolean.class, null));
  }

  @Test
  public void testAdaptingBooleanTRUE() {
    assertTrue(Adapters.adapt(boolean.class, "TRUE"));
  }

  @Test
  public void testAdaptingBooleanTrueWithSpacesAndTabs() {
    assertTrue(Adapters.adapt(boolean.class, "          TRUE        "));
  }

  @Test
  public void testAdaptingBooleanTrue() {
    assertTrue(Adapters.adapt(boolean.class, "True"));
  }

  @Test
  public void testAdaptingBooleantrue() {
    assertTrue(Adapters.adapt(boolean.class, "true"));
  }

  @Test
  public void testAdaptingBooleanTrueWithT() {
    assertTrue(Adapters.adapt(boolean.class, "T"));
  }

  @Test
  public void testAdaptingBooleanTrueWitht() {
    assertTrue(Adapters.adapt(boolean.class, "t"));
  }

  @Test
  public void testAdaptingBooleanTrueWith1() {
    assertTrue(Adapters.adapt(boolean.class, "1"));
  }

  @Test
  public void testAdaptingBooleanTrueWithYes() {
    assertTrue(Adapters.adapt(boolean.class, "Yes"));
  }

  @Test
  public void testAdaptingBooleanTrueWithYES() {
    assertTrue(Adapters.adapt(boolean.class, "YES"));
  }

  @Test
  public void testAdaptingBooleanTrueWithyes() {
    assertTrue(Adapters.adapt(boolean.class, "yes"));
  }

  @Test
  public void testAdaptingBooleanTrueWithY() {
    assertTrue(Adapters.adapt(boolean.class, "Y"));
  }

  @Test
  public void testAdaptingBooleanTrueWithy() {
    assertTrue(Adapters.adapt(boolean.class, "y"));
  }

  @Test
  public void testAdaptingBooleanFALSE() {
    assertFalse(Adapters.adapt(boolean.class, "FALSE"));
  }

  @Test
  public void testAdaptingBooleanFalseWithSpacesAndTabs() {
    assertFalse(Adapters.adapt(boolean.class, "          FALSE        "));
  }

  @Test
  public void testAdaptingBooleanFalse() {
    assertFalse(Adapters.adapt(boolean.class, "False"));
  }

  @Test
  public void testAdaptingBooleanfalse() {
    assertFalse(Adapters.adapt(boolean.class, "false"));
  }

  @Test
  public void testAdaptingBooleanTrueWithF() {
    assertFalse(Adapters.adapt(boolean.class, "F"));
  }

  @Test
  public void testAdaptingBooleanTrueWithf() {
    assertFalse(Adapters.adapt(boolean.class, "f"));
  }

  @Test
  public void testAdaptingBooleanTrueWith0() {
    assertFalse(Adapters.adapt(boolean.class, "0"));
  }

  @Test
  public void testAdaptingBooleanTrueWithNo() {
    assertFalse(Adapters.adapt(boolean.class, "No"));
  }

  @Test
  public void testAdaptingBooleanTrueWithNO() {
    assertFalse(Adapters.adapt(boolean.class, "NO"));
  }

  @Test
  public void testAdaptingBooleanTrueWithno() {
    assertFalse(Adapters.adapt(boolean.class, "no"));
  }

  @Test
  public void testAdaptingBooleanTrueWithN() {
    assertFalse(Adapters.adapt(boolean.class, "N"));
  }

  @Test
  public void testAdaptingBooleanTrueWithn() {
    assertFalse(Adapters.adapt(boolean.class, "n"));
  }
}
