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

package com.taotao.cloud.realtime.datalake.mall.app.dws;

import com.taotao.cloud.realtime.mall.app.func.KeywordUDTF;
import com.taotao.cloud.realtime.mall.bean.KeywordStats;
import com.taotao.cloud.realtime.mall.common.GmallConstant;
import com.taotao.cloud.realtime.mall.utils.ClickHouseUtil;
import com.taotao.cloud.realtime.mall.utils.MyKafkaUtil;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * Date: 2021/2/26 Desc: dWS层 搜索关键字
 */
public class KeywordStatsApp {

    public static void main(String[] args) throws Exception {
        // TODO 1.基本环境准备
        // 1.1 创建Flink流式处理环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 1.2 设置并行度
        env.setParallelism(4);
        /*
        //1.3 检查点CK相关设置
        env.enableCheckpointing(5000, CheckpointingMode.AT_LEAST_ONCE);
        env.getCheckpointConfig().setCheckpointTimeout(60000);
        StateBackend fsStateBackend = new FsStateBackend(
                "hdfs://hadoop202:8020/gmall/flink/checkpoint/ProductStatsApp");
        env.setStateBackend(fsStateBackend);
        System.setProperty("HADOOP_USER_NAME","atguigu");
        */
        // 1.4 创建Table环境
        EnvironmentSettings setting = EnvironmentSettings.newInstance().inStreamingMode().build();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env, setting);

        // TODO 2.注册自定义函数
        tableEnv.createTemporarySystemFunction("ik_analyze", KeywordUDTF.class);

        // TODO 3.创建动态表
        // 3.1 声明主题以及消费者组
        String pageViewSourceTopic = "dwd_page_log";
        String groupId = "keywordstats_app_group";
        // 3.2建表
        tableEnv.executeSql(
                "CREATE TABLE page_view ("
                        + " common MAP<STRING, STRING>,"
                        + " page MAP<STRING, STRING>,"
                        + " ts BIGINT,"
                        + " rowtime as TO_TIMESTAMP(FROM_UNIXTIME(ts/1000,'yyyy-MM-dd HH:mm:ss')),"
                        + " WATERMARK FOR rowtime AS rowtime - INTERVAL '2' SECOND) "
                        + " WITH ("
                        + MyKafkaUtil.getKafkaDDL(pageViewSourceTopic, groupId)
                        + ")");
        // TODO 4.从动态表中查询数据  --->尚硅谷大数据数仓-> [尚, 硅谷, 大, 数据, 数, 仓]
        Table fullwordTable =
                tableEnv.sqlQuery(
                        "select page['item'] fullword,rowtime "
                                + " from page_view "
                                + " where page['page_id']='good_list' and page['item'] IS NOT NULL");
        // TODO 5.利用自定义函数  对搜索关键词进行拆分
        Table keywordTable =
                tableEnv.sqlQuery(
                        "SELECT keyword, rowtime "
                                + "FROM  "
                                + fullwordTable
                                + ","
                                + "LATERAL TABLE(ik_analyze(fullword)) AS t(keyword)");
        // TODO 6.分组、开窗、聚合
        Table reduceTable =
                tableEnv.sqlQuery(
                        "select keyword,count(*) ct,  '"
                                + GmallConstant.KEYWORD_SEARCH
                                + "' source,"
                                + "DATE_FORMAT(TUMBLE_START(rowtime, INTERVAL '10' SECOND),'yyyy-MM-dd HH:mm:ss') stt,"
                                + "DATE_FORMAT(TUMBLE_END(rowtime, INTERVAL '10' SECOND),'yyyy-MM-dd HH:mm:ss') edt ,"
                                + "UNIX_TIMESTAMP()*1000 ts from "
                                + keywordTable
                                + " group by TUMBLE(rowtime, INTERVAL '10' SECOND),keyword");

        // TODO 7.转换为流
        DataStream<KeywordStats> keywordStatsDS =
                tableEnv.toAppendStream(reduceTable, KeywordStats.class);

        keywordStatsDS.print(">>>>");

        // TODO 8.写入到ClickHouse
        keywordStatsDS.addSink(
                ClickHouseUtil.getJdbcSink(
                        "insert into keyword_stats_0820(keyword,ct,source,stt,edt,ts) values(?,?,?,?,?,?)"));

        env.execute();
    }
}
