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

package com.americanexpress.jexm.mapping;

import com.americanexpress.jexm.mapping.exceptions.IllegalRowClassException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import sun.reflect.ReflectionFactory;

/**
 * Utilities class to allow accessing constructors and fields in an object using reflection. This is
 * useful to produce instances of Java beans and update their internal properties.
 */
public final class ReflectionUtils {

  private ReflectionUtils() {
    // not meant to be initialised
  }

  private static final ReflectionFactory REFLECTION_FACTORY =
      ReflectionFactory.getReflectionFactory();

  /**
   * Creates an instance of the given class using the Object constructor. This allows initialising
   * objects which do not have explicit constructors.
   *
   * @param clazz Class used to produce an instance
   * @return New instance of the given class
   */
  public static <T> T newInstance(Class<T> clazz) {
    Objects.requireNonNull(clazz);

    // allows initialising an object which does not have an empty constructor.
    // It uses the default constructor from the Object class
    try {
      Constructor emptyConstructor =
          REFLECTION_FACTORY.newConstructorForSerialization(
              clazz, Object.class.getDeclaredConstructor());

      return clazz.cast(emptyConstructor.newInstance());
    } catch (NoSuchMethodException
        | InvocationTargetException
        | IllegalAccessException
        | InstantiationException e) {
      throw new IllegalRowClassException(clazz, e);
    }
  }

  /**
   * Writes to the field of a given object, bypassing access modifiers. If the value to be written
   * is {@code null}, the field is not updated.
   *
   * @param target Object with a field to be updated
   * @param field Field which is to be updated in the target Object
   * @param value New value to set the field
   */
  static void update(Object target, Field field, Object value) {
    Objects.requireNonNull(field);
    Objects.requireNonNull(target);

    if (value == null) {
      return; // cannot update null to a primitive field
    }

    field.setAccessible(true);

    try {
      field.set(target, value);
    } catch (IllegalAccessException e) {
      throw new IllegalRowClassException(target.getClass(), e);
    }
  }
}
