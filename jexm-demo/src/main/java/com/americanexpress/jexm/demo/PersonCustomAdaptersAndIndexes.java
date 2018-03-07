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

package com.americanexpress.jexm.demo;

import com.americanexpress.jexm.JEXMContext;
import com.americanexpress.jexm.adapter.CellAdapter;
import com.americanexpress.jexm.annotation.Adapter;
import com.americanexpress.jexm.annotation.Header;
import java.io.Serializable;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Stream;

/**
 * This demo will show you how you can use either the header index or the name itself to perform the
 * mapping. In addition it shows how the {@link Adapter} annotation can be used to customise the
 * mapping function. Some field field types have been changed from {@link Person} to show
 * JEXMContext can support mapping to different fields for the same data.
 */
class PersonCustomAdaptersAndIndexes implements Serializable {

  @Header(index = 0)
  private int id;

  @Header(index = 1)
  private String name;

  @Header(name = "Gender")
  @Adapter(MrMsAdapter.class)
  private String mr_ms; // Mr. or Ms.

  @Header(name = "Lastname")
  private String lastname;

  @Header(name = "Date of Birth")
  @Adapter(AgeAdapter.class)
  private int age;

  @Header(name = "Date of Birth")
  private Date dob;

  @Header(name = "Score")
  private double score;

  @Header(name = "Audition time")
  private LocalTime auditionTime;

  @Header(name = "Role")
  private Role role;

  @Header(name = "Favourite Colors")
  private String[] favouriteColors;

  private enum Role {
    SINGER,
    DANCER,
    FIGHTER
  }

  @Override
  public String toString() {
    return "("
        + id
        + ") "
        + mr_ms
        + " "
        + name
        + " "
        + lastname
        + ", born in "
        + dob
        + " (age: "
        + age
        + "), score "
        + score
        + ", has audition at "
        + auditionTime
        + " for role "
        + role
        + ", favourite colors: "
        + Arrays.toString(favouriteColors);
  }

  private static class MrMsAdapter implements CellAdapter<String> {
    @Override
    public String apply(String gender) {
      switch (gender.toUpperCase()) {
        case "MALE":
          return "Mr.";
        case "FEMALE":
          return "Ms.";
        default:
          return "";
      }
    }
  }

  private static class AgeAdapter implements CellAdapter<Integer> {
    @Override
    public Integer apply(String dob) {
      return Period.between(LocalDate.parse(dob), LocalDate.now()).getYears();
    }
  }

  public static void main(String[] args) {
    try (Stream<PersonCustomAdaptersAndIndexes> stream =
        JEXMContext.newInstance(PersonCustomAdaptersAndIndexes.class)
            .read(Paths.get("jexm-demo", "src", "main", "resources", "people.xlsx"))) {
      stream.forEach(System.out::println);
    }
  }
}
