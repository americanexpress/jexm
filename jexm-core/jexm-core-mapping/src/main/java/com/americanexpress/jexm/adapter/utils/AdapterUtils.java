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

package com.americanexpress.jexm.adapter.utils;

import com.americanexpress.jexm.adapter.exceptions.ParserException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

/** Contains utility methods to handle data conversion for adapters. */
public final class AdapterUtils {

  private AdapterUtils() {
    // not meant to be initialised
  }

  private static final int ISO_LOCAL_DATE_LENGTH = 10;

  private static Map<String, Boolean> BOOLEAN_STRINGS =
      Collections.unmodifiableMap(
          new HashMap<String, Boolean>() {
            {
              put("TRUE", true);
              put("T", true);
              put("1", true);
              put("YES", true);
              put("Y", true);

              put("FALSE", false);
              put("F", false);
              put("0", false);
              put("NO", false);
              put("N", false);
            }
          });

  /**
   * Parses a boolean-like String into a Boolean type.
   *
   * @param s Case insensitive String representing a Boolean value.
   * @return {@code true} if s upper case is equals to "TRUE", "T", "1", "YES" or "Y". {@code false}
   *     if s upper case is equivalent to "FALSE", "F", "0", "NO", "N".
   * @throws ParserException if s is non-null and does not match the above
   */
  public static Boolean parseBoolean(String s) {
    Objects.requireNonNull(s);
    Boolean b = BOOLEAN_STRINGS.get(s.toUpperCase());
    if (b == null) {
      throw new ParserException(
          "Cannot convert \"" + s + "\" to " + Boolean.class.getName() + " type.");
    }
    return b;
  }

  /**
   * Parses a String into a Date
   *
   * @param s String in ISO_LOCAL_DATE_TIME or ISO_LOCAL_DATE format
   * @return Date
   */
  public static Date parseDate(String s) {
    Objects.requireNonNull(s);

    LocalDateTime ldt = parseLocalDateTime(s);
    return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
  }

  private static String withoutZ(String s) {
    return s.lastIndexOf('Z') == s.length() - 1 ? s.substring(0, s.length() - 1) : s;
  }

  /**
   * Parses a String into a LocalDate
   *
   * @param s String in ISO_LOCAL_DATE_TIME or ISO_LOCAL_DATE format
   * @return LocalDate
   */
  public static LocalDate parseLocalDate(String s) {
    Objects.requireNonNull(s);
    return (s.length() > ISO_LOCAL_DATE_LENGTH)
        ? parseLocalDateTime(s).toLocalDate()
        : LocalDate.parse(s);
  }

  /**
   * Parses a String into a LocalDateTime
   *
   * @param s String in ISO_LOCAL_DATE_TIME or ISO_LOCAL_DATE format
   * @return LocalDateTime
   */
  public static LocalDateTime parseLocalDateTime(String s) {
    Objects.requireNonNull(s);
    return (s.length() > ISO_LOCAL_DATE_LENGTH)
        ? LocalDateTime.parse(withoutZ(s))
        : LocalDateTime.of(LocalDate.parse(s), LocalTime.of(0, 0));
  }

  /**
   * Produces a numeric String without its decimal places and comma.
   *
   * @param s A numeric String with or without comma
   * @return The same String if it does not have comma, else a new String as a substring of the
   *     original before the comma.
   */
  public static String withoutDecimals(String s) {
    Objects.requireNonNull(s);
    int dotIndex = s.lastIndexOf(".");
    return dotIndex == -1 ? s : s.substring(0, dotIndex);
  }

  /**
   * Converts a case insensitive String into an instance of given enum type. Similar to
   * Enum.valueOf, but case insensitive.
   *
   * @param enumType Enum class to use for conversion
   * @param name Case insensitive String used to lookup over enum class
   * @return Instance of given enum class
   */
  public static Enum<?> findEnumIgnoreCase(Class<? extends Enum<?>> enumType, String name) {
    Objects.requireNonNull(enumType);
    Objects.requireNonNull(name);

    return Arrays.stream(enumType.getEnumConstants())
        .filter(e -> e.name().equalsIgnoreCase(name))
        .findFirst()
        .orElseThrow(
            () ->
                new IllegalArgumentException(
                    "Cannot convert String \""
                        + name
                        + "\" to enum of type "
                        + enumType.getName()));
  }

  /**
   * Calls {@link Class#forName(String)} without throwing a checked exception.
   *
   * @param s Class name
   * @return Class for given name
   * @throws RuntimeException if {@link Class#forName(String)} would throw a {@link
   *     ClassNotFoundException} for the input
   */
  public static Class<?> uncheckedClassForName(String s) {
    Objects.requireNonNull(s);

    try {
      return Class.forName(s);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
}
