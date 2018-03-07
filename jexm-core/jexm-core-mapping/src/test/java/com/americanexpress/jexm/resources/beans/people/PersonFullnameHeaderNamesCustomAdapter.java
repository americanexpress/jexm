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

import com.americanexpress.jexm.annotation.Adapter;
import com.americanexpress.jexm.annotation.Header;
import com.americanexpress.jexm.resources.adapter.FirstLetterAdapter;
import com.americanexpress.jexm.resources.adapter.NameWithMrMsAdapter;
import java.io.Serializable;

public class PersonFullnameHeaderNamesCustomAdapter implements Serializable {

  public PersonFullnameHeaderNamesCustomAdapter(
      String nameWithMrMs, char middlenameFirstchar, String lastname) {
    this.nameWithMrMs = nameWithMrMs;
    this.middlenameFirstchar = middlenameFirstchar;
    this.lastname = lastname;
  }

  @Header(name = "Name")
  @Adapter(NameWithMrMsAdapter.class)
  private String nameWithMrMs;

  @Header(name = "Middlename")
  @Adapter(FirstLetterAdapter.class)
  private char middlenameFirstchar;

  @Header(name = "Lastname")
  private String lastname;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PersonFullnameHeaderNamesCustomAdapter that = (PersonFullnameHeaderNamesCustomAdapter) o;

    if (middlenameFirstchar != that.middlenameFirstchar) return false;
    if (nameWithMrMs != null ? !nameWithMrMs.equals(that.nameWithMrMs) : that.nameWithMrMs != null)
      return false;
    return lastname != null ? lastname.equals(that.lastname) : that.lastname == null;
  }

  @Override
  public int hashCode() {
    int result = nameWithMrMs != null ? nameWithMrMs.hashCode() : 0;
    result = 31 * result + (int) middlenameFirstchar;
    result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return (nameWithMrMs == null ? "(null)" : nameWithMrMs)
        + " "
        + middlenameFirstchar
        + " "
        + (lastname == null ? "(null)" : lastname);
  }
}
