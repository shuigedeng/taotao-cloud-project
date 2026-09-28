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

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

/**
 * @author : dylanz
 * @since : 08/25/2020
 */
public class MultiWriterService implements ItemWriter<String> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // 在此处进行数据输出操作，如写入数据库、写入文件、打印log等，本例为打印log；
    public void write(List<? extends String> messages) throws Exception {
        for (String message : messages) {
            logger.info("Writing data: " + message);
        }
    }

    @Override
    public void write(Chunk<? extends String> chunk) throws Exception {}
}
