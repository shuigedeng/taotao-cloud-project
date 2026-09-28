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
 * @since : 08/26/2020
 */
public class MultiReaderService2 implements ItemReader<String> {

    private int count = 0;

    @Override
    public String read() throws Exception {
        if (MultiProcessorService1.message != null
                && count < MultiProcessorService1.message.length) {
            return MultiProcessorService1.message[count++];
        }
        count = 0;
        return null;
    }
}
