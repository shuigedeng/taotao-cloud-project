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

import org.apache.flink.annotation.PublicEvolving;
import org.apache.flink.api.common.serialization.DeserializationSchema.InitializationContext;
import org.apache.flink.util.Collector;

import java.io.IOException;
import java.util.List;

/** An interface for the deserialization of RocketMQ records. */
public interface RocketMQDeserializationSchema<T>
        extends DeserializationSchema<List<MessageExt>, T> {

    /**
     * Initialization method for the schema. It is called before the actual working methods {@link
     * #deserialize} and thus suitable for one time setup work.
     *
     * <p>The provided {@link InitializationContext} can be used to access additional features such
     * as e.g. registering user metrics.
     *
     * @param context Contextual information that can be used during initialization.
     */
    @Override
    @PublicEvolving
    default void open(InitializationContext context) throws Exception {}

    /**
     * Deserializes the byte message.
     *
     * <p>Can output multiple records through the {@link Collector}. Note that number and size of
     * the produced records should be relatively small. Depending on the source implementation
     * records can be buffered in memory or collecting records might delay emitting checkpoint
     * barrier.
     *
     * @param record The MessageExts to deserialize.
     * @param out The collector to put the resulting messages.
     */
    @Override
    @PublicEvolving
    void deserialize(List<MessageExt> record, Collector<T> out) throws IOException;
}
