package com.taotao.cloud.hudi.partitions;

import com.taotao.cloud.hudi.common.CustomDataGenerator;
import com.taotao.cloud.hudi.common.OpType;
import org.apache.hudi.hive.NonPartitionedExtractor;
import org.apache.hudi.keygen.NonpartitionedKeyGenerator;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;

/**
 * non partition demo.
 */
public class NonPartitionDemo extends PartitionDemo {
    private static final String BASE_PATH = "file:/tmp/hudi-partitions/nonPartitionDemo";
    private static final String TABLE_NAME = "nonPartitionDemo";
    private static final String PARTITION_FILED = "";
    private static final String KEY_GENERATOR = NonpartitionedKeyGenerator.class.getName();
    private static final String EXTRACTOR_CLASS = NonPartitionedExtractor.class.getName();

    public NonPartitionDemo(ConfigBuilder configBuilder) {
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

        PartitionDemo partitionDemo = new NonPartitionDemo(configBuilder);

        Dataset<Row> dataset = CustomDataGenerator.getCustomDataset(10, OpType.INSERT, spark);

        partitionDemo.writeHudi(dataset);
    }
}
