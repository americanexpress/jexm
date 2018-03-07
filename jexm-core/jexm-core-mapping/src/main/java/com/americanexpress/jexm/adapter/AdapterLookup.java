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

package com.americanexpress.jexm.adapter;

import com.americanexpress.jexm.adapter.utils.AdapterUtils;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class containing an unmodifiable lookup table used to store all built-in adapters for fields of
 * the Java bean. When passing in a class, the lookup table will return an instance of type
 * CellAdapter, defining the rules to generate an instance of the given class from a String object.
 * This does not take into account arrays (please see ArrayAdapters) or enum types (please see
 * Adapters).
 */
final class AdapterLookup {

  private AdapterLookup() {}

  private static Map<Class<?>, CellAdapter<?>> lookup =
      Collections.unmodifiableMap(
          Stream.of(
                  // String
                  entry(String.class, String::toString),
                  entry(StringBuilder.class, StringBuilder::new),
                  entry(StringBuffer.class, StringBuffer::new),

                  // Primitives
                  entry(boolean.class, AdapterUtils::parseBoolean),
                  entry(byte.class, (s) -> Byte.parseByte(AdapterUtils.withoutDecimals(s))),
                  entry(char.class, (s) -> s.charAt(0)),
                  entry(double.class, Double::parseDouble),
                  entry(float.class, Float::parseFloat),
                  entry(int.class, (s) -> Integer.parseInt(AdapterUtils.withoutDecimals(s))),
                  entry(long.class, (s) -> Long.parseLong(AdapterUtils.withoutDecimals(s))),
                  entry(short.class, (s) -> Short.parseShort(AdapterUtils.withoutDecimals(s))),

                  // Boxed Primitives
                  entry(Boolean.class, AdapterUtils::parseBoolean),
                  entry(Byte.class, (s) -> Byte.valueOf(AdapterUtils.withoutDecimals(s))),
                  entry(Character.class, (s) -> s.charAt(0)),
                  entry(Double.class, Double::parseDouble),
                  entry(Float.class, Float::parseFloat),
                  entry(Integer.class, (s) -> Integer.valueOf(AdapterUtils.withoutDecimals(s))),
                  entry(Long.class, (s) -> Long.valueOf(AdapterUtils.withoutDecimals(s))),
                  entry(Short.class, (s) -> Short.valueOf(AdapterUtils.withoutDecimals(s))),

                  // Date and Time
                  entry(LocalDate.class, AdapterUtils::parseLocalDate),
                  entry(LocalTime.class, LocalTime::parse),
                  entry(LocalDateTime.class, AdapterUtils::parseLocalDateTime),
                  entry(Instant.class, Instant::parse),
                  entry(MonthDay.class, MonthDay::parse),
                  entry(YearMonth.class, YearMonth::parse),
                  entry(Date.class, AdapterUtils::parseDate),

                  // Numeric objects
                  entry(BigInteger.class, (s) -> new BigInteger(AdapterUtils.withoutDecimals(s))),
                  entry(BigDecimal.class, BigDecimal::new),

                  // SQL
                  entry(Time.class, Time::valueOf),
                  entry(java.sql.Date.class, java.sql.Date::valueOf),
                  entry(Timestamp.class, Timestamp::valueOf),

                  // Other
                  entry(Pattern.class, Pattern::compile),
                  entry(Class.class, AdapterUtils::uncheckedClassForName))
              .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));

  /**
   * Allows retrieving a {@link CellAdapter} which defines how to convert a String into an instance
   * of the given class.
   *
   * @param clazz A class for which we want the adapter rules
   * @param <T> Any parameter type
   * @return A cell adapter for the given class if found in the lookup table. Null otherwise
   */
  @SuppressWarnings("unchecked")
  static <T> CellAdapter<? extends T> get(Class<T> clazz) {
    Objects.requireNonNull(clazz);
    return (CellAdapter<? extends T>) lookup.get(clazz);
  }

  private static <T> Map.Entry<Class<T>, CellAdapter<? extends T>> entry(
      Class<T> clazz, CellAdapter<? extends T> adapter) {
    return new AbstractMap.SimpleEntry<>(clazz, adapter);
  }
}
