/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.rocketmq.flink.source.reader.deserializer;

import org.apache.rocketmq.common.message.MessageExt;

import org.apache.flink.api.common.serialization.DeserializationSchema.InitializationContext;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.util.Collector;

import java.io.IOException;
import java.util.List;

/** DeserializeWrapper to wrap DeserializationSchema */
public class RocketMQValueOnlyDeserializationSchemaWrapper<T>
        implements RocketMQDeserializationSchema<T> {
    private static final long serialVersionUID = 1L;
    private final DeserializationSchema<List<MessageExt>, T> deserializationSchema;

    public RocketMQValueOnlyDeserializationSchemaWrapper(
            DeserializationSchema<List<MessageExt>, T> deserializationSchema) {
        this.deserializationSchema = deserializationSchema;
    }

    @Override
    public void open(InitializationContext context) throws Exception {
        deserializationSchema.open(context);
    }

    @Override
    public void deserialize(List<MessageExt> record, Collector<T> out) throws IOException {
        deserializationSchema.deserialize(record, out);
    }

    @Override
    public TypeInformation<T> getProducedType() {
        return deserializationSchema.getProducedType();
    }
}
