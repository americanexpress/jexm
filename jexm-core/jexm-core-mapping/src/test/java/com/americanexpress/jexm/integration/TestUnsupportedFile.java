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

package com.americanexpress.jexm.integration;

import com.americanexpress.jexm.FileToTest;
import com.americanexpress.jexm.JEXMContext;
import com.americanexpress.jexm.parsing.exceptions.UnsupportedFileTypeException;
import com.americanexpress.jexm.resources.beans.people.PersonFirstName;
import java.io.UncheckedIOException;
import java.util.stream.Collectors;
import org.junit.Test;

public class TestUnsupportedFile {

  @Test(expected = UnsupportedFileTypeException.class)
  public final void testPeopleWithUnsupportedFileTypeShouldThrowException() {
    JEXMContext.newInstance(PersonFirstName.class)
        .read(FileToTest.UNSUPPORTED_FILE.path("unsupported"))
        .collect(Collectors.toList());
  }

  @Test(expected = UncheckedIOException.class)
  public final void testPeopleWithFileNotFoundShouldThrowException() {
    JEXMContext.newInstance(PersonFirstName.class)
        .read("not/there.csv")
        .collect(Collectors.toList());
  }
}
