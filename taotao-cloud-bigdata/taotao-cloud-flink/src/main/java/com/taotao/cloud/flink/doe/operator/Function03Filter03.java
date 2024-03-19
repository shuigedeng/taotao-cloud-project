package com.taotao.cloud.flink.doe.operator;


import com.taotao.cloud.flink.doe.beans.HeroBean;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @Date: 2023/12/28
 * @Author: Hang.Nian.YY
 * @WX: 17710299606
 * @Tips: 学大数据 ,到多易教育
 * @DOC: https://blog.csdn.net/qq_37933018?spm=1000.2115.3001.5343
 * @Description:
 */
public class Function03Filter03 {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.setInteger("rest.port", 8888);
        StreamExecutionEnvironment see = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);

        DataStreamSource<String> ds = see.socketTextStream("doe01", 8899);

        SingleOutputStreamOperator<HeroBean> beans = ds.map(line -> {
            try {
                String[] arr = line.split(",");
                int id = Integer.parseInt(arr[0]);
                String name = arr[1];
                double combatValue = Double.parseDouble(arr[2]);
                HeroBean heroBean = new HeroBean(id, name, combatValue);
                return heroBean;
            } catch (Exception e) {
                return new HeroBean();
            }
        }).returns(TypeInformation.of(new TypeHint<HeroBean>() {
        }));

        SingleOutputStreamOperator<HeroBean> res = beans.filter(bean -> bean.getCombatValue() > 90) ;

        res.print();
        see.execute();


    }
}
