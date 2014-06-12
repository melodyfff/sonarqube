/*
 * SonarQube, open source software quality management tool.
 * Copyright (C) 2008-2014 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * SonarQube is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * SonarQube is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.server.search.action;

import java.io.Serializable;

public class KeyIndexAction<K extends Serializable> extends IndexAction {

  private final K key;

  public KeyIndexAction(String indexType, Method method, K key) {
    super(indexType, method);
    this.key = key;
  }

  @Override
  public Class<?> getPayloadClass() {
    return String.class;
  }

  @Override
  public String getKey() {
    return this.key.toString();
  }

  @Override
  public void doExecute() {
    try {
      if (this.getMethod().equals(Method.DELETE)) {
        index.deleteByKey(this.key);
      } else if (this.getMethod().equals(Method.UPSERT)) {
        index.upsertByKey(this.key);
      }
    } catch (Exception e) {
      throw new IllegalStateException(this.getClass().getSimpleName() +
        "cannot execute " + this.getMethod() + " for " + this.key.getClass().getSimpleName() +
        " on type: " + this.getIndexType() +
        " on key: " + this.key, e);
    }
  }
}
