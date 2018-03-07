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

import com.americanexpress.jexm.adapter.exceptions.UnsupportedAdapterFieldException;
import com.americanexpress.jexm.adapter.utils.AdapterUtils;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Objects;

/**
 * Class used to convert a given String object into the instance of a given class. The conversion
 * will be done according to rules defined in AdapterLookup and ArrayAdapters.
 */
public final class Adapters {

  private Adapters() {
    // not meant to be initialised
  }

  /**
   * Converts/adapts a String value into an instance of the given class. This supports classes
   * defined in AdapterLookup, any enum types and single-dimensional arrays of them. Example 1:
   * {@code adapt(int.class, "1", null)} will return an int with value {@code 1} Example 2: {@code
   * adapt(int[].class, "1, 2, 3", null)} will return a new {@code int[]{1,2,3}} Example 3: {@code
   * adapt(DayOfWeek.class, "WEDNESDAY", null)} will return {@code DayOfWeek.WEDNESDAY}
   *
   * @param clazz A class for which we want an instance of
   * @param rawCellValue The String we want to parse to produce an instance of the given class
   * @param customAdapterClass An optional adapter class to use instead of using the built-in
   *     lookup. If null, one from the lookup will be used.
   * @param <T> Instance type of the given class
   * @return Instance of given class, converted from given String
   */
  public static <T> T adapt(
      Class<T> clazz,
      Type genericType,
      String rawCellValue,
      Class<? extends CellAdapter> customAdapterClass) {
    Objects.requireNonNull(clazz);

    CellAdapter<? extends T> adapter;

    if (customAdapterClass == null) {
      // no custom adapter was specified by the user, so lookup adapter according to the field type

      if (clazz.isArray()) {
        return ArrayAdapters.adapt(clazz, rawCellValue);
      }

      if (Collection.class.isAssignableFrom(clazz)) {
        // if class is a collection type
        return CollectionAdapters.adapt(clazz, genericType, rawCellValue);
      }

      if (rawCellValue == null) {
        return null;
      }

      rawCellValue = rawCellValue.trim();

      if (rawCellValue.isEmpty()) {
        // if we have an empty (or all-spaces) String, return null
        return null;
      }

      if (clazz.isEnum()) {
        // if we have an enum type, convert the String into this type, ignoring case
        @SuppressWarnings("unchecked")
        T t = (T) AdapterUtils.findEnumIgnoreCase((Class<? extends Enum<?>>) clazz, rawCellValue);
        return t;
      }

      // retrieve built-in adapter for this class
      adapter = AdapterLookup.get(clazz);
      if (adapter == null) {
        throw new UnsupportedAdapterFieldException(clazz);
      }
    } else {
      // a custom adapter was specified by the user. Use it to convert the raw String type to field
      // type
      adapter = CustomAdapterCache.getInstance().get(customAdapterClass);
    }

    // from given adapter, finally perform conversion
    return adapter.apply(rawCellValue);
  }

  /**
   * @see {@link Adapters#adapt(Class, Type, String, Class)} Assumes there is no generic type nor
   *     custom adapter (ie. using non-parameterized built-in adapters only).
   */
  public static <T> T adapt(Class<T> clazz, String rawCellValue) {
    return adapt(clazz, null, rawCellValue, null);
  }
}
