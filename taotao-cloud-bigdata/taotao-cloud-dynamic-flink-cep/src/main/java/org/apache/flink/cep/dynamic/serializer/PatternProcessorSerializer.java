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

package org.apache.flink.cep.dynamic.serializer;

import org.apache.flink.annotation.Internal;
import org.apache.flink.cep.dynamic.processor.PatternProcessor;
import org.apache.flink.core.io.SimpleVersionedSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Serializer class for {@link PatternProcessor}.
 *
 * @param <IN> Base type of the elements appearing in the pattern.
 */
@Internal
public class PatternProcessorSerializer<IN>
        implements SimpleVersionedSerializer<List<PatternProcessor<IN>>> {

    private static final int CURRENT_VERSION = 1;

    public PatternProcessorSerializer() {}

    @Override
    public int getVersion() {
        return CURRENT_VERSION;
    }

    @Override
    public byte[] serialize(List<PatternProcessor<IN>> patternProcessors) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(baos)) {
            out.writeInt(patternProcessors.size());
            for (PatternProcessor<IN> patternProcessor : patternProcessors) {
                out.writeObject(patternProcessor);
                out.flush();
            }
            return baos.toByteArray();
        }
    }

    @Override
    public List<PatternProcessor<IN>> deserialize(int version, byte[] serialized)
            throws IOException {
        if (version != CURRENT_VERSION) {
            throw new IOException("Unrecognized version: " + version);
        }
        try (ByteArrayInputStream bais = new ByteArrayInputStream(serialized);
                ObjectInputStream in = new ObjectInputStream(bais)) {
            int length = in.readInt();
            List<PatternProcessor<IN>> patternProcessors = new ArrayList<>(length);
            for (int i = 0; i < length; i++) {
                patternProcessors.add((PatternProcessor<IN>) in.readObject());
            }
            return patternProcessors;
        } catch (ClassNotFoundException e) {
            throw new IOException(
                    "Could not deserialize the serialized pattern processor for version "
                            + version
                            + " as ClassNotFoundException is thrown: "
                            + e.getMessage());
        }
    }
}
