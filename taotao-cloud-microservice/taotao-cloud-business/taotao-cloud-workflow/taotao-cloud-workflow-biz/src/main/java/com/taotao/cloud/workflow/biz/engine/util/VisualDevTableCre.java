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

package com.taotao.cloud.workflow.biz.engine.util;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.taotao.boot.common.utils.json.JacksonUtils;
import com.taotao.cloud.workflow.biz.common.database.model.DbTableFieldModel;
import com.taotao.cloud.workflow.biz.common.model.FormAllModel;
import com.taotao.cloud.workflow.biz.common.model.FormColumnTableModel;
import com.taotao.cloud.workflow.biz.common.model.FormEnum;
import com.taotao.cloud.workflow.biz.common.model.visiual.TableFields;
import com.taotao.cloud.workflow.biz.common.model.visiual.TableModel;
import com.taotao.cloud.workflow.biz.common.model.visiual.fields.FieLdsModel;
import com.taotao.cloud.workflow.biz.common.util.RandomUtil;
import com.taotao.cloud.workflow.biz.exception.WorkFlowException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** */
@Component
public class VisualDevTableCre {

    @Autowired
    private ServiceAllUtil serviceAllUtil;

    /**
     * 表单赋值tableName
     *
     * @param jsonArray
     * @param tableModels
     */
    public void fieldsTableName(JSONArray jsonArray, List<TableModel> tableModels) {
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            String flowKey = jsonObject.getJSONObject("__config__").getString("flowKey");
            if (FormEnum.card.getMessage().equals(flowKey)
                    || FormEnum.row.getMessage().equals(flowKey)
                    || FormEnum.tab.getMessage().equals(flowKey)
                    || FormEnum.collapse.getMessage().equals(flowKey)
                    || StrUtil.isEmpty(flowKey)) {
                JSONArray childArray = jsonObject.getJSONObject("__config__").getJSONArray("children");
                this.fieldsTableName(childArray, tableModels);
                jsonObject.getJSONObject("__config__").put("children", childArray);
            } else if (FormEnum.table.getMessage().equals(flowKey)) {
                JSONArray childrenList = new JSONArray();
                JSONArray children = jsonObject.getJSONObject("__config__").getJSONArray("children");
                String tableModel = "";
                for (Object child : children) {
                    JSONObject childrenObject = (JSONObject) child;
                    this.fieldsModel(childrenObject, tableModels);
                    if (StrUtil.isEmpty(tableModel)) {
                        tableModel = childrenObject.getJSONObject("__config__").getString("relationTable");
                    }
                    childrenList.add(childrenObject);
                }
                jsonObject.getJSONObject("__config__").put("tableName", tableModel);
                jsonObject.getJSONObject("__config__").put("children", childrenList);
            } else {
                this.fieldsModel(jsonObject, tableModels);
            }
        }
    }

    /**
     * 赋值table
     *
     * @param jsonObject
     * @param tableModels
     */
    private TableModel fieldsModel(JSONObject jsonObject, List<TableModel> tableModels) {
        String vModel = jsonObject.getString("__vModel__");
        String relationField =
                StrUtil.isNotEmpty(jsonObject.getString("relationField")) ? jsonObject.getString("relationField") : "";
        String flowKey = jsonObject.getJSONObject("__config__").getString("flowKey");
        TableModel tableName = tableModels.stream()
                .filter(t -> "1".equals(t.getTypeId()))
                .findFirst()
                .orElse(null);
        if (tableName != null) {
            jsonObject.getJSONObject("__config__").put("tableName", tableName.getTable());
        }
        List<TableModel> childTableAll =
                tableModels.stream().filter(t -> "0".equals(t.getTypeId())).toList();
        TableModel childTableaa = childTableAll.stream()
                .filter(t -> t.getFields().stream().anyMatch(k -> k.getField().equals(vModel)))
                .findFirst()
                .orElse(null);
        if (childTableaa != null) {
            jsonObject.getJSONObject("__config__").put("relationTable", childTableaa.getTable());
        }
        if (FormEnum.relationFormAttr.getMessage().equals(flowKey)
                || FormEnum.popupAttr.getMessage().equals(flowKey)) {
            if (StrUtil.isNotEmpty(relationField)) {
                boolean isSubTable = jsonObject.getJSONObject("__config__").getBoolean("isSubTable") != null
                        ? jsonObject.getJSONObject("__config__").getBoolean("isSubTable")
                        : false;
                String model = relationField.split("_flowTable_")[0];
                jsonObject.put(
                        "relationField", model + "_flowTable_" + tableName.getTable() + (isSubTable ? "0" : "1"));
            }
        }
        return childTableaa;
    }

    /**
     * 创建表
     *
     * @param formAllModel
     * @return
     */
    public List<TableModel> tableList(JSONArray jsonArray, List<FormAllModel> formAllModel, String table, String linkId)
            throws WorkFlowException {
        List<TableModel> tableModelList = new LinkedList<>();
        Map<String, String> tableNameList = new HashMap<>();
        try {
            List<DbTableFieldModel> fieldList = new ArrayList<>();
            Map<String, List<DbTableFieldModel>> tableListAll = new HashMap<>();
            Map<String, String> tableNameAll = new HashMap<>();
            for (FormAllModel model : formAllModel) {
                if (FormEnum.mast.getMessage().equals(model.getFlowKey())) {
                    FieLdsModel fieLdsModel = model.getFormColumnModel().getFieLdsModel();
                    this.fieldList(fieLdsModel, table, fieldList);
                    tableNameAll.put(fieLdsModel.getVModel(), table);
                } else if (FormEnum.table.getMessage().equals(model.getFlowKey())) {
                    String tableName = "table" + RandomUtil.enUuid();
                    FormColumnTableModel fieLdsModel = model.getChildList();
                    List<DbTableFieldModel> tableList = new ArrayList<>();
                    String tableModel = fieLdsModel.getTableModel();
                    List<FieLdsModel> fieldsList = fieLdsModel.getChildList().stream()
                            .map(t -> t.getFieLdsModel())
                            .toList();
                    for (FieLdsModel tableFieLdsModel : fieldsList) {
                        this.fieldList(tableFieLdsModel, tableName, tableList);
                        tableNameAll.put(tableFieLdsModel.getVModel(), tableName);
                    }
                    this.dbTableField(tableList, true);
                    tableNameList.put(tableModel, tableName);
                    tableListAll.put(tableModel, tableList);
                }
            }
            this.dbTableField(fieldList, false);
            // 创建子表
            for (String key : tableListAll.keySet()) {
                String tableName = tableNameList.get(key);
                List<DbTableFieldModel> datableList = tableListAll.get(key);
                this.tableModel(tableModelList, datableList, tableName, table, true);
                DbTableCreate dbTable = this.dbTable(linkId, tableName, datableList, true);
                serviceAllUtil.createTable(dbTable);
            }
            this.tableModel(tableModelList, fieldList, table, table, false);
            DbTableCreate dbTable = this.dbTable(linkId, table, fieldList, false);
            serviceAllUtil.createTable(dbTable);
            this.fieldsTableName(jsonArray, tableModelList);
        } catch (Exception e) {
            throw new WorkFlowException(e.getMessage());
        }
        return tableModelList;
    }

    /**
     * 获取表单字段
     *
     * @param fieLdsModel
     * @param tableList
     */
    private void fieldList(FieLdsModel fieLdsModel, String table, List<DbTableFieldModel> tableList) {
        String vmodel = fieLdsModel.getVModel();
        String lable = fieLdsModel.getConfig().getLabel();
        String flowKey = fieLdsModel.getConfig().getFlowKey();
        fieLdsModel.getConfig().setTableName(table);
        if (StrUtil.isNotEmpty(vmodel)) {
            DbTableFieldModel fieldForm = new DbTableFieldModel();
            fieldForm.setAllowNull(1);
            fieldForm.setDataType("varchar");
            fieldForm.setDataLength("255");
            fieldForm.setPrimaryKey(0);
            if (StrUtil.isNotEmpty(fieLdsModel.getVModel())) {
                if (FlowKeyConsts.UPLOADIMG.equals(flowKey)
                        || FlowKeyConsts.UPLOADFZ.equals(flowKey)
                        || FlowKeyConsts.ADDRESS.equals(flowKey)) {
                    fieldForm.setDataType("text");
                }
                if (FlowKeyConsts.MODIFYTIME.equals(flowKey)
                        || FlowKeyConsts.CREATETIME.equals(flowKey)
                        || FlowKeyConsts.DATE.equals(flowKey)) {
                    fieldForm.setDataType("datetime");
                }
                if (FlowKeyConsts.NUM_INPUT.equals(flowKey)) {
                    fieldForm.setDataType("decimal");
                }
                fieldForm.setField(vmodel);
                fieldForm.setFieldName(lable);
                tableList.add(fieldForm);
            }
        }
    }

    /**
     * 创建主外键字段
     *
     * @param tableList
     * @param isforeign
     */
    private void dbTableField(List<DbTableFieldModel> tableList, boolean isforeign) {
        DbTableFieldModel tableKey = new DbTableFieldModel();
        tableKey.setAllowNull(0);
        tableKey.setDataType("varchar");
        tableKey.setDataLength("50");
        tableKey.setPrimaryKey(1);
        tableKey.setField("f_id");
        tableKey.setFieldName("主键");
        tableList.add(tableKey);
        if (isforeign) {
            DbTableFieldModel tableForeignKey = new DbTableFieldModel();
            tableForeignKey.setAllowNull(1);
            tableForeignKey.setDataType("varchar");
            tableForeignKey.setDataLength("50");
            tableForeignKey.setPrimaryKey(0);
            tableForeignKey.setField("f_foreignId");
            tableForeignKey.setFieldName("外键");
            tableList.add(tableForeignKey);
        }
    }

    /**
     * 组装字段list
     *
     * @param tableModelList
     * @param dbtable
     * @param table
     * @param mastTable
     * @param isforeign
     */
    private void tableModel(
            List<TableModel> tableModelList,
            List<DbTableFieldModel> dbtable,
            String table,
            String mastTable,
            boolean isforeign) {
        TableModel tableModel = new TableModel();
        tableModel.setRelationField(isforeign ? "f_id" : "");
        tableModel.setRelationTable(isforeign ? mastTable : "");
        tableModel.setTable(table);
        tableModel.setTableName(isforeign ? "子表" : "主表");
        tableModel.setTableField(isforeign ? "f_foreignId" : "");
        tableModel.setTypeId(isforeign ? "0" : "1");
        tableModel.setFields(JacksonUtils.toList(dbtable, TableFields.class));
        tableModelList.add(tableModel);
    }

    /**
     * 组装创表字段
     *
     * @param linkId
     * @param tableName
     * @param tableFieldList
     * @param isforeign
     * @return
     */
    private DbTableCreate dbTable(
            String linkId, String tableName, List<DbTableFieldModel> tableFieldList, boolean isforeign) {
        DbTableCreate dbTable = new DbTableCreate();
        dbTable.setDbLinkId(linkId);
        dbTable.setNewTable(tableName);
        dbTable.setDbTableFieldModelList(tableFieldList);
        dbTable.setTableComment(isforeign ? "子表" : "主表");
        return dbTable;
    }
}
