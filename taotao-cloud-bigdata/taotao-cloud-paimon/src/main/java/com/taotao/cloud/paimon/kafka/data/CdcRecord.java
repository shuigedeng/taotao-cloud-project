/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.paimon.kafka.data;

import org.apache.kafka.connect.data.Struct;
import org.apache.paimon.types.RowKind;

import java.util.Objects;

public class CdcRecord {

    private RowKind kind;

    private final Struct value;

    public CdcRecord(RowKind kind, Struct value) {
        this.kind = kind;
        this.value = value;
    }

    public static CdcRecord emptyRecord() {
        return new CdcRecord(RowKind.INSERT, null);
    }

    public RowKind kind() {
        return kind;
    }

    public Struct value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CdcRecord cdcRecord = (CdcRecord) o;
        return kind == cdcRecord.kind && Objects.equals(value, cdcRecord.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kind, value);
    }
}
