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

package com.americanexpress.jexm.resources.beans.people;

import com.americanexpress.jexm.annotation.Adapter;
import com.americanexpress.jexm.annotation.Header;
import com.americanexpress.jexm.resources.adapter.FirstLetterAdapter;
import java.io.Serializable;

public class PersonFullnameHeaderNamesIllegalCustomAdapterType implements Serializable {

  // FirstLetterAdapter is a character adapter, which is not compatible with a String field
  @Header(name = "Name")
  @Adapter(FirstLetterAdapter.class)
  private String nameWithMrMs;
}
