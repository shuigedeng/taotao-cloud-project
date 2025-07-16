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

package com.taotao.cloud.paimon.kafka.sink;

import com.taotao.cloud.paimon.kafka.common.CatalogUtils;
import java.util.Collection;
import java.util.Map;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;
import org.apache.paimon.catalog.Catalog;

/**
 * Paimon sink task
 */
public class PaimonSinkTask extends SinkTask {
    private PaimonSinkConfig config;
    private Catalog catalog;
    private PaimonTableWriter paimonTableWriter;

    @Override
    public void start(Map<String, String> config) {
        this.config = new PaimonSinkConfig(config);
        this.catalog = CatalogUtils.createCataLog(this.config.catalogProps());
        this.paimonTableWriter = new PaimonTableWriter(this.catalog, this.config);
    }

    @Override
    public void put(Collection<SinkRecord> records) {
        records.stream()
                .forEach(
                        record -> {
                            try {
                                paimonTableWriter.write(record);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        });
    }

    @Override
    public void stop() {
        if (paimonTableWriter != null) {
            try {
                paimonTableWriter.close();
            } catch (Exception e) {
                throw new ConnectException(e);
            }
        }
    }

    @Override
    public String version() {
        return PaimonSinkConfig.version();
    }
}
