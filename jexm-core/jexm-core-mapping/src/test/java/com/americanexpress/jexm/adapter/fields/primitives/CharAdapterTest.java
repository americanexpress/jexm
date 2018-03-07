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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.americanexpress.jexm.adapter.Adapters;
import org.junit.Test;

public class CharAdapterTest {

  @Test
  public void testAdaptingCharFromSingleCharacterStringA() {
    assertEquals('A', (char) Adapters.adapt(char.class, "A"));
  }

  @Test
  public void testAdaptingCharFromSingleCharacterStringB() {
    assertEquals('B', (char) Adapters.adapt(char.class, "B"));
  }

  @Test
  public void testAdaptingCharFromSingleCharacterStringWithSpacesAndTabs() {
    assertEquals('A', (char) Adapters.adapt(char.class, "        A       "));
  }

  @Test
  public void testAdaptingCharFromSimpleString() {
    assertEquals('H', (char) Adapters.adapt(char.class, "Hello World"));
  }

  @Test
  public void testAdaptingCharFromSimpleStringWithSpacesAndTabs() {
    assertEquals('H', (char) Adapters.adapt(char.class, "        Hello World     "));
  }

  @Test
  public void testAdaptingCharFromAllSpacesString() {
    assertNull(Adapters.adapt(char.class, "        "));
  }

  @Test
  public void testAdaptingCharNull() {
    assertNull(Adapters.adapt(int.class, null));
  }
}
