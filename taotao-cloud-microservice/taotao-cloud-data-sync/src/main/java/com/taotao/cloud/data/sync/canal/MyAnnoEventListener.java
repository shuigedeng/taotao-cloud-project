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

package com.taotao.cloud.data.sync.canal;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.taotao.boot.canal.annotation.AlertTableListenPoint;
import com.taotao.boot.canal.annotation.CanalEventListener;
import com.taotao.boot.canal.annotation.CreateIndexListenPoint;
import com.taotao.boot.canal.annotation.CreateTableListenPoint;
import com.taotao.boot.canal.annotation.DeleteListenPoint;
import com.taotao.boot.canal.annotation.DropTableListenPoint;
import com.taotao.boot.canal.annotation.InsertListenPoint;
import com.taotao.boot.canal.annotation.UpdateListenPoint;
import com.taotao.boot.canal.model.CanalMsg;
import com.taotao.boot.common.utils.log.LogUtils;
import java.util.List;
import org.springframework.util.CollectionUtils;

/**
 * 注解方式
 *
 * @author shuigedeng
 * @version 2022.04 1.0.0
 * @return
 * @since 2021/8/31 09:08
 */
@CanalEventListener
public class MyAnnoEventListener {

    @InsertListenPoint
    public void onEventInsertData(CanalMsg canalMsg, CanalEntry.RowChange rowChange) {
        LogUtils.info("======================注解方式（新增数据操作）==========================");
        List<CanalEntry.RowData> rowDatasList = rowChange.getRowDatasList();
        for (CanalEntry.RowData rowData : rowDatasList) {
            String sql = "use " + canalMsg.getSchemaName() + ";\n";
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
                            + canalMsg.getTableName()
                            + "("
                            + colums.substring(0, colums.length() - 1)
                            + ") VALUES("
                            + values.substring(0, values.length() - 1)
                            + ");";
            LogUtils.info(sql);
        }
        LogUtils.info("\n======================================================");
    }

    @UpdateListenPoint
    public void onEventUpdateData(CanalMsg canalMsg, CanalEntry.RowChange rowChange) {
        LogUtils.info("======================注解方式（更新数据操作）==========================");
        List<CanalEntry.RowData> rowDatasList = rowChange.getRowDatasList();
        for (CanalEntry.RowData rowData : rowDatasList) {

            String sql = "use " + canalMsg.getSchemaName() + ";\n";
            StringBuffer updates = new StringBuffer();
            StringBuffer conditions = new StringBuffer();
            rowData.getAfterColumnsList()
                    .forEach(
                            (c) -> {
                                if (c.getIsKey()) {
                                    conditions.append(c.getName() + "='" + c.getValue() + "'");
                                } else {
                                    updates.append(c.getName() + "='" + c.getValue() + "',");
                                }
                            });
            sql +=
                    "UPDATE "
                            + canalMsg.getTableName()
                            + " SET "
                            + updates.substring(0, updates.length() - 1)
                            + " WHERE "
                            + conditions;
            LogUtils.info(sql);
        }
        LogUtils.info("\n======================================================");
    }

    @DeleteListenPoint
    public void onEventDeleteData(CanalEntry.RowChange rowChange, CanalMsg canalMsg) {
        LogUtils.info("======================注解方式（删除数据操作）==========================");
        List<CanalEntry.RowData> rowDatasList = rowChange.getRowDatasList();
        for (CanalEntry.RowData rowData : rowDatasList) {

            if (!CollectionUtils.isEmpty(rowData.getBeforeColumnsList())) {
                String sql = "use " + canalMsg.getSchemaName() + ";\n";

                sql += "DELETE FROM " + canalMsg.getTableName() + " WHERE ";
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

    @CreateTableListenPoint
    public void onEventCreateTable(CanalEntry.RowChange rowChange) {
        LogUtils.info("======================注解方式（创建表操作）==========================");
        LogUtils.info("use " + rowChange.getDdlSchemaName() + ";\n" + rowChange.getSql());
        LogUtils.info("\n======================================================");
    }

    @DropTableListenPoint
    public void onEventDropTable(CanalEntry.RowChange rowChange) {
        LogUtils.info("======================注解方式（删除表操作）==========================");
        LogUtils.info("use " + rowChange.getDdlSchemaName() + ";\n" + rowChange.getSql());
        LogUtils.info("\n======================================================");
    }

    @AlertTableListenPoint
    public void onEventAlertTable(CanalEntry.RowChange rowChange) {
        LogUtils.info("======================注解方式（修改表信息操作）==========================");
        LogUtils.info("use " + rowChange.getDdlSchemaName() + ";\n" + rowChange.getSql());
        LogUtils.info("\n======================================================");
    }

    @CreateIndexListenPoint
    public void onEventCreateIndex(CanalMsg canalMsg, CanalEntry.RowChange rowChange) {
        LogUtils.info("======================注解方式（创建索引操作）==========================");
        LogUtils.info("use " + canalMsg.getSchemaName() + ";\n" + rowChange.getSql());
        LogUtils.info("\n======================================================");
    }
}
