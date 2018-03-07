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

package com.americanexpress.jexm.resources.beans.date.localdate;

import com.americanexpress.jexm.annotation.Header;
import java.io.Serializable;
import java.time.LocalDate;

public class LocalDate_dmyyy implements Serializable {

  public LocalDate_dmyyy(LocalDate date) {
    this.date = date;
  }

  @Header(name = "d/m/yyyy")
  private LocalDate date;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    LocalDate_dmyyy that = (LocalDate_dmyyy) o;

    return date != null ? date.equals(that.date) : that.date == null;
  }

  @Override
  public int hashCode() {
    return date != null ? date.hashCode() : 0;
  }
}
