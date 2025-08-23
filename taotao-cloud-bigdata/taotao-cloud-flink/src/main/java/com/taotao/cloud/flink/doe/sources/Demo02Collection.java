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

package com.taotao.cloud.flink.doe.sources;

import com.taotao.cloud.flink.doe.beans.OrdersBean;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.connector.source.SourceReaderContext;
import org.apache.flink.api.connector.source.util.ratelimit.RateLimiterStrategy;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.connector.datagen.source.DataGeneratorSource;
import org.apache.flink.connector.datagen.source.GeneratorFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.types.LongValue;
import org.apache.flink.util.LongValueSequenceIterator;

/**
 * @since: 2023/12/27
 * @Author: Hang.Nian.YY
 * @WX: 17710299606
 * @Tips: 学大数据 ,到多易教育
 * @DOC: https://blog.csdn.net/qq_37933018?spm=1000.2115.3001.5343
 * @Description:
 */
public class Demo02Collection {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        // conf.set("rest.port", 8888);
        conf.set(RestOptions.PORT, 8888);
        StreamExecutionEnvironment see =
                StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);
        see.setParallelism(3);

        /*
         *  DataGeneratorSource(
         *       GeneratorFunction<Long, OUT> generatorFunction
         *      ,long count
         *      ,RateLimiterStrategy rateLimiterStrategy
         *      ,TypeInformation<OUT> typeInfo)
         *   参数说明：
         *      @generatorFunction   ： 指定 GeneratorFunction 实现类(生成数据的具体实现类)
         *      @count               ： 指定输出数据的总行数(如果想一直输出，可以设置为Long.MAX_VALUE)
         *      @rateLimiterStrategy ： 指定发射速率(每秒发射的记录数)
         *      @typeInfo            ： 指定返回值类型
         * */
        DataGeneratorSource<FlinkUser> stringDataGeneratorSource =
                new DataGeneratorSource<>(
                        new GeneratorFunction<Long, FlinkUser>() {
                            // 定义随机数数据生成器
                            public RandomDataGenerator generator;

                            @Override
                            public void open(SourceReaderContext readerContext) throws Exception {
                                generator = new RandomDataGenerator();
                            }

                            @Override
                            public FlinkUser map(Long value) throws Exception {
                                // 使用 随机数数据生成器 来创建 FlinkUser实例
                                return new FlinkUser(
                                        value,
                                        generator.nextHexString(4) // 生成随机的4位字符串
                                        ,
                                        System.currentTimeMillis(),
                                        generator.nextInt(1, 2));
                            }
                        },
                        Long.MAX_VALUE,
                        RateLimiterStrategy.perSecond(3L),
                        TypeInformation.of(FlinkUser.class));

        DataStreamSource<FlinkUser> stringDataStreamSource =
                see.fromSource(
                        stringDataGeneratorSource,
                        WatermarkStrategy.noWatermarks(),
                        "data-generator");

        SingleOutputStreamOperator<FlinkUser> map =
                stringDataStreamSource.map(
                        new RichMapFunction<FlinkUser, FlinkUser>() {
                            @Override
                            public void open(OpenContext openContext) throws Exception {
                                super.open(openContext);
                            }

                            @Override
                            public FlinkUser map(FlinkUser value) throws Exception {
                                return value;
                            }

                            @Override
                            public void close() throws Exception {
                                super.close();
                            }
                        });

        KeyedStream<FlinkUser, Integer> flinkUserIntegerKeyedStream =
                map.keyBy(
                        new KeySelector<FlinkUser, Integer>() {
                            @Override
                            public Integer getKey(FlinkUser value) throws Exception {
                                return value.getGender();
                            }
                        });

        flinkUserIntegerKeyedStream.print();

        see.execute();
    }

    /**
     * 通过集合 创建一个并行的source
     */
    private static DataStreamSource<LongValue> getLongValueDataStreamSource(
            StreamExecutionEnvironment see) {
        LongValueSequenceIterator longValueSequenceIterator =
                new LongValueSequenceIterator(1L, 10L);
        DataStreamSource<LongValue> ds =
                see.fromParallelCollection(longValueSequenceIterator, LongValue.class);
        System.out.println(ds.getParallelism());
        DataStreamSource<LongValue> res = ds.setParallelism(2);
        System.out.println(res.getParallelism());
        return res;
    }

    /**
     * 创建测试数据集合并行度为  1
     *
     * @param see
     * @return
     */
    private static DataStreamSource<OrdersBean> getBeanDataStreamSource(
            StreamExecutionEnvironment see) {
        /**
         *  创建测试数据集合
         *  并行度为  1   不能修改并行度
         */
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6);
        List<OrdersBean> orders =
                Arrays.asList(
                        new OrdersBean(1, "zss", "BJ", 99.99, 1000L),
                        new OrdersBean(2, "lss", "BJ", 99.99, 1000L),
                        new OrdersBean(3, "ww", "BJ", 99.99, 1000L),
                        new OrdersBean(4, "zl", "BJ", 99.99, 1000L));
        DataStreamSource<OrdersBean> ds = see.fromCollection(orders);
        System.out.println(ds.getParallelism());
        //  ds.setParallelism(2) ;
        return ds;
    }

    /**
     * 测试数据元素  创建数据源
     *
     * @param see
     * @return
     */
    private static DataStreamSource<OrdersBean> getOrdersBeanDataStreamSource(
            StreamExecutionEnvironment see) {
        /**
         * 测试数据  批处理  元素创建数据源
         * 并行度为  1
         */
        DataStreamSource<String> ds = see.fromElements("zss", "lss", "ww");
        DataStreamSource<OrdersBean> ds2 =
                see.fromElements(
                        OrdersBean.class,
                        new OrdersBean(1, "zss", "BJ", 99.99, 1000L),
                        new OrdersBean(2, "lss", "BJ", 99.99, 1000L),
                        new OrdersBean(3, "ww", "BJ", 99.99, 1000L),
                        new OrdersBean(4, "zl", "BJ", 99.99, 1000L));
        return ds2;
    }

    public static class FlinkUser {
        public Long id;
        public String name;
        public Integer gender;
        public Long createtime;

        // 一定要提供一个 空参 的构造器(反射的时候要使用)
        public FlinkUser() {}

        public FlinkUser(Long id, String name, Long createtime, Integer gender) {
            this.id = id;
            this.name = name;
            this.createtime = createtime;
            this.gender = gender;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(Long createtime) {
            this.createtime = createtime;
        }

        public Integer getGender() {
            return gender;
        }

        public void setGender(Integer gender) {
            this.gender = gender;
        }

        @Override
        public int hashCode() {
            return this.name.hashCode();
        }

        @Override
        public String toString() {
            return "FlinkUser{"
                    + "id="
                    + id
                    + ", name='"
                    + name
                    + '\''
                    + ", createtime="
                    + createtime
                    + ", gender="
                    + gender
                    + '}';
        }
    }
}
