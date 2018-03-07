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

package com.americanexpress.jexm.adapter.fields.boxed;

import static org.junit.Assert.*;

import com.americanexpress.jexm.adapter.Adapters;
import com.americanexpress.jexm.adapter.exceptions.ParserException;
import org.junit.Test;

public class BooleanBoxedAdapterTest {

  @Test(expected = ParserException.class)
  public void testAdaptingBooleanInvalid() {
    Adapters.adapt(Boolean.class, "hello");
  }

  @Test
  public void testAdaptingBooleanNull() {
    assertNull(Adapters.adapt(Boolean.class, null));
  }

  @Test
  public void testAdaptingBooleanTRUE() {
    assertTrue(Adapters.adapt(Boolean.class, "TRUE"));
  }

  @Test
  public void testAdaptingBooleanTrueWithSpacesAndTabs() {
    assertTrue(Adapters.adapt(Boolean.class, "          TRUE        "));
  }

  @Test
  public void testAdaptingBooleanTrue() {
    assertTrue(Adapters.adapt(Boolean.class, "True"));
  }

  @Test
  public void testAdaptingBooleantrue() {
    assertTrue(Adapters.adapt(Boolean.class, "true"));
  }

  @Test
  public void testAdaptingBooleanTrueWithT() {
    assertTrue(Adapters.adapt(Boolean.class, "T"));
  }

  @Test
  public void testAdaptingBooleanTrueWitht() {
    assertTrue(Adapters.adapt(Boolean.class, "t"));
  }

  @Test
  public void testAdaptingBooleanTrueWith1() {
    assertTrue(Adapters.adapt(Boolean.class, "1"));
  }

  @Test
  public void testAdaptingBooleanTrueWithYes() {
    assertTrue(Adapters.adapt(Boolean.class, "Yes"));
  }

  @Test
  public void testAdaptingBooleanTrueWithYES() {
    assertTrue(Adapters.adapt(Boolean.class, "YES"));
  }

  @Test
  public void testAdaptingBooleanTrueWithyes() {
    assertTrue(Adapters.adapt(Boolean.class, "yes"));
  }

  @Test
  public void testAdaptingBooleanTrueWithY() {
    assertTrue(Adapters.adapt(Boolean.class, "Y"));
  }

  @Test
  public void testAdaptingBooleanTrueWithy() {
    assertTrue(Adapters.adapt(Boolean.class, "y"));
  }

  @Test
  public void testAdaptingBooleanFALSE() {
    assertFalse(Adapters.adapt(Boolean.class, "FALSE"));
  }

  @Test
  public void testAdaptingBooleanFalseWithSpacesAndTabs() {
    assertFalse(Adapters.adapt(boolean.class, "          FALSE        "));
  }

  @Test
  public void testAdaptingBooleanFalse() {
    assertFalse(Adapters.adapt(Boolean.class, "False"));
  }

  @Test
  public void testAdaptingBooleanfalse() {
    assertFalse(Adapters.adapt(Boolean.class, "false"));
  }

  @Test
  public void testAdaptingBooleanTrueWithF() {
    assertFalse(Adapters.adapt(Boolean.class, "F"));
  }

  @Test
  public void testAdaptingBooleanTrueWithf() {
    assertFalse(Adapters.adapt(Boolean.class, "f"));
  }

  @Test
  public void testAdaptingBooleanTrueWith0() {
    assertFalse(Adapters.adapt(Boolean.class, "0"));
  }

  @Test
  public void testAdaptingBooleanTrueWithNo() {
    assertFalse(Adapters.adapt(Boolean.class, "No"));
  }

  @Test
  public void testAdaptingBooleanTrueWithNO() {
    assertFalse(Adapters.adapt(Boolean.class, "NO"));
  }

  @Test
  public void testAdaptingBooleanTrueWithno() {
    assertFalse(Adapters.adapt(Boolean.class, "no"));
  }

  @Test
  public void testAdaptingBooleanTrueWithN() {
    assertFalse(Adapters.adapt(Boolean.class, "N"));
  }

  @Test
  public void testAdaptingBooleanTrueWithn() {
    assertFalse(Adapters.adapt(Boolean.class, "n"));
  }
}
