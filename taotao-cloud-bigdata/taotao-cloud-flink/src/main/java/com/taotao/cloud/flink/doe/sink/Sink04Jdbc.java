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

package com.taotao.cloud.flink.doe.sink;

import com.taotao.cloud.flink.doe.beans.HeroBean;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.connector.jdbc.JdbcStatementBuilder;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

/**
 * @since: 2023/12/30
 * @Author: Hang.Nian.YY
 * @WX: 17710299606
 * @Tips: 学大数据 ,到多易教育
 * @DOC: https://blog.csdn.net/qq_37933018?spm=1000.2115.3001.5343
 * @Description:
 */
public class Sink04Jdbc {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.set("rest.port", 8888);
        StreamExecutionEnvironment see =
                StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);
        // 1 获取数据
        DataStreamSource<String> ds = see.socketTextStream("doe01", 8899);

        SingleOutputStreamOperator<HeroBean> beans =
                ds.map(
                        new MapFunction<String, HeroBean>() {
                            @Override
                            public HeroBean map(String value) throws Exception {
                                HeroBean heroBean = null;
                                try {
                                    String[] split = value.split(",");
                                    int id = Integer.parseInt(split[0]);
                                    String name = split[1];
                                    double v = Double.parseDouble(split[2]);
                                    heroBean = new HeroBean(id, name, v);
                                } catch (Exception e) {
                                    heroBean = new HeroBean();
                                }
                                return heroBean;
                            }
                        });

        // 2 处理数据  bean

        // 3 将结果保存到mysql中
        /**
         * 参数一 :  执行的SQL语句  插入
         *   insert  into tb_name values(?,?,?) duplicate key update  name = ? ,combat_value  = ?
         *    1) 支持幂等写入数据 ,保证主键不重复 on duplicate key update 当主键重复时 ,更新数据
         *    2) 主键重复   插入失败  不建议使用
         * 参数二 : 预编译sql语句, 创建执行sql语句的Statement对象
         * 参数三 :执行参数  批大小  重试次数等
         * 参数四 :和数据库连接相关的信息  url  user  password
         */

        // 执行的sql
        String sql =
                "insert  into tb_hero values(?,?,?)  on duplicate key update  name = ? ,combat_value  = ?";
        // 对sql预编译
        JdbcStatementBuilder<HeroBean> jdbcStatementBuilder =
                new JdbcStatementBuilder<HeroBean>() {
                    // 每摄入一条数据执行一次
                    // 预编译sql语句
                    @Override
                    public void accept(PreparedStatement preparedStatement, HeroBean heroBean)
                            throws SQLException {
                        preparedStatement.setInt(1, heroBean.getId());
                        preparedStatement.setString(2, heroBean.getName());
                        preparedStatement.setDouble(3, heroBean.getCombatValue());
                        // 更新的参数
                        preparedStatement.setString(4, heroBean.getName());
                        preparedStatement.setDouble(5, heroBean.getCombatValue());
                    }
                };
        // 执行参数
        JdbcExecutionOptions executionOptions =
                JdbcExecutionOptions.builder().withMaxRetries(3).withBatchIntervalMs(1000).build();
        // 连接数据库的信息
        JdbcConnectionOptions connectionOptions =
                new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                        .withUrl("jdbc:mysql://127.0.0.1:3306/doe44") // 8  JDBC 使用的URL
                        .withUsername("root")
                        .withPassword("root")
                        //  .withDriverName() 指定连接数据库的类型  驱动类
                        .build();

        SinkFunction<HeroBean> sink =
                JdbcSink.sink(sql, jdbcStatementBuilder, executionOptions, connectionOptions);

        beans.addSink(sink);

        // 4 执行
        see.execute();
    }
}
