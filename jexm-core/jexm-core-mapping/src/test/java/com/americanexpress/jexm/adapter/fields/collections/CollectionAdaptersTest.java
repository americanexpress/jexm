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

package com.americanexpress.jexm.adapter.fields.collections;

import static org.junit.Assert.assertEquals;

import com.americanexpress.jexm.adapter.CollectionAdapters;
import com.americanexpress.jexm.adapter.exceptions.UnsupportedAdapterFieldException;
import java.lang.reflect.Field;
import java.time.DayOfWeek;
import java.util.*;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("unused")
public class CollectionAdaptersTest {

  private static class UnsupportedElement {}

  private static List list;
  private static List<?> listWildcard;
  private static List<String> listOfStrings;
  private static List<Integer> listOfIntegers;
  private static List<DayOfWeek> listOfEnumsDayOfWeek;
  private static List<UnsupportedElement> listOfUnsupported;
  private static List<List<String>> listOfListOfStrings;
  private static ArrayList<String> arrayListOfStrings;
  private static Set<String> setOfStrings;
  private static HashSet<String> hashSetOfStrings;

  @Test
  public void testAdaptListNotGenericHelloWorldShouldReturnListOfStrings()
      throws NoSuchFieldException {
    Field f = CollectionAdaptersTest.class.getDeclaredField("list");

    Assert.assertEquals(
        Arrays.asList("Hello", "World", "!"),
        CollectionAdapters.adapt(f.getType(), f.getGenericType(), "Hello,World,!"));
  }

  @Test
  public void testAdaptListWildcardShouldReturnListOfStrings() throws NoSuchFieldException {
    Field f = CollectionAdaptersTest.class.getDeclaredField("listWildcard");

    assertEquals(
        Arrays.asList("Hello", "World", "!"),
        CollectionAdapters.adapt(f.getType(), f.getGenericType(), "Hello,World,!"));
  }

  @Test
  public void testAdaptListOfStringsHelloWorldShouldReturnCorrectValues()
      throws NoSuchFieldException {
    Field f = CollectionAdaptersTest.class.getDeclaredField("listOfStrings");

    assertEquals(
        Arrays.asList("Hello", "World", "!"),
        CollectionAdapters.adapt(f.getType(), f.getGenericType(), "Hello,World,!"));
  }

  @Test
  public void testAdaptListOfStringsEmptyInputShouldReturnEmptyList() throws NoSuchFieldException {
    Field f = CollectionAdaptersTest.class.getDeclaredField("listOfStrings");

    assertEquals(
        Collections.emptyList(), CollectionAdapters.adapt(f.getType(), f.getGenericType(), ""));
  }

  @Test
  public void testAdaptListOfStringsNullInputShouldReturnEmptyList() throws NoSuchFieldException {
    Field f = CollectionAdaptersTest.class.getDeclaredField("listOfStrings");

    assertEquals(
        Collections.emptyList(), CollectionAdapters.adapt(f.getType(), f.getGenericType(), null));
  }

  @Test(expected = NullPointerException.class)
  public void testAdaptCollectionClassNotProvidedShouldThrowException()
      throws NoSuchFieldException {
    Field f = CollectionAdaptersTest.class.getDeclaredField("listOfStrings");

    CollectionAdapters.adapt(null, f.getGenericType(), "Hello,World,!");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAdaptNonCollectionClassShouldThrowException() throws NoSuchFieldException {
    Field f = CollectionAdaptersTest.class.getDeclaredField("listOfStrings");

    CollectionAdapters.adapt(String.class, f.getGenericType(), "Hello,World,!");
  }

  @Test(expected = UnsupportedAdapterFieldException.class)
  public void testAdaptUnsupportedCollectionClassShouldThrowException()
      throws NoSuchFieldException {
    Field f = CollectionAdaptersTest.class.getDeclaredField("listOfStrings");

    class MyCollection<T> extends ArrayList<T> {}

    CollectionAdapters.adapt(MyCollection.class, f.getGenericType(), "Hello,World,!");
  }

  @Test(expected = UnsupportedAdapterFieldException.class)
  public void testAdaptMultiDimensionalListShouldThrowException() throws NoSuchFieldException {
    Field f = CollectionAdaptersTest.class.getDeclaredField("listOfListOfStrings");

    CollectionAdapters.adapt(f.getType(), f.getGenericType(), "Hello,World,!");
  }

  @Test
  public void testAdaptUnsupportedCollectionElementClassShouldReturnNullValues()
      throws NoSuchFieldException {
    Field f = CollectionAdaptersTest.class.getDeclaredField("listOfUnsupported");

    assertEquals(
        Arrays.asList(null, null, null),
        CollectionAdapters.adapt(f.getType(), f.getGenericType(), "Hello,World,!"));
  }

  @Test
  public void testAdaptListWithNullGenericTypeShouldReturnListOfStrings()
      throws NoSuchFieldException {
    Field f = CollectionAdaptersTest.class.getDeclaredField("listWildcard");

    assertEquals(
        Arrays.asList("Hello", "World", "!"),
        CollectionAdapters.adapt(f.getType(), null, "Hello,World,!"));
  }

  @Test
  public void testAdaptListOfStringsOneValueShouldReturSingleValueList()
      throws NoSuchFieldException {
    Field f = CollectionAdaptersTest.class.getDeclaredField("listOfStrings");

    assertEquals(
        Arrays.asList("Hello"), CollectionAdapters.adapt(f.getType(), f.getGenericType(), "Hello"));
  }

  @Test
  public void testAdaptListOfIntegers123ShouldReturnCorrectValues() throws NoSuchFieldException {
    Field f = CollectionAdaptersTest.class.getDeclaredField("listOfIntegers");

    assertEquals(
        Arrays.asList(1, 2, 3), CollectionAdapters.adapt(f.getType(), f.getGenericType(), "1,2,3"));
  }

  @Test
  public void testAdaptListOfEnumsDayOfWeekShouldReturnCorrectValues() throws NoSuchFieldException {
    Field f = CollectionAdaptersTest.class.getDeclaredField("listOfEnumsDayOfWeek");

    assertEquals(
        Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY),
        CollectionAdapters.adapt(f.getType(), f.getGenericType(), "Monday,Tuesday,Wednesday"));
  }

  @Test
  public void testAdaptArrayListOfStringsHelloWorldShouldReturnCorrectValues()
      throws NoSuchFieldException {
    Field f = CollectionAdaptersTest.class.getDeclaredField("arrayListOfStrings");

    assertEquals(
        Arrays.asList("Hello", "World", "!"),
        CollectionAdapters.adapt(f.getType(), f.getGenericType(), "Hello,World,!"));
  }

  @Test
  public void testAdaptSetOfStringsHelloWorldShouldReturnCorrectValues()
      throws NoSuchFieldException {
    Field f = CollectionAdaptersTest.class.getDeclaredField("setOfStrings");

    assertEquals(
        new HashSet<>(Arrays.asList("Hello", "World", "!")),
        CollectionAdapters.adapt(f.getType(), f.getGenericType(), "Hello,World,!"));
  }

  @Test
  public void testAdaptHashSetOfStringsHelloWorldShouldReturnCorrectValues()
      throws NoSuchFieldException {
    Field f = CollectionAdaptersTest.class.getDeclaredField("hashSetOfStrings");

    assertEquals(
        new HashSet<>(Arrays.asList("Hello", "World", "!")),
        CollectionAdapters.adapt(f.getType(), f.getGenericType(), "Hello,World,!"));
  }
}
