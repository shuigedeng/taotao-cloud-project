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

import com.taotao.cloud.hudi.common.CustomDataGenerator;
import com.taotao.cloud.hudi.common.OpType;
import org.apache.hudi.hive.MultiPartKeysValueExtractor;
import org.apache.hudi.keygen.ComplexKeyGenerator;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;

/**
 * multi partition fields demo.
 */
public class MultiPartitionDemo extends PartitionDemo {
    private static final String BASE_PATH = "file:/tmp/hudi-partitions/multiPartitionDemo";
    private static final String TABLE_NAME = "multiPartitionDemo";
    private static final String PARTITION_FILED = "location,sex";
    private static final String KEY_GENERATOR = ComplexKeyGenerator.class.getName();
    private static final String EXTRACTOR_CLASS = MultiPartKeysValueExtractor.class.getName();

    public MultiPartitionDemo(ConfigBuilder configBuilder) {
        super(configBuilder);
    }

    public static void main(String[] args) {
        ConfigBuilder configBuilder = new ConfigBuilder();
        configBuilder
                .basePath(BASE_PATH)
                .saveMode(SaveMode.Overwrite)
                .tableName(TABLE_NAME)
                .partitionFields(PARTITION_FILED)
                .keyGenerator(KEY_GENERATOR)
                .hivePartitionFields(PARTITION_FILED)
                .hivePartitionExtractorClass(EXTRACTOR_CLASS);

        PartitionDemo partitionDemo = new MultiPartitionDemo(configBuilder);

        Dataset<Row> dataset = CustomDataGenerator.getCustomDataset(10, OpType.INSERT, spark);

        partitionDemo.writeHudi(dataset);
    }
}
