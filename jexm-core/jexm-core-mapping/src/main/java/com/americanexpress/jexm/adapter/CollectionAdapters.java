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
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;
import sun.reflect.generics.reflectiveObjects.WildcardTypeImpl;

/**
 * Utility class which allows converting Strings representing an array of objects into a Java
 * collection type. For example we can adapt the String "1,2,3" into the equivalent of {@code
 * Arrays.asList(1,2,3)}.
 */
public final class CollectionAdapters {

  private CollectionAdapters() {
    // not meant to be initialised
  }

  private static final Logger log = LoggerFactory.getLogger(CollectionAdapters.class);

  /**
   * Converts a String representing an array, comma separated, into a collection of a reference
   * type. For field, the parameter type can be found by calling {@link
   * java.lang.reflect.Field#getGenericType()}. Consequently using a {@code List<String>} will
   * indicate to adapt the String into a list type with String elements, {@code HashSet<Integer>}
   * will indicate to adapt the String into a hash set with Integer elements, etc. If the generic
   * type is null, empty or is a wildcard, JEXMContext will assume the user wants a collection of
   * Strings. Example: {@code adapt(List.class, null, ("[Hello,World]")} will return the equivalent
   * of {@code Arrays.asList("Hello, "World")}
   *
   * @param collectionClass Class of collection type (eg. {@code Set.class}) for which an instance
   *     will be created.
   * @param s String containing list of objects in String format each, comma separated. It may
   *     optionally be prefixed by '[', '{' or '(' and suffixed by their closing equivalents.
   *     Example: "[1, 2, 3, 4]"
   * @return An instance of the given collection class as a result of parsing the String. If the
   *     String is empty or null, an empty array is returned.
   * @throws IllegalArgumentException if the class is not an instance of {@code Collection}.
   * @throws UnsupportedAdapterFieldException if the collection class is multi-dimensional
   */
  public static <T, E, C extends Collection<E>> T adapt(
      Class<T> collectionClass, Type parameterType, String s) {
    Objects.requireNonNull(collectionClass);

    if (!Collection.class.isAssignableFrom(collectionClass)) {
      throw new IllegalArgumentException(
          "Class " + collectionClass.getName() + " is not a collection type.");
    }

    @SuppressWarnings("unchecked")
    T t = (T) adaptCollection((Class<C>) collectionClass, parameterType, s);

    return t;
  }

  // inner class which provides a lookup of suppliers to initialise an instance of each supported
  // collection
  private static class CollectionSuppliers {

    private static Map<Class<? extends Collection>, Supplier<? extends Collection>> lookup =
        Collections.unmodifiableMap(
            Stream.of(
                    entry(Collection.class, ArrayList::new),
                    entry(AbstractCollection.class, ArrayList::new),
                    entry(List.class, ArrayList::new),
                    entry(AbstractList.class, ArrayList::new),
                    entry(ArrayList.class, ArrayList::new),
                    entry(LinkedList.class, LinkedList::new),
                    entry(Queue.class, ArrayDeque::new),
                    entry(Deque.class, ArrayDeque::new),
                    entry(ArrayDeque.class, ArrayDeque::new),
                    entry(Vector.class, Vector::new),
                    entry(Stack.class, Stack::new),
                    entry(Set.class, LinkedHashSet::new),
                    entry(LinkedHashSet.class, LinkedHashSet::new),
                    entry(HashSet.class, HashSet::new),
                    entry(TreeSet.class, TreeSet::new),
                    entry(BlockingDeque.class, LinkedBlockingDeque::new),
                    entry(LinkedBlockingDeque.class, LinkedBlockingDeque::new),
                    entry(CopyOnWriteArrayList.class, CopyOnWriteArrayList::new),
                    entry(CopyOnWriteArraySet.class, CopyOnWriteArraySet::new),
                    entry(AbstractQueue.class, SynchronousQueue::new),
                    entry(SynchronousQueue.class, SynchronousQueue::new),
                    entry(ConcurrentSkipListSet.class, ConcurrentSkipListSet::new))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));

    @SuppressWarnings("unchecked")
    static <T extends Collection> Supplier<T> get(Class<T> clazz) {
      return (Supplier<T>) lookup.get(clazz);
    }

    private static <T extends Collection> Map.Entry<Class<T>, Supplier<T>> entry(
        Class<T> clazz, Supplier<T> supp) {
      return new AbstractMap.SimpleEntry<>(clazz, supp);
    }
  }

  /**
   * Equivalent to {@link CollectionAdapters#adapt(Class, Type, String)}, but type-safe for the
   * collection class.
   *
   * @param collectionClass Collection class
   * @param type Generic type of collection elements
   * @param s String to be parsed
   * @param <E> Element type
   * @param <C> Collection type
   * @return Instance of the given collection class
   */
  private static <E, C extends Collection<E>> C adaptCollection(
      Class<C> collectionClass, Type type, String s) {

    /* Get array equivalent of the given collection parameter type. This takes advantage of the existing
    array adapters and then by using Collectors, we can convert them into the Collection itself.
    Example: List<String> -> String[]
             Set<Integer> -> Integer[] */
    E[] array = arrayOfGenericType(type, s);

    // Find which supported supplier to use in the Collectors.toCollection to return the right
    // collection type
    Supplier<C> supplier = CollectionSuppliers.get(collectionClass);

    if (supplier == null) {
      throw new UnsupportedAdapterFieldException(collectionClass);
    }

    return Arrays.stream(array).collect(Collectors.toCollection(supplier));
  }

  private static <E> E[] arrayOfGenericType(Type type, String s) {
    Class<?> arrayClass = arrayClassFromParameterType(type);

    return (E[]) ArrayAdapters.adapt(arrayClass, s);
  }

  @SuppressWarnings("unchecked")
  private static <E> Class<? extends E[]> arrayClassFromParameterType(Type parameterType) {

    Class<? extends E[]> arrayClass;

    if (parameterType instanceof ParameterizedType) {
      // if the generic type is parameterized (eg. List<String>)

      Type[] genericParameterTypes = ((ParameterizedType) parameterType).getActualTypeArguments();

      if (genericParameterTypes == null || genericParameterTypes.length == 0) {
        throw new IllegalArgumentException(
            "Actual type arguments of ParameterizedType is null or empty.");
      }

      // get the first parameter (eg. List<String> -> String
      Type firstType = genericParameterTypes[0];

      if (firstType instanceof WildcardTypeImpl) {
        // if we had a collection of a Wildcard (eg. List<?>), assume the user wants a list of
        // Strings
        log.warn("A wildcard type was specified for given collection. Populating it with Strings.");

        arrayClass = (Class<? extends E[]>) ((Class<?>) String[].class);
      } else if (firstType instanceof ParameterizedTypeImpl) {
        // if the collection has a parameterized parameter (eg. List<List<String>>), throw an
        // exception, as
        // we do not support that in the built-in adapters
        throw new UnsupportedAdapterFieldException(firstType.getClass());
      } else {
        Class<E> elementClass = (Class<E>) genericParameterTypes[0];

        arrayClass = (Class<? extends E[]>) ArrayAdapters.getArrayClass(elementClass);
      }
    } else {
      // if the collection does not have an explicit parameter type (eg. List with no diamond),
      // assume it is a
      // collection of Strings
      log.warn("No type was specified for given collection. Populating it with Strings.");

      arrayClass = (Class<? extends E[]>) ((Class<?>) String[].class);
    }

    return arrayClass;
  }
}
