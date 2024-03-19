package com.taotao.cloud.flink.doe.operator;


import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * @Date: 2023/12/28
 * @Author: Hang.Nian.YY
 * @WX: 17710299606
 * @Tips: 学大数据 ,到多易教育
 * @DOC: https://blog.csdn.net/qq_37933018?spm=1000.2115.3001.5343
 * @Description: 每摄入一个元素  执行一次   扁平化
 * 返回值  0 1 多个  大部分返回多个结果数据
 */
public class Function02FlatMap02 {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.setInteger("rest.port", 8888);
        StreamExecutionEnvironment see = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);
        see.setParallelism(1);

        DataStreamSource<String> lines = see.socketTextStream("doe01", 8899);
        /**
         * lambda 表达式在实现多个参数的抽象方法时, 声明参数的数据类型
         * 最好都声明返回数据类型
         *
         */
        lines.flatMap((String line, Collector<String> out) -> {
            try {
                String[] words = line.split("\\s+");
                for (String word : words) {
                    out.collect(word);
                }
            } catch (Exception e) {

            }
        }).returns(TypeInformation.of(new TypeHint<String>() {}));

        // scala中
     /*   val f:(Int,Int)=>Int = (x:Int,y:Int)=>{x+y}
        val f:(Int,Int)=>Int = (x,y)=>{x+y}*/

        see.execute() ;

    }


}
