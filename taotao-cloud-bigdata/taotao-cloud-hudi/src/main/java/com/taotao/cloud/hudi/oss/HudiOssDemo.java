package com.taotao.cloud.hudi.oss;

import static org.apache.hudi.QuickstartUtils.convertToStringList;
import static org.apache.hudi.QuickstartUtils.getQuickstartWriteConfigs;
import static org.apache.hudi.config.HoodieWriteConfig.TABLE_NAME;
import static org.apache.spark.sql.SaveMode.Overwrite;

import java.io.IOException;
import java.util.List;
import org.apache.hudi.QuickstartUtils.DataGenerator;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

/**
 * Writing data into hudi on aliyun OSS.
 */
public class HudiOssDemo {
	public static void main(String[] args) throws IOException {
		System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
			"com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
		System.setProperty("HADOOP_USER_NAME", "root");
		System.setProperty("hadoop.home.dir", "/Users/shuigedeng/hadoop");

		SparkSession spark = SparkSession
			.builder()
			.appName("Hoodie Datasource test")
			.master("local[2]")
			.config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
			.config("spark.io.compression.codec", "snappy")
			.config("spark.sql.hive.convertMetastoreParquet", "false")
			.getOrCreate();

		JavaSparkContext jsc = new JavaSparkContext(spark.sparkContext());
		jsc.setCheckpointDir("file:///Users/shuigedeng/checkpoint");

		String tableName = "hudi_trips_cow";
		String basePath = "/Users/shuigedeng/hudi";
		DataGenerator dataGen = new DataGenerator();

		List<String> inserts = convertToStringList(dataGen.generateInserts(10));
		Dataset<Row> df = spark
			.read()
			.json(jsc.parallelize(inserts, 2));

		df.write()
			.format("org.apache.hudi")
			.options(getQuickstartWriteConfigs())
			.option(TABLE_NAME, tableName)
			.mode(Overwrite)
			.save(basePath);

		// Dataset<Row> roViewDF = spark
		// 	.read()
		// 	.format("org.apache.hudi")
		// 	.load(basePath + "/*/*/*");
		//
		// roViewDF.registerTempTable("hudi_ro_table");
		// spark
		// 	.sql("select *  from  hudi_ro_table")
		// 	.show(false);
		spark.stop();
	}
}
