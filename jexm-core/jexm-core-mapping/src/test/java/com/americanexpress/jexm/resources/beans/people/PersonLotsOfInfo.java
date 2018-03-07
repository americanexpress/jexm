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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

public class PersonLotsOfInfo implements Serializable {

  public enum Gender {
    MALE,
    FEMALE,
    OTHER
  }

  public enum Role {
    SINGER,
    DANCER,
    FIGHTER
  }

  public enum Color {
    BLACK,
    WHITE,
    RED,
    BLUE,
    PURPLE,
    GREEN,
    ORANGE,
    YELLOW
  }

  @Header(name = "Id")
  private int id;

  @Header(name = "Name")
  private String name;

  @Header(name = "Middlename")
  private String middlename;

  @Header(name = "Lastname")
  private String lastname;

  @Header(name = "Date of Birth")
  private LocalDate dob;

  @Header(name = "Gender")
  private Gender gender;

  @Header(name = "Score")
  private double score;

  @Header(name = "Audition time")
  private LocalTime auditionTime;

  @Header(name = "Role")
  private Role role;

  @Header(name = "Favourite Colors")
  private Color[] favouriteColors;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PersonLotsOfInfo that = (PersonLotsOfInfo) o;

    if (id != that.id) return false;
    if (Double.compare(that.score, score) != 0) return false;
    if (name != null ? !name.equals(that.name) : that.name != null) return false;
    if (middlename != null ? !middlename.equals(that.middlename) : that.middlename != null)
      return false;
    if (lastname != null ? !lastname.equals(that.lastname) : that.lastname != null) return false;
    if (dob != null ? !dob.equals(that.dob) : that.dob != null) return false;
    if (gender != that.gender) return false;
    if (auditionTime != null ? !auditionTime.equals(that.auditionTime) : that.auditionTime != null)
      return false;
    if (role != that.role) return false;
    return Arrays.equals(favouriteColors, that.favouriteColors);
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    result = id;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (middlename != null ? middlename.hashCode() : 0);
    result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
    result = 31 * result + (dob != null ? dob.hashCode() : 0);
    result = 31 * result + (gender != null ? gender.hashCode() : 0);
    temp = Double.doubleToLongBits(score);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    result = 31 * result + (auditionTime != null ? auditionTime.hashCode() : 0);
    result = 31 * result + (role != null ? role.hashCode() : 0);
    result = 31 * result + Arrays.hashCode(favouriteColors);
    return result;
  }

  @Override
  public String toString() {
    return "("
        + id
        + ") "
        + name
        + (middlename == null ? "" : middlename)
        + " "
        + lastname
        + ", born in "
        + dob
        + ", "
        + gender
        + ", score "
        + score
        + ", has audition at "
        + auditionTime
        + ", currently on role "
        + role
        + ", favourite colors: "
        + Arrays.toString(favouriteColors);
  }

  public static class Builder {

    private PersonLotsOfInfo c;

    public Builder() {
      c = new PersonLotsOfInfo();
    }

    public Builder id(int id) {
      c.id = id;
      return this;
    }

    public Builder name(String name) {
      c.name = name;
      return this;
    }

    public Builder middlename(String middlename) {
      c.middlename = middlename;
      return this;
    }

    public Builder lastname(String lastname) {
      c.lastname = lastname;
      return this;
    }

    public Builder dob(LocalDate dob) {
      c.dob = dob;
      return this;
    }

    public Builder gender(Gender gender) {
      c.gender = gender;
      return this;
    }

    public Builder role(Role role) {
      c.role = role;
      return this;
    }

    public Builder score(double score) {
      c.score = score;
      return this;
    }

    public Builder auditionTime(LocalTime auditionTime) {
      c.auditionTime = auditionTime;
      return this;
    }

    public Builder favouriteColours(Color[] favouriteColours) {
      c.favouriteColors = favouriteColours;
      return this;
    }

    public PersonLotsOfInfo build() {
      return c;
    }
  }
}
