package com.taotao.cloud.bigdata.hadoop.atguigu.mapreduce.a10_reduceJoin;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class TableReducer extends Reducer<Text, TableBean,TableBean, NullWritable> {

    @Override
    protected void reduce(Text key, Iterable<TableBean> values, Context context) throws IOException, InterruptedException {
//        01 	1001	1   order
//        01 	1004	4   order
//        01	小米   	     pd
        // 准备初始化集合
        ArrayList<TableBean> orderBeans = new ArrayList<>();
        TableBean pdBean = new TableBean();

        // 循环遍历
        for (TableBean value : values) {

            if ("order".equals(value.getFlag())){// 订单表

                TableBean tmptableBean = new TableBean();

                try {
                    BeanUtils.copyProperties(tmptableBean,value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

                orderBeans.add(tmptableBean);
            }else {// 商品表

                try {
                    BeanUtils.copyProperties(pdBean,value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        // 循环遍历orderBeans，赋值 pdname
        for (TableBean orderBean : orderBeans) {

            orderBean.setPname(pdBean.getPname());

            context.write(orderBean,NullWritable.get());
        }
    }
}
