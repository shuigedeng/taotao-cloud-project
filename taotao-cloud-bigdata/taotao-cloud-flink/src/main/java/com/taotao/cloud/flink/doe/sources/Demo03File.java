package com.taotao.cloud.flink.doe.sources;


import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.connector.file.src.FileSource;
import org.apache.flink.connector.file.src.reader.TextLineInputFormat;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @Date: 2023/12/27
 * @Author: Hang.Nian.YY
 * @WX: 17710299606
 * @Tips: 学大数据 ,到多易教育
 * @DOC: https://blog.csdn.net/qq_37933018?spm=1000.2115.3001.5343
 * @Description: 基于文件的source   批处理 加载文件
 * 在实时场景下很少
 */
public class Demo03File {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.setInteger("rest.port", 8888);
        StreamExecutionEnvironment see = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);

        TextLineInputFormat textLineInputFormat = new TextLineInputFormat();
        Path path = new Path("data/csv");
        // 文件source
        FileSource<String> fileSource = FileSource.forRecordStreamFormat(textLineInputFormat, path).build();
        /**
         * 基于文件source  创DS
         * fileSource  文件的路径 加载数据的方式
         * 参数2 水位线  ,处理的数据. 有时候按照数据的时间来进行计算 , 需要水位线配合
         * 参数3  数据源名字
         */
        DataStreamSource<String> ds = see.fromSource(fileSource, WatermarkStrategy.noWatermarks(), "file-source");

        ds.print() ;

        see.execute() ;


    }
}
