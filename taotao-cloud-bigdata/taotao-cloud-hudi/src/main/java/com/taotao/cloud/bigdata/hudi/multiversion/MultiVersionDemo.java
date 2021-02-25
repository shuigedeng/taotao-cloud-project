package com.taotao.cloud.bigdata.hudi.multiversion;

import org.apache.hudi.DataSourceWriteOptions;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

import java.util.Map;

import static org.apache.hudi.QuickstartUtils.getQuickstartWriteConfigs;
import static org.apache.hudi.config.HoodieWriteConfig.TABLE_NAME;

public abstract class MultiVersionDemo {
    protected Map<String, String> properties;
    protected String basePath;

    protected static SparkSession spark = SparkSession.builder().appName("Hudi Datasource test")
            .master("local[2]")
            .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
            .config("spark.io.compression.codec", "snappy")
            .config("spark.sql.hive.convertMetastoreParquet", "false")
            .getOrCreate();

    public MultiVersionDemo(Map<String, String> properties, String basePath) {
        this.properties = properties;
        this.basePath = basePath;
    }

    public void writeHudi(Dataset<Row> df, SaveMode saveMode) {
        df.write().format("org.apache.hudi").
                options(getQuickstartWriteConfigs()).
                options(properties).
                option(DataSourceWriteOptions.TABLE_TYPE_OPT_KEY(), tableType()).
                option(DataSourceWriteOptions.PRECOMBINE_FIELD_OPT_KEY(), "ts").
                option(DataSourceWriteOptions.RECORDKEY_FIELD_OPT_KEY(), "name").
                option(DataSourceWriteOptions.PARTITIONPATH_FIELD_OPT_KEY(), "location").
                option(TABLE_NAME, "MultiVersionDemo").
                option("hoodie.embed.timeline.server", false).
                mode(saveMode).
                save(basePath);
    }

    protected abstract String tableType();
}
