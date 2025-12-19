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

package com.taotao.cloud.hudi.multiversion;

import static org.apache.hudi.QuickstartUtils.getQuickstartWriteConfigs;
import static org.apache.hudi.config.HoodieWriteConfig.TABLE_NAME;

import java.util.Map;

import org.apache.hudi.DataSourceWriteOptions;
import org.apache.hudi.QuickstartUtils;
import org.apache.hudi.config.HoodieWriteConfig;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

/**
 * MultiVersionDemo
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public abstract class MultiVersionDemo {

    protected Map<String, String> properties;
    protected String basePath;

    protected static SparkSession spark =
            SparkSession.builder()
                    .appName("Hudi Datasource test")
                    .master("local[2]")
                    .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
                    .config("spark.io.compression.codec", "snappy")
                    .config("spark.sql.hive.convertMetastoreParquet", "false")
                    .getOrCreate();

    public MultiVersionDemo( Map<String, String> properties, String basePath ) {
        this.properties = properties;
        this.basePath = basePath;
    }

    public void writeHudi( Dataset<Row> df, SaveMode saveMode ) {
        df.write()
                .format("org.apache.hudi")
                .options(getQuickstartWriteConfigs())
                .options(properties)
                .option(DataSourceWriteOptions.TABLE_TYPE_OPT_KEY(), tableType())
                .option(DataSourceWriteOptions.PRECOMBINE_FIELD_OPT_KEY(), "ts")
                .option(DataSourceWriteOptions.RECORDKEY_FIELD_OPT_KEY(), "name")
                .option(DataSourceWriteOptions.PARTITIONPATH_FIELD_OPT_KEY(), "location")
                .option(TABLE_NAME, "MultiVersionDemo")
                .option("hoodie.embed.timeline.server", false)
                .mode(saveMode)
                .save(basePath);
    }

    protected abstract String tableType();
}
