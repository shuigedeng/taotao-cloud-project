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

package com.taotao.cloud.workflow.biz.common.database.model;

import com.taotao.cloud.workflow.biz.common.database.model.interfaces.JdbcGetMod;
import java.sql.ResultSet;
import lombok.Data;
import lombok.experimental.Accessors;

/** 表字段模型 */
@Data
@Accessors(chain = true)
public class DbTableFieldModel extends JdbcGetMod {

    /** 字段名 */
    private String field;

    /** 默认值 */
    private String defaults;

    /** 自增 */
    private String identity;

    /** ==================修改添加相关信息=======================* */

    /** 字段说明 （PS:属性名歧义,但涉及多平台，故内部做处理） */
    private String fieldName;

    /**
     * 修正fieldName作为字段注释
     *
     * @return String
     */
    private String fieldComment;

    /** 说明 (PS:这个字段用来返回，字段名+注释) */
    private String description;

    /** 数据类型 */
    private String dataType;

    /** 数据长度 */
    private String dataLength;

    /** 主键 */
    private Integer primaryKey;

    /** 允许null值 */
    private Integer allowNull;

    @Override
    public void setMod(ModelDTO modelDTO) {
        try {
            ResultSet result = modelDTO.getResultSet();
            DbBase db = modelDTO.getDbBase();
            DbTableFieldModel model = db.getPartFieldModel(result);
            String dbFieldType = result.getString(DbAliasEnum.getAsByDb(db, DbAliasConst.DATA_TYPE));
            DataTypeModel dataTypeModel = db.getDataTypeModel(dbFieldType);

            // 字段类型(会出现一些项目中没有内置的类型)
            String dataType;
            if (dataTypeModel != null) {
                dataType = model.getDataType() != null ? model.getDataType() : dataTypeModel.getViewDataType();
            } else {
                // 返回类型原值
                dataType = dbFieldType;
            }

            // 字段名
            String fieldName = model.getField() != null
                    ? model.getField()
                    : result.getString(DbAliasEnum.getAsByDb(db, DbAliasConst.FIELD_NAME));
            // 字段注释
            String fieldComment = model.getFieldComment() != null
                    ? model.getFieldComment()
                    : result.getString(DbAliasEnum.getAsByDb(db, DbAliasConst.FIELD_COMMENT));
            // 字段默认值
            /*String defaults = model.getDefaults()!=null ? model.getDefaults() :
            result.getString(DbAliasEnum.DEFAULTS.AS());*/

            String dataLength;
            // 字段长度
            if (dataTypeModel != null && !dataTypeModel.getLengthModifyFlag()) {
                dataLength = ViewDataTypeConst.DEFAULT;
            } else {
                dataLength = model.getDataLength() != null
                        ? model.getDataLength()
                        :
                        // 如果不getLong的话，有一些长度会显示类似5E+1的字符串,用getLong不用Int,因为有些长度例如：4294967295
                        String.valueOf(result.getLong(DbAliasEnum.getAsByDb(db, DbAliasConst.DATA_LENGTH)));
                // text的长度默认无法设置
                if ("0".equals(dataLength) || StringUtil.isEmpty(dataLength) || "null".equals(dataLength)) {
                    dataLength = ViewDataTypeConst.DEFAULT;
                }
            }

            // 字段主键
            Integer primaryKey = model.getPrimaryKey() != null
                    ? model.getPrimaryKey()
                    : result.getInt(DbAliasEnum.PRIMARY_KEY.asByDb(db));
            // 字段允空
            Integer allowNull = model.getAllowNull() != null
                    ? model.getAllowNull()
                    : result.getInt(DbAliasEnum.ALLOW_NULL.asByDb(db));

            this.setField(fieldName)
                    .
                    // 早期的前后协议命名comment为fieldName;
                    setFieldName(fieldComment)
                    .
                    /* setDefaults(defaults).*/
                    setDataType(dataType)
                    .setDescription(fieldName + "(" + fieldComment + ")")
                    .setDataLength(dataLength)
                    .setAllowNull(allowNull)
                    .setPrimaryKey(primaryKey);
        } catch (Exception e) {
            LogUtils.error(e);
        }
    }
}
