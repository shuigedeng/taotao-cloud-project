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

package com.taotao.cloud.data.sync.canal.option;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.taotao.boot.canal.option.CreateTableOption;
import com.taotao.boot.common.utils.log.LogUtils;
import org.springframework.stereotype.Component;

/**
 * 真正的创建表操作
 *
 * @author shuigedeng
 * @version 2022.04 1.0.0
 * @since 2021/8/31 09:03
 */
@Component
public class RealCreateTableOption extends CreateTableOption {

    @Override
    public void doOption(
            String destination,
            String schemaName,
            String tableName,
            CanalEntry.RowChange rowChange) {
        LogUtils.info("======================接口方式（创建表操作）==========================");
        LogUtils.info("use " + schemaName + ";\n" + rowChange.getSql());
        LogUtils.info("\n======================================================");
    }
}
