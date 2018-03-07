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

import com.americanexpress.jexm.annotation.Adapter;
import com.americanexpress.jexm.mapping.ReflectionUtils;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This singleton class allows for caching of custom {@link CellAdapter} classes defined by API
 * users over the {@link Adapter} annotation. This is desired because initializing classes using
 * reflection is very expensive and each {@link CellAdapter} class only needs to be initialised
 * once.
 */
final class CustomAdapterCache {

  private static CustomAdapterCache INSTANCE;

  private Map<Class<? extends CellAdapter>, CellAdapter> cache = new ConcurrentHashMap<>();

  private CustomAdapterCache() {}

  /**
   * Retrieves from cache an existing or new instance of the given {@link CellAdapter} class.
   *
   * @param clazz {@link CellAdapter} which we want an instance of.
   * @param <T> Instance type of a {@link CellAdapter} class
   * @return A cached or new instance of {@link CellAdapter}
   */
  @SuppressWarnings("unchecked")
  <T extends CellAdapter> T get(Class<T> clazz) {
    return (T) cache.computeIfAbsent(Objects.requireNonNull(clazz), ReflectionUtils::newInstance);
  }

  /**
   * Method to retrieve the {@link CustomAdapterCache} lazily-evaluated singleton instance
   *
   * @return Singleton instance of this class
   */
  static synchronized CustomAdapterCache getInstance() {
    return INSTANCE == null ? (INSTANCE = new CustomAdapterCache()) : INSTANCE;
  }
}
