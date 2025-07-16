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

package com.taotao.cloud.hudi.common;

import java.util.ArrayList;
import java.util.List;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

/**
 * A util class to generator data for demos.
 */
public class CustomDataGenerator {

    public static Dataset<Row> getCustomDataset(int num, OpType opType, SparkSession spark) {
        List<String> datas = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            if (opType == OpType.INSERT) {
                datas.add(
                        "{ \"name\": \"le"
                                + (i + 1)
                                + "\""
                                + ", \"ts\": 1574297893837, \"age\": 16, \"location\": \"beijing\", \"sex\":\"male\", \"date\":\"2020/08/16\"}");
                datas.add(
                        "{ \"name\": \"le"
                                + (i + 1)
                                + "\""
                                + ", \"ts\": 1574297893837, \"age\": 16, \"location\": \"shanghai\", \"sex\":\"male\", \"date\":\"2020/08/15\"}");
            } else if (opType == OpType.UPDATE) {
                datas.add(
                        "{ \"name\": \"le"
                                + (i + 1)
                                + "\""
                                + ", \"ts\": 1574297893837, \"age\": 17, \"location\": \"beijing\", \"sex\":\"male\", \"date\":\"2020/08/16\"}");
            } else if (opType == OpType.APPEND) {
                datas.add(
                        "{ \"name\": \"lee"
                                + (i + 1)
                                + "\""
                                + ", \"ts\": 1574297893837, \"age\": 16, \"location\": \"shanghai\", \"sex\":\"male\"}");
            } else if (opType == OpType.DELETE) {
                datas.add(
                        "{ \"name\": \"le"
                                + (i + 1)
                                + "\""
                                + ", \"ts\": 1574297893837, \"age\": 16, \"location\": \"beijing\", \"sex\":\"male\"}");
            }
        }
        JavaSparkContext jsc = new JavaSparkContext(spark.sparkContext());
        return spark.sqlContext().read().json(jsc.parallelize(datas, 2));
    }

    public static Dataset<Row> getCustomDataset(
            int num, OpType opType, int age, String location, SparkSession spark) {
        List<String> datas = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            if (opType == OpType.INSERT) {
                datas.add(
                        "{ \"name\": \"le"
                                + (i + 1)
                                + "\""
                                + ", \"ts\": 1574297893837, \"age\": "
                                + age
                                + ", \"location\": \"beijing\", \"sex\":\"male\", \"date\":\"2020/08/16\"}");
                datas.add(
                        "{ \"name\": \"le"
                                + (i + 1)
                                + "\""
                                + ", \"ts\": 1574297893837, \"age\": "
                                + age
                                + ", \"location\": \"shanghai\", \"sex\":\"male\", \"date\":\"2020/08/15\"}");
            } else if (opType == OpType.UPDATE) {
                datas.add(
                        "{ \"name\": \"le"
                                + (i + 1)
                                + "\""
                                + ", \"ts\": 1574297893837, \"age\": "
                                + age
                                + ", \"location\": \""
                                + location
                                + "\", \"sex\":\"male\", \"date\":\"2020/08/16\"}");
            } else if (opType == OpType.APPEND) {
                datas.add(
                        "{ \"name\": \"lee"
                                + (i + 1)
                                + "\""
                                + ", \"ts\": 1574297893837, \"age\": "
                                + age
                                + ", \"location\": \"shanghai\", \"sex\":\"male\"}");
            } else if (opType == OpType.DELETE) {
                datas.add(
                        "{ \"name\": \"le"
                                + (i + 1)
                                + "\""
                                + ", \"ts\": 1574297893837, \"age\": "
                                + age
                                + ", \"location\": \"beijing\", \"sex\":\"male\"}");
            }
        }
        JavaSparkContext jsc = new JavaSparkContext(spark.sparkContext());
        Dataset<Row> df = spark.sqlContext().read().json(jsc.parallelize(datas, 2));
        return df;
    }
}
