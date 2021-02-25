package com.taotao.cloud.bigdata.hudi.common;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.ArrayList;
import java.util.List;

/**
 * A util class to generator data for demos.
 */
public class CustomDataGenerator {

	public static Dataset<Row> getCustomDataset(int num, OpType opType, SparkSession spark) {
		List<String> datas = new ArrayList<>();
		for (int i = 0; i < num; i++) {
			if (opType == OpType.INSERT) {
				datas.add("{ \"name\": \"le" + (i + 1) + "\""
					+ ", \"ts\": 1574297893837, \"age\": 16, \"location\": \"beijing\", \"sex\":\"male\", \"date\":\"2020/08/16\"}");
				datas.add("{ \"name\": \"le" + (i + 1) + "\""
					+ ", \"ts\": 1574297893837, \"age\": 16, \"location\": \"shanghai\", \"sex\":\"male\", \"date\":\"2020/08/15\"}");
			} else if (opType == OpType.UPDATE) {
				datas.add("{ \"name\": \"le" + (i + 1) + "\""
					+ ", \"ts\": 1574297893837, \"age\": 17, \"location\": \"beijing\", \"sex\":\"male\", \"date\":\"2020/08/16\"}");
			} else if (opType == OpType.APPEND) {
				datas.add("{ \"name\": \"lee" + (i + 1) + "\""
					+ ", \"ts\": 1574297893837, \"age\": 16, \"location\": \"shanghai\", \"sex\":\"male\"}");
			} else if (opType == OpType.DELETE) {
				datas.add("{ \"name\": \"le" + (i + 1) + "\""
					+ ", \"ts\": 1574297893837, \"age\": 16, \"location\": \"beijing\", \"sex\":\"male\"}");
			}
		}
		JavaSparkContext jsc = new JavaSparkContext(spark.sparkContext());
		Dataset<Row> df = spark.sqlContext().read().json(jsc.parallelize(datas, 2));
		return df;
	}

	public static Dataset<Row> getCustomDataset(int num, OpType opType, int age, String location, SparkSession spark) {
		List<String> datas = new ArrayList<>();
		for (int i = 0; i < num; i++) {
			if (opType == OpType.INSERT) {
				datas.add("{ \"name\": \"le" + (i + 1) + "\""
					+ ", \"ts\": 1574297893837, \"age\": " + age + ", \"location\": \"beijing\", \"sex\":\"male\", \"date\":\"2020/08/16\"}");
				datas.add("{ \"name\": \"le" + (i + 1) + "\""
					+ ", \"ts\": 1574297893837, \"age\": " + age + ", \"location\": \"shanghai\", \"sex\":\"male\", \"date\":\"2020/08/15\"}");
			} else if (opType == OpType.UPDATE) {
				datas.add("{ \"name\": \"le" + (i + 1) + "\""
					+ ", \"ts\": 1574297893837, \"age\": " + age + ", \"location\": \"" + location + "\", \"sex\":\"male\", \"date\":\"2020/08/16\"}");
			} else if (opType == OpType.APPEND) {
				datas.add("{ \"name\": \"lee" + (i + 1) + "\""
					+ ", \"ts\": 1574297893837, \"age\": " + age + ", \"location\": \"shanghai\", \"sex\":\"male\"}");
			} else if (opType == OpType.DELETE) {
				datas.add("{ \"name\": \"le" + (i + 1) + "\""
					+ ", \"ts\": 1574297893837, \"age\": " + age + ", \"location\": \"beijing\", \"sex\":\"male\"}");
			}
		}
		JavaSparkContext jsc = new JavaSparkContext(spark.sparkContext());
		Dataset<Row> df = spark.sqlContext().read().json(jsc.parallelize(datas, 2));
		return df;
	}
}
