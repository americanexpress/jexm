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
import java.io.Serializable;

public class PersonAgeThrowingExceptionIfInvalidAge implements Serializable {

  public PersonAgeThrowingExceptionIfInvalidAge(String name, int age) {
    this.name = name;
    this.age = age;
  }

  @Header(name = "Name")
  private String name;

  @Header(name = "Age")
  @Adapter(suppressAdapterException = false)
  private int age;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PersonAgeThrowingExceptionIfInvalidAge personAge = (PersonAgeThrowingExceptionIfInvalidAge) o;

    if (age != personAge.age) return false;
    return name != null ? name.equals(personAge.name) : personAge.name == null;
  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + age;
    return result;
  }

  @Override
  public String toString() {
    return name + " " + age;
  }
}
