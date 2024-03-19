package com.taotao.cloud.hudi.partitions;

import com.taotao.cloud.hudi.common.CustomDataGenerator;
import com.taotao.cloud.hudi.common.OpType;
import java.util.HashMap;
import java.util.Map;
import org.apache.hudi.DataSourceWriteOptions;
import org.apache.hudi.hive.MultiPartKeysValueExtractor;
import org.apache.hudi.keygen.ComplexKeyGenerator;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;

/**
 * hive style partitioning demo.
 */
public class HiveStylePartitionDemo extends PartitionDemo {
    private static final String BASE_PATH = "file:/tmp/hudi-partitions/hiveStylePartitionDemo";
    private static final String TABLE_NAME = "hiveStylePartitionDemo";
    private static final String PARTITION_FILED = "location,sex";
    private static final String KEY_GENERATOR = ComplexKeyGenerator.class.getName();
    private static final String EXTRACTOR_CLASS = MultiPartKeysValueExtractor.class.getName();

    public HiveStylePartitionDemo(ConfigBuilder configBuilder, Map<String, String> config) {
        super(configBuilder, config);
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

        Map<String, String> otherConfig = new HashMap<>();
        otherConfig.put(DataSourceWriteOptions.HIVE_STYLE_PARTITIONING_OPT_KEY(), "true");

        PartitionDemo partitionDemo = new HiveStylePartitionDemo(configBuilder, otherConfig);

        Dataset<Row> dataset = CustomDataGenerator.getCustomDataset(10, OpType.INSERT, spark);

        partitionDemo.writeHudi(dataset);
    }
}
