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

package com.americanexpress.jexm.resources.beans.people;

import com.americanexpress.jexm.annotation.Header;
import java.io.Serializable;

public class PersonFullnameIllegalHeaderNameAndRefAndIndex implements Serializable {

  @Header(
    name = "Name",
    ref = "A",
    index = 0
  ) // a header cannot specify name, header and index simultaneuously
  private String name;

  @Header(index = 1)
  private String middlename;

  @Header(index = 2)
  private String lastname;

  @Override
  public String toString() {
    return (name == null ? "(null)" : name)
        + " "
        + (middlename == null ? "(null)" : middlename)
        + " "
        + (lastname == null ? "(null)" : lastname);
  }
}
