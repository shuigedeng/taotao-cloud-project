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

import com.mysql.cj.jdbc.MysqlXADataSource;
import com.taotao.cloud.flink.doe.beans.HeroBean;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.XADataSource;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.connector.jdbc.*;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.util.function.SerializableSupplier;

/**
 * @since: 2023/12/30
 * @Author: Hang.Nian.YY
 * @WX: 17710299606
 * @Tips: 学大数据 ,到多易教育
 * @DOC: https://blog.csdn.net/qq_37933018?spm=1000.2115.3001.5343
 * @Description:
 */
public class Sink05ExcatlyOnce {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.set("rest.port", 8888);
        StreamExecutionEnvironment see =
                StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);
        // 1 获取数据
        DataStreamSource<String> ds = see.socketTextStream("doe01", 8899);
        // 保证开始chkp
        see.enableCheckpointing(1000);
        // 2 处理数据  bean
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

        // 1 执行的sql
        String sql =
                "insert  into tb_hero values(?,?,?)  on duplicate key update  name = ? ,combat_value  = ?";
        // 2 对sql预编译
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
        // 3 执行参数
        JdbcExecutionOptions executionOptions =
                JdbcExecutionOptions.builder().withMaxRetries(0).withBatchIntervalMs(1000).build();

        // 4
        JdbcExactlyOnceOptions exactlyOnceOptions =
                JdbcExactlyOnceOptions.builder()
                        // 每个连接内部是否需求事务
                        .withTransactionPerConnection(true)
                        .withRecoveredAndRollback(true)
                        .withMaxCommitAttempts(10)
                        .build();
        // 5 支持分布式事务的数据源

        SerializableSupplier<XADataSource> serializableSupplier =
                new SerializableSupplier<XADataSource>() {
                    @Override
                    public XADataSource get() {
                        MysqlXADataSource mysqlXADataSource = new MysqlXADataSource();
                        mysqlXADataSource.setUrl("jdbc:mysql://127.0.0.1:3306/doe44");
                        mysqlXADataSource.setPassword("root");
                        mysqlXADataSource.setUser("root");
                        return mysqlXADataSource;
                    }
                };

        /**
         * sink的时候保证数据的精确处理一次
         * exactlyOnceSink
         * 参数1  sql语句
         * 参数2 sql执行对象  预编译sql
         * 参数3 执行参数
         * 参数4 数据精确处理一次的参数
         * 参数5 支持分布式事务的数据源  数据库的连接信息
         *
         */
        SinkFunction<HeroBean> sink =
                JdbcSink.exactlyOnceSink(
                        sql,
                        jdbcStatementBuilder,
                        executionOptions,
                        exactlyOnceOptions,
                        serializableSupplier);

        beans.addSink(sink);

        // 4 执行
        see.execute();
    }
}
