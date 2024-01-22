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

package org.apache.flink.cep.dynamic.coordinator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/** A serialization util class for the {@link DynamicCepOperatorCoordinator}. */
public class DynamicCepOperatorCoordinatorSerdeUtils {

    /** The current pattern processor coordinator serde version. */
    private static final int CURRENT_VERSION = 1;

    /** Private constructor for utility class. */
    private DynamicCepOperatorCoordinatorSerdeUtils() {}

    /** Write the current serde version. */
    static void writeCoordinatorSerdeVersion(DataOutputStream out) throws IOException {
        out.writeInt(CURRENT_VERSION);
    }

    /** Verify the serde version. */
    static int verifyCoordinatorSerdeVersion(DataInputStream in) throws IOException {
        int version = in.readInt();
        if (version > CURRENT_VERSION) {
            throw new IOException(
                    "Unsupported DynamicCepOperatorCoordinator serde version " + version);
        }
        return version;
    }

    /** Read bytes. */
    static byte[] readBytes(DataInputStream in, int size) throws IOException {
        byte[] bytes = new byte[size];
        in.readFully(bytes);
        return bytes;
    }
}
