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

package com.americanexpress.jexm.resources.beans.date.localtime;

import com.americanexpress.jexm.annotation.Header;
import java.io.Serializable;
import java.time.LocalTime;

public class LocalTime_sq_h_sqmmss implements Serializable {

  public LocalTime_sq_h_sqmmss(LocalTime time) {
    this.time = time;
  }

  @Header(name = "[h]:mm:ss")
  private LocalTime time;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    LocalTime_sq_h_sqmmss that = (LocalTime_sq_h_sqmmss) o;

    return time != null ? time.equals(that.time) : that.time == null;
  }

  @Override
  public int hashCode() {
    return time != null ? time.hashCode() : 0;
  }
}
