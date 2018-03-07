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

package com.americanexpress.jexm.resources.adapter;

import com.americanexpress.jexm.adapter.CellAdapter;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;

public class DobToAgeAdapter implements CellAdapter<Integer> {

  @Override
  public Integer apply(String s) {
    if (s == null) {
      return 0;
    }
    return Period.between(LocalDate.parse(s), LocalDate.of(2017, Month.MAY, 1)).getYears();
  }
}
