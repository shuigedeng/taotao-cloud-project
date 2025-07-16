/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.paimon.kafka.sink.naming;

import com.taotao.cloud.paimon.kafka.sink.PaimonSinkConfig;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.paimon.catalog.Identifier;

public interface TableNamingStrategy {
    /**
     * Resolves the logical table name from the sink record.
     *
     * @param config sink connector configuration, should not be {@code null}
     * @param record Kafka sink record, should not be {@code null}
     * @return the resolved logical table name; if {@code null} the record should not be processed
     */
    Identifier resolveTableName(PaimonSinkConfig config, SinkRecord record);
}
