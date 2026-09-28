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
import com.taotao.boot.canal.option.DeleteOption;
import com.taotao.boot.common.utils.log.LogUtils;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * 真正的删除数据操作
 *
 * @author shuigedeng
 * @version 2022.04 1.0.0
 * @since 2021/8/31 09:05
 */
@Component
public class RealDeleteOption extends DeleteOption {

    @Override
    public void doOption(
            String destination,
            String schemaName,
            String tableName,
            CanalEntry.RowChange rowChange) {
        LogUtils.info("======================接口方式（删除数据操作）==========================");
        List<CanalEntry.RowData> rowDatasList = rowChange.getRowDatasList();
        for (CanalEntry.RowData rowData : rowDatasList) {
            if (!CollectionUtils.isEmpty(rowData.getBeforeColumnsList())) {
                String sql = "use " + schemaName + ";\n";

                sql += "DELETE FROM " + tableName + " WHERE ";
                StringBuffer idKey = new StringBuffer();
                StringBuffer idValue = new StringBuffer();
                for (CanalEntry.Column c : rowData.getBeforeColumnsList()) {
                    if (c.getIsKey()) {
                        idKey.append(c.getName());
                        idValue.append(c.getValue());
                        break;
                    }
                }

                sql += idKey + " =" + idValue + ";";

                LogUtils.info(sql);
            }
            LogUtils.info("\n======================================================");
        }
    }
}
