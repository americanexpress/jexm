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

package com.americanexpress.jexm.parsing.config;

import com.americanexpress.jexm.annotation.Sheet;
import com.americanexpress.jexm.parsing.exceptions.IllegalSheetException;

public final class SheetConfig {

  private int index = -1;
  private String name = null;

  private SheetConfig() {
    // not meant to be initialised from outside the Builder
  }

  public int getIndex() {
    return index;
  }

  public String getName() {
    return name;
  }

  public boolean isIndexSet() {
    return this.index >= 0;
  }

  public boolean isNameSet() {
    return this.name != null && !this.name.isEmpty();
  }

  public static SheetConfig fromAnnotation(Sheet sheet) {
    return sheet == null ? null : new Builder().index(sheet.index()).name(sheet.name()).build();
  }

  public static class Builder {

    private SheetConfig sheetConfig = new SheetConfig();

    public Builder index(int i) {
      this.sheetConfig.index = i;
      return this;
    }

    public Builder name(String n) {
      this.sheetConfig.name = n;
      return this;
    }

    public SheetConfig build() {
      if (this.sheetConfig.isIndexSet()) {
        if (this.sheetConfig.isNameSet()) {
          // both the name and index are set
          throw new IllegalSheetException("Only one of Sheet name or index can be set.");
        } else {
          // only index is set. valid configuration
          return this.sheetConfig;
        }
      } else if (this.sheetConfig.isNameSet()) {
        // only name is set. valid configuration
        return this.sheetConfig;
      } else {
        throw new IllegalSheetException("Neither Sheet name nor index are set.");
      }
    }
  }
}
