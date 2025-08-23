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

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.connector.file.src.FileSource;
import org.apache.flink.connector.file.src.reader.TextLineInputFormat;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @since: 2023/12/27
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
        conf.set("rest.port", 8888);
        StreamExecutionEnvironment see =
                StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);

        TextLineInputFormat textLineInputFormat = new TextLineInputFormat();
        Path path = new Path("data/csv");
        // 文件source
        FileSource<String> fileSource =
                FileSource.forRecordStreamFormat(textLineInputFormat, path).build();
        /**
         * 基于文件source  创DS
         * fileSource  文件的路径 加载数据的方式
         * 参数2 水位线  ,处理的数据. 有时候按照数据的时间来进行计算 , 需要水位线配合
         * 参数3  数据源名字
         */
        DataStreamSource<String> ds =
                see.fromSource(fileSource, WatermarkStrategy.noWatermarks(), "file-source");

        ds.print();

        see.execute();
    }
}
