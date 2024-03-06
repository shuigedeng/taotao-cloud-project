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

import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.util.Collector;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.apache.flink.util.Preconditions.checkNotNull;

/** Deserialize value to string */
public class SimpleStringSchema implements DeserializationSchema<List<MessageExt>, String> {

    private static final long serialVersionUID = 1L;

    private String charName;

    /**
     * The charset to use to convert between strings and bytes. The field is transient because we
     * serialize a different delegate object instead
     */
    private transient Charset charset;

    /** Creates a new SimpleStringSchema that uses "UTF-8" as the encoding. */
    public SimpleStringSchema() {
        this(StandardCharsets.UTF_8);
    }

    /**
     * Creates a new SimpleStringSchema that uses the given charset to convert between strings and
     * bytes.
     *
     * @param charset The charset to use to convert between strings and bytes.
     */
    public SimpleStringSchema(Charset charset) {
        this.charset = checkNotNull(charset);
        this.charName = charset.name();
    }

    @Override
    public void open(
            org.apache.flink.api.common.serialization.DeserializationSchema.InitializationContext
                    context)
            throws Exception {
        DeserializationSchema.super.open(context);
        if (charset == null) {
            charset = Charset.forName(charName);
        }
    }

    @Override
    public void deserialize(List<MessageExt> record, Collector<String> out) throws IOException {
        for (MessageExt messageExt : record) {
            byte[] body = messageExt.getBody();
            String value = new String(body, charset);
            out.collect(value);
        }
    }

    @Override
    public TypeInformation<String> getProducedType() {
        return BasicTypeInfo.STRING_TYPE_INFO;
    }
}
