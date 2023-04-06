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

import org.springframework.batch.item.ItemReader;

/**
 * @author : dylanz
 * @since : 08/25/2020
 */
public class MultiReaderService1 implements ItemReader<String> {

    // 在此处进行数据读取操作，如从数据库查询、从文件中读取、从变量中读取等，本例从变量中读取；
    private String[] message = {"message 1", "message 2", "message 3", "message 4", "message 5"};
    private int count = 0;

    @Override
    public String read() throws Exception {
        if (count < message.length) {
            return message[count++];
        }
        count = 0;
        return null;
    }
}
