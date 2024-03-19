package com.taotao.cloud.hudi.partitions;

import com.taotao.cloud.hudi.common.CustomDataGenerator;
import com.taotao.cloud.hudi.common.OpType;
import org.apache.hudi.hive.SlashEncodedDayPartitionValueExtractor;
import org.apache.hudi.keygen.SimpleKeyGenerator;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;

/**
 * single partition field and in date format demo.
 */
public class DateFormatSinglePartitionDemo extends
		PartitionDemo {
    private static final String BASE_PATH = "file:/tmp/hudi-partitions/dateFormatSinglePartitionDemo";
    private static final String TABLE_NAME = "dateFormatSinglePartitionDemo";
    private static final String PARTITION_FILED = "date";
    private static final String KEY_GENERATOR = SimpleKeyGenerator.class.getName();
    private static final String EXTRACTOR_CLASS = SlashEncodedDayPartitionValueExtractor.class.getName();

    public DateFormatSinglePartitionDemo(ConfigBuilder configBuilder) {
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

        PartitionDemo partitionDemo = new DateFormatSinglePartitionDemo(configBuilder);

        Dataset<Row> dataset = CustomDataGenerator.getCustomDataset(10, OpType.INSERT, spark);

        partitionDemo.writeHudi(dataset);
    }
}
