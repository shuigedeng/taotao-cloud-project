package com.taotao.cloud.bigdata.flink.doe.sources;


import com.taotao.cloud.bigdata.flink.doe.beans.OrdersBean;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.types.LongValue;
import org.apache.flink.util.LongValueSequenceIterator;

import java.util.Arrays;
import java.util.List;

/**
 * @Date: 2023/12/27
 * @Author: Hang.Nian.YY
 * @WX: 17710299606
 * @Tips: 学大数据 ,到多易教育
 * @DOC: https://blog.csdn.net/qq_37933018?spm=1000.2115.3001.5343
 * @Description:
 */
public class Demo02Collection {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.setInteger("rest.port", 8888);
        StreamExecutionEnvironment see = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);

        //see.fromSequence(1 , 8);


        see.execute();

    }
    /**
     *通过集合 创建一个并行的source
     */
    private static DataStreamSource<LongValue> getLongValueDataStreamSource(StreamExecutionEnvironment see) {
        LongValueSequenceIterator longValueSequenceIterator = new LongValueSequenceIterator(1L, 10L);
        DataStreamSource<LongValue> ds = see.fromParallelCollection(longValueSequenceIterator, LongValue.class);
        System.out.println(ds.getParallelism());
        DataStreamSource<LongValue>  res= ds.setParallelism(2);
        System.out.println(res.getParallelism());
        return res;
    }

    /**
     * 创建测试数据集合并行度为  1
     *
     * @param see
     * @return
     */
    private static DataStreamSource<OrdersBean> getBeanDataStreamSource(StreamExecutionEnvironment see) {
        /**
         *  创建测试数据集合
         *  并行度为  1   不能修改并行度
         */

        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6);
        List<OrdersBean> orders = Arrays.asList(new OrdersBean(1, "zss", "BJ", 99.99, 1000L),
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
    private static DataStreamSource<OrdersBean> getOrdersBeanDataStreamSource(StreamExecutionEnvironment see) {
        /**
         * 测试数据  批处理  元素创建数据源
         * 并行度为  1
         */
        DataStreamSource<String> ds = see.fromElements("zss", "lss", "ww");
        DataStreamSource<OrdersBean> ds2 = see.fromElements(OrdersBean.class,
                new OrdersBean(1, "zss", "BJ", 99.99, 1000L),
                new OrdersBean(2, "lss", "BJ", 99.99, 1000L),
                new OrdersBean(3, "ww", "BJ", 99.99, 1000L),
                new OrdersBean(4, "zl", "BJ", 99.99, 1000L)
        );
        return ds2;
    }
}
