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

import java.util.UUID;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

/**
 * @since: 2023/12/28
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
        while (true) {
            String str = UUID.randomUUID().toString();
            ctx.collect(str);
            Thread.sleep(3000);
        }
    }

    /**
     * 取消生成数据的方法
     */
    @Override
    public void cancel() {}
}
