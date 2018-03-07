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

public class PersonFullnameHeaderNoName implements Serializable {

  public PersonFullnameHeaderNoName(String name, String middlename, String lastname) {
    this.Name = name;
    this.Middlename = middlename;
    this.Lastname = lastname;
  }

  @Header private String Name;

  @Header private String Middlename;

  @Header private String Lastname; //

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PersonFullnameHeaderNoName that = (PersonFullnameHeaderNoName) o;

    if (Name != null ? !Name.equals(that.Name) : that.Name != null) return false;
    if (Middlename != null ? !Middlename.equals(that.Middlename) : that.Middlename != null)
      return false;
    return Lastname != null ? Lastname.equals(that.Lastname) : that.Lastname == null;
  }

  @Override
  public int hashCode() {
    int result = Name != null ? Name.hashCode() : 0;
    result = 31 * result + (Middlename != null ? Middlename.hashCode() : 0);
    result = 31 * result + (Lastname != null ? Lastname.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return (Name == null ? "(null)" : Name)
        + " "
        + (Middlename == null ? "(null)" : Middlename)
        + " "
        + (Lastname == null ? "(null)" : Lastname);
  }
}
