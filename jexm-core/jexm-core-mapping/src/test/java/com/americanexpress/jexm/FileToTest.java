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

package com.americanexpress.jexm;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Ignore;

@Ignore
public enum FileToTest {
  PERSON_AGES_FILE("people_ages"),
  PERSON_AGES_INVALID_FILE("people_invalid_ages"),
  PERSON_FIRST_NAMES("people_first_names"),
  PERSON_FIRST_NAMES_STYLED("people_first_names_styled"),
  PERSON_FIRST_NAMES_MULTIPLE_SHEETS("people_first_names_multiple_sheets"),
  PERSON_FIRST_NAMES_ILLEGAL_DUPLICATE_HEADERS("people_first_names_illegal_duplicate_headers"),
  PERSON_NAMES_FILE("people_names"),
  PERSON_NAMES_INCOMPLETE_FILE("people_names_incomplete"),
  PERSON_NAMES_AND_DOB_FILE("people_names_and_dob"),
  PERSON_NAMES_ONLY_HEADERS("people_names_only_headers"),
  PERSON_NAMES_ONLY_DOB_COMPLETE("people_names_only_dob_complete"),
  PERSON_NAMES_ONLY_MIDDLENAME_COMPLETE("people_names_only_middlename"),
  PERSON_NAMES_SKIP_ONE_ROW("people_names_skip_one"),
  PERSON_NAMES_SHIFTED_RIGHT("people_names_shifted_right"),
  PERSON_NAMES_SHIFTED_DOWN("people_names_shifted_down"),
  PERSON_NAMES_SHIFTED_RIGHT_AND_DOWN("people_names_shifted_right_and_down"),
  PERSON_LOTS_OF_INFO("people_lots_of_info"),
  DATES_AND_TIMES("dates_and_times"),
  NUMBERS_ARRAY("numbers_array"),
  UNSUPPORTED_FILE("unsupported_file"),
  EMPTY_FILE("empty");

  private String filenameWithoutExtension;

  FileToTest(String filenameWithoutExtension) {
    this.filenameWithoutExtension = filenameWithoutExtension;
  }

  public Path path(String extension) {
    return Paths.get(
        "src", "test", "resources", extension, filenameWithoutExtension + "." + extension);
  }

  public Path path(ExcelExtension extension) {
    return path(extension.toString());
  }
}
