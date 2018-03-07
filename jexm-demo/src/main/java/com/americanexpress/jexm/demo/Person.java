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
import com.americanexpress.jexm.annotation.Header;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.*;
import java.util.Arrays;
import java.util.stream.Stream;

class Person implements Serializable {

  @Header(name = "Id")
  private int id;

  @Header(name = "Name")
  private String name;

  @Header(name = "Lastname")
  private String lastname;

  @Header(name = "Date of Birth")
  private LocalDate dob;

  @Header(name = "Score")
  private double score;

  @Header(name = "Audition time")
  private LocalTime auditionTime;

  @Header(name = "Role")
  private Role role;

  @Header(name = "Favourite Colors")
  private Color[] favouriteColors;

  private enum Role {
    SINGER,
    DANCER,
    FIGHTER
  }

  private enum Color {
    BLACK,
    WHITE,
    RED,
    BLUE,
    PURPLE,
    GREEN,
    ORANGE,
    YELLOW
  }

  @Override
  public String toString() {
    return "("
        + id
        + ") "
        + name
        + " "
        + lastname
        + ", born in "
        + dob
        + ", score "
        + score
        + ", has audition at "
        + auditionTime
        + " for role "
        + role
        + ", favourite colors: "
        + Arrays.toString(favouriteColors);
  }

  public static void main(String[] args) {
    Path path = Paths.get("jexm-demo", "src", "main", "resources", "people.xlsx");

    try (Stream<Person> stream = JEXMContext.newInstance(Person.class).read(path)) {
      stream.forEach(System.out::println);
    }
  }
}
