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
import java.lang.reflect.Array;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class which allows converting Strings representing an array of objects into a Java array
 * itself. For example we can adapt the String "1,2,3" into {@code new int[]{1,2,3}}.
 */
public final class ArrayAdapters {

  private ArrayAdapters() {
    // not meant to be initialised
  }

  private static final Logger log = LoggerFactory.getLogger(ArrayAdapters.class);

  /**
   * Converts a String representing an array, comma separated, into an array object itself of a
   * given class based on the build-in adapters. Example: {@code adapt(int[].class, "[1, 2, 3, 4]")}
   * will return {@code new int[]{1,2,3,4}}
   *
   * @param arrayClass Class of array type (eg. {@code int[].class}) for which an instance will be
   *     created
   * @param s String containing list of objects in String format each, comma separated. It may
   *     optionally be prefixed by '[', '{' or '(' and suffixed by their closing equivalents.
   *     Example: "[1, 2, 3, 4]"
   * @return An instance of the given array class as a result of parsing the String. If the String
   *     is empty or null, an empty array is returned.
   * @throws IllegalArgumentException if the class is not an array type.
   * @throws UnsupportedAdapterFieldException if the array class is multi-dimensional
   */
  @SuppressWarnings("unchecked")
  static <T> T adapt(Class<T> arrayClass, String s) {
    Objects.requireNonNull(arrayClass);
    if (!arrayClass.isArray()) {
      throw new IllegalArgumentException(
          "Class " + arrayClass.getName() + " is not an array type.");
    }

    Class<?> componentClass = arrayClass.getComponentType();

    if (componentClass.isArray()) {
      throw new UnsupportedAdapterFieldException(
          arrayClass,
          "Arrays of multiple dimensions are not supported by the built-in adapters. "
              + "You can define your own using the Adapter annotation.");
    }

    String[] stringArray = asStringArray(s);

    if (componentClass.isPrimitive()) {
      return adaptPrimitiveArray(arrayClass, stringArray);
    } else {
      return (T) adaptObjectArray((Class<Object[]>) arrayClass, stringArray);
    }
  }

  /**
   * Converts a String array into an array of objects itself of a given class based on the built-in
   * adapters.
   *
   * @param arrayClass lass of array type (eg. {@code int[].class}) for which an instance will be
   *     created
   * @param strArr Array of Strings, each of which will be converted into an object type according
   *     to their built-in adapter rules.
   * @return An instance of the given array class as a result of parsing each String in the
   *     parameter String array.
   */
  @SuppressWarnings("unchecked")
  private static <T> T[] adaptObjectArray(Class<T[]> arrayClass, String[] strArr) {

    // eg. int[].class.getComponentType() returns int.class
    Class<T> componentClass = (Class<T>) arrayClass.getComponentType();

    // Create an array to store the adapted/converted elements of each String in the rawElements
    // array
    T[] arr = (T[]) Array.newInstance(componentClass, strArr.length);

    for (int i = 0; i < strArr.length; i++) {
      try {
        arr[i] = Adapters.adapt(componentClass, strArr[i]);
      } catch (Exception e) {
        printAdapterExceptionInArrayElement(i, strArr[i], componentClass, e);
      }
    }

    return arr;
  }

  /**
   * Produces an array of a primitive value (given primitive array class) from a String array.
   *
   * @param arrayType Primitive array type (eg. {@code int[].class}, {@code long[].class}, etc.
   * @param stringArr String array to be adapted
   * @param <U> Primitive array type
   * @return Primitive array as a result of adapting a String array
   */
  private static <U> U adaptPrimitiveArray(Class<U> arrayType, String[] stringArr) {
    if (arrayType == int[].class) {
      return (U) toIntArray(stringArr);
    } else if (arrayType == long[].class) {
      return (U) toLongArray(stringArr);
    } else if (arrayType == double[].class) {
      return (U) toDoubleArray(stringArr);
    } else if (arrayType == byte[].class) {
      return (U) toByteArray(stringArr);
    } else if (arrayType == float[].class) {
      return (U) toFloatArray(stringArr);
    } else if (arrayType == boolean[].class) {
      return (U) toBooleanArray(stringArr);
    } else if (arrayType == char[].class) {
      return (U) toCharArray(stringArr);
    } else if (arrayType == short[].class) {
      return (U) toShortArray(stringArr);
    } else {
      throw new IllegalArgumentException("Missing a primitive array converter for " + arrayType);
    }
  }

  static Class<?> getArrayClass(Class<?> elementClass) {
    Objects.requireNonNull(elementClass);
    return Array.newInstance(elementClass, 0).getClass();
  }

  /**
   * @param s A String which may or may not start and end with brackets.
   * @return true if the String is prefixed by '[', '{' or '(' and suffixed by their closing
   *     equivalents, false otherwise.
   */
  private static boolean isWrappedByBrackets(String s) {
    if (s != null && s.length() >= 2) {
      char firstChar = s.charAt(0);
      char lastChar = s.charAt(s.length() - 1);

      return (firstChar == '(' && lastChar == ')')
          || (firstChar == '[' && lastChar == ']')
          || (firstChar == '{' && lastChar == '}');
    } else {
      return false;
    }
  }

  /**
   * @param s A String which may or may not start and end with brackets.
   * @return The given String itself if it does not start/end with brackets, else a new String as a
   *     substring without such brackets.
   */
  private static String removeBrackets(String s) {
    if (isWrappedByBrackets(s)) {
      return s.substring(1, s.length() - 1).trim();
    } else {
      return s;
    }
  }

  /**
   * @param s String to be split
   * @return A String array by splitting the given String on each comma ','. If the String parameter
   *     is null, an empty array is returned. *
   */
  private static String[] asStringArray(String s) {
    if (s == null) {
      return new String[0]; // if the input is null, return an empty array
    }

    s = removeBrackets(s.trim());

    // if the input is all spaces (or simply empty), we will return an empty array
    // split with -1 stops removal of empty elements between the commas
    return s.isEmpty() ? new String[0] : s.split(",", -1);
  }

  private static void printAdapterExceptionInArrayElement(
      int index, String stringValue, Class<?> componentClass, Exception e) {
    log.warn(
        "Unable to adapt array element at position {} (raw value=\"{}\") to class {}. Exception: {}",
        index,
        stringValue,
        componentClass.getName(),
        e.getClass().getName());
  }

  private static boolean[] toBooleanArray(String[] strArray) {
    boolean[] booleanArr = new boolean[strArray.length];
    for (int i = 0; i < strArray.length; i++) {
      try {
        booleanArr[i] = Adapters.adapt(boolean.class, strArray[i]);
      } catch (Exception e) {
        printAdapterExceptionInArrayElement(i, strArray[i], boolean.class, e);
      }
    }
    return booleanArr;
  }

  private static int[] toIntArray(String[] strArray) {
    int[] intArray = new int[strArray.length];
    for (int i = 0; i < strArray.length; i++) {
      try {
        intArray[i] = Adapters.adapt(int.class, strArray[i]);
      } catch (Exception e) {
        printAdapterExceptionInArrayElement(i, strArray[i], int.class, e);
      }
    }
    return intArray;
  }

  private static char[] toCharArray(String[] strArray) {
    char[] charArray = new char[strArray.length];
    for (int i = 0; i < strArray.length; i++) {
      try {
        charArray[i] = Adapters.adapt(char.class, strArray[i]);
      } catch (Exception e) {
        printAdapterExceptionInArrayElement(i, strArray[i], char.class, e);
      }
    }
    return charArray;
  }

  private static byte[] toByteArray(String[] strArray) {
    byte[] byteArray = new byte[strArray.length];
    for (int i = 0; i < strArray.length; i++) {
      try {
        byteArray[i] = Adapters.adapt(byte.class, strArray[i]);
      } catch (Exception e) {
        printAdapterExceptionInArrayElement(i, strArray[i], byte.class, e);
      }
    }
    return byteArray;
  }

  private static short[] toShortArray(String[] strArray) {
    short[] shortArray = new short[strArray.length];
    for (int i = 0; i < strArray.length; i++) {
      try {
        shortArray[i] = Adapters.adapt(short.class, strArray[i]);
      } catch (Exception e) {
        printAdapterExceptionInArrayElement(i, strArray[i], short.class, e);
      }
    }
    return shortArray;
  }

  private static float[] toFloatArray(String[] strArray) {
    float[] floatArray = new float[strArray.length];
    for (int i = 0; i < strArray.length; i++) {
      try {
        floatArray[i] = Adapters.adapt(float.class, strArray[i]);
      } catch (Exception e) {
        printAdapterExceptionInArrayElement(i, strArray[i], float.class, e);
      }
    }
    return floatArray;
  }

  private static double[] toDoubleArray(String[] strArray) {
    double[] doubleArray = new double[strArray.length];
    for (int i = 0; i < strArray.length; i++) {
      try {
        doubleArray[i] = Adapters.adapt(double.class, strArray[i]);
      } catch (Exception e) {
        printAdapterExceptionInArrayElement(i, strArray[i], double.class, e);
      }
    }
    return doubleArray;
  }

  private static long[] toLongArray(String[] strArray) {
    long[] longArray = new long[strArray.length];
    for (int i = 0; i < strArray.length; i++) {
      try {
        longArray[i] = Adapters.adapt(long.class, strArray[i]);
      } catch (Exception e) {
        printAdapterExceptionInArrayElement(i, strArray[i], long.class, e);
      }
    }
    return longArray;
  }
}
