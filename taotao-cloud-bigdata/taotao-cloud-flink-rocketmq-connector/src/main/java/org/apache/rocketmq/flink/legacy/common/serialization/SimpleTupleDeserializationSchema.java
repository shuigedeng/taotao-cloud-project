/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.rocketmq.flink.legacy.common.serialization;

import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;

import java.nio.charset.StandardCharsets;

public class SimpleTupleDeserializationSchema
        implements KeyValueDeserializationSchema<Tuple2<String, String>> {

    @Override
    public Tuple2<String, String> deserializeKeyAndValue(byte[] key, byte[] value) {
        String keyString = key != null ? new String(key, StandardCharsets.UTF_8) : null;
        String valueString = value != null ? new String(value, StandardCharsets.UTF_8) : null;
        return new Tuple2<>(keyString, valueString);
    }

    @Override
    public TypeInformation<Tuple2<String, String>> getProducedType() {
        return TypeInformation.of(new TypeHint<Tuple2<String, String>>() {});
    }
}
