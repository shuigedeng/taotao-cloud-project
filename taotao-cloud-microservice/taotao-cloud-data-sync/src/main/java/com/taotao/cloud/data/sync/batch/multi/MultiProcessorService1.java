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

package com.taotao.cloud.data.sync.batch.multi;

import java.util.ArrayList;
import java.util.List;
import org.springframework.batch.item.ItemProcessor;

/**
 * @author : dylanz
 * @since : 08/25/2020
 */
public class MultiProcessorService1 implements ItemProcessor<String, String> {

    public static String[] message;

    // 在此处进行数据处理操作，如进行计算、逻辑处理、格式转换等，本例将数据变成全大写数据；
    @Override
    public String process(String data) throws Exception {
        // 存储处理过的数据，可供下一个step使用
        List<String> list = new ArrayList<>();
        if (message != null) {
            for (int i = 0; i < message.length; i++) {
                list.add(message[i]);
            }
        }
        list.add(data.toUpperCase());
        message = list.toArray(new String[list.size()]);
        return data.toUpperCase();
    }
}
