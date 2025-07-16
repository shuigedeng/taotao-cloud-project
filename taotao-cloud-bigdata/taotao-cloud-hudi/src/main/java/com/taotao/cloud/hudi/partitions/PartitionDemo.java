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

package com.taotao.cloud.hudi.partitions;

import static org.apache.hudi.QuickstartUtils.getQuickstartWriteConfigs;
import static org.apache.hudi.config.HoodieWriteConfig.TABLE_NAME;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.hudi.DataSourceWriteOptions;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

public class PartitionDemo {
    private String tableName;
    private String partitionFields;
    private String hivePartitionFields;
    private String hivePartitionExtractorClass;
    private SaveMode saveMode;
    private String basePath;
    private String keyGenerator;
    private Map<String, String> properties;

    protected static SparkSession spark =
            SparkSession.builder()
                    .appName("Hudi Datasource test")
                    .master("local[2]")
                    .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
                    .config("spark.io.compression.codec", "snappy")
                    .config("spark.sql.hive.convertMetastoreParquet", "false")
                    .getOrCreate();

    public PartitionDemo(ConfigBuilder configBuilder) {
        this(configBuilder, new HashMap<>());
    }

    public PartitionDemo(ConfigBuilder configBuilder, Map<String, String> properties) {
        this.tableName = configBuilder.tableName;
        this.partitionFields = configBuilder.partitionFields;
        this.keyGenerator = configBuilder.keyGenerator;
        this.hivePartitionFields = configBuilder.hivePartitionFields;
        this.hivePartitionExtractorClass = configBuilder.hivePartitionExtractorClass;
        this.saveMode = configBuilder.saveMode;
        this.basePath = configBuilder.basePath;
        this.properties = properties;
    }

    public void writeHudi(Dataset<Row> df) {
        df.write()
                .format("org.apache.hudi")
                .options(getQuickstartWriteConfigs())
                .options(properties)
                .option(DataSourceWriteOptions.TABLE_TYPE_OPT_KEY(), "COPY_ON_WRITE")
                .option(DataSourceWriteOptions.PRECOMBINE_FIELD_OPT_KEY(), "ts")
                .option(DataSourceWriteOptions.RECORDKEY_FIELD_OPT_KEY(), "name")
                .option(DataSourceWriteOptions.PARTITIONPATH_FIELD_OPT_KEY(), partitionFields)
                .option(DataSourceWriteOptions.KEYGENERATOR_CLASS_OPT_KEY(), keyGenerator)
                .option(TABLE_NAME, tableName)
                .option("hoodie.datasource.hive_sync.enable", true)
                .option("hoodie.datasource.hive_sync.table", tableName)
                .option("hoodie.datasource.hive_sync.username", "root")
                .option("hoodie.datasource.hive_sync.password", "123456")
                .option("hoodie.datasource.hive_sync.jdbcurl", "jdbc:hive2://127.0.0.1:10000")
                .option("hoodie.datasource.hive_sync.partition_fields", hivePartitionFields)
                .option("hoodie.datasource.write.table.type", "COPY_ON_WRITE")
                .option("hoodie.embed.timeline.server", false)
                .option(
                        "hoodie.datasource.hive_sync.partition_extractor_class",
                        hivePartitionExtractorClass)
                .mode(saveMode)
                .save(basePath);
    }

    static class ConfigBuilder {
        private String tableName;
        private String partitionFields;
        private String keyGenerator;
        private String hivePartitionFields;
        private String hivePartitionExtractorClass;
        private SaveMode saveMode;
        private String basePath;
        private Properties properties;

        public ConfigBuilder() {}

        public ConfigBuilder tableName(String tableName) {
            this.tableName = tableName;
            return this;
        }

        public ConfigBuilder partitionFields(String partitionFields) {
            this.partitionFields = partitionFields;
            return this;
        }

        public ConfigBuilder keyGenerator(String keyGenerator) {
            this.keyGenerator = keyGenerator;
            return this;
        }

        public ConfigBuilder hivePartitionFields(String hivePartitionFields) {
            this.hivePartitionFields = hivePartitionFields;
            return this;
        }

        public ConfigBuilder hivePartitionExtractorClass(String hivePartitionExtractorClass) {
            this.hivePartitionExtractorClass = hivePartitionExtractorClass;
            return this;
        }

        public ConfigBuilder properties(Properties properties) {
            this.properties = properties;
            return this;
        }

        public ConfigBuilder saveMode(SaveMode saveMode) {
            this.saveMode = saveMode;
            return this;
        }

        public ConfigBuilder basePath(String basePath) {
            this.basePath = basePath;
            return this;
        }
    }
}
