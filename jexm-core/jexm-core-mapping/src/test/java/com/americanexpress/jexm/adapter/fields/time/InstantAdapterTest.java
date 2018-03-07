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

package com.americanexpress.jexm.adapter.fields.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.americanexpress.jexm.adapter.Adapters;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import org.junit.Test;

public class InstantAdapterTest {

  @Test
  public void testAdaptingInstantNull() {
    assertNull(Adapters.adapt(Instant.class, null));
  }

  @Test
  public void testAdaptingInstantWithTime() {
    assertEquals(
        Instant.parse("2000-01-01T12:00:00Z"),
        Adapters.adapt(Instant.class, "2000-01-01T12:00:00Z"));
  }

  @Test(expected = DateTimeParseException.class)
  public void testAdaptingInstantInvalidInput() {
    Adapters.adapt(Instant.class, "hello");
  }

  @Test(expected = DateTimeParseException.class)
  public void testAdaptingInstantInvalidDate() {
    Adapters.adapt(Instant.class, "2000-13-01");
  }
}
