package com.taotao.cloud.flink.doe.sources;


import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.UUID;

/**
 * @Date: 2023/12/28
 * @Author: Hang.Nian.YY
 * @WX: 17710299606
 * @Tips: 学大数据 ,到多易教育
 * @DOC: https://blog.csdn.net/qq_37933018?spm=1000.2115.3001.5343
 * @Description:
 */
public class MyCustomSource implements SourceFunction<String> {
    /**
     * 生成数据的逻辑方法
     */
    @Override
    public void run(SourceContext<String> ctx) throws Exception {
        // 生成数据   将数据由ctx 管理
        while (true){
            String str = UUID.randomUUID().toString();
            ctx.collect(str);
            Thread.sleep(3000);
        }
    }

    /**
     * 取消生成数据的方法
     */
    @Override
    public void cancel() {

    }
}
