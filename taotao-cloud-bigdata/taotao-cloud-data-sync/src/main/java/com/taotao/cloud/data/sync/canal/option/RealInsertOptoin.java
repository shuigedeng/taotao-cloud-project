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
import com.taotao.boot.canal.option.InsertOption;
import com.taotao.boot.common.utils.log.LogUtils;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 真正的插入数据操作
 *
 * @author shuigedeng
 * @version 2022.04 1.0.0
 * @since 2021/8/31 09:05
 */
@Component
public class RealInsertOptoin extends InsertOption {

    // @Autowired
    // private Mapper mapper;
    //

    @Override
    public void doOption(
            String destination,
            String schemaName,
            String tableName,
            CanalEntry.RowChange rowChange) {
        LogUtils.info("======================接口方式（新增数据操作）==========================");
        List<CanalEntry.RowData> rowDatasList = rowChange.getRowDatasList();
        for (CanalEntry.RowData rowData : rowDatasList) {

            String sql = "use " + schemaName + ";\n";
            StringBuffer colums = new StringBuffer();
            StringBuffer values = new StringBuffer();
            rowData.getAfterColumnsList()
                    .forEach(
                            (c) -> {
                                colums.append(c.getName() + ",");
                                values.append("'" + c.getValue() + "',");
                            });

            sql +=
                    "INSERT INTO "
                            + tableName
                            + "("
                            + colums.substring(0, colums.length() - 1)
                            + ") VALUES("
                            + values.substring(0, values.length() - 1)
                            + ");";
            LogUtils.info(sql);
            // mapper.doOption(sql);

        }
        LogUtils.info("\n======================================================");
    }
}
