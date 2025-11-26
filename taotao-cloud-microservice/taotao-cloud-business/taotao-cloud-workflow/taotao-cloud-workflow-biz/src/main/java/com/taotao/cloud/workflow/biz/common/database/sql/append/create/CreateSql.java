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

package com.taotao.cloud.workflow.biz.common.database.sql.append.create;

import com.taotao.cloud.workflow.biz.common.database.model.DbTableFieldModel;
import com.taotao.cloud.workflow.biz.common.database.model.dto.PreparedStatementDTO;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import com.taotao.boot.common.utils.lang.StringUtils;
import org.springframework.web.util.HtmlUtils;

/** 添表SQL模板 */
@Slf4j
public class CreateSql {

    /** 获取 - SQL */
    public static List<PreparedStatementDTO> getCreTabSql(CreateSqlDTO createSqlDTO) throws Exception {
        List<PreparedStatementDTO> PSDList = new LinkedList<>();
        // 字段添加
        PSDList.add(getFieldsPSD(createSqlDTO));
        // 注释添加
        PSDList.addAll(getComments(createSqlDTO));
        return PSDList;
    }

    /*============================ 字段 ================================*/

    /** 装配 - 字段集合 */
    private static PreparedStatementDTO getFieldsPSD(CreateSqlDTO createSqlDTO) throws Exception {
        PreparedStatementDTO dto = new PreparedStatementDTO(null, "", new LinkedList<>());
        // 新增表，初始字段不能为空
        List<DbTableFieldModel> fieldModels = createSqlDTO.getFieldModels();
        if (CollectionUtils.isNotEmpty(fieldModels)) {
            for (DbTableFieldModel fieldModel : fieldModels) {
                // 类型转换 (fieldName即是fieldComment);
                fieldsSqlFrame(createSqlDTO.getDbBase(), fieldModel, dto);
            }
            // 去除最后一个逗号
            String preparedSql = dto.getPrepareSql();
            /* ==================框架======================
             * 框架 - 基础
             * 基本框架：CREATE TABLE + {表名} + ({字段集合})
             */
            // 表名无法用?占位符的方式防止SQL注入，使用过滤的方法了
            String tableName = HtmlUtils.htmlEscape(String.valueOf(createSqlDTO.getNewTable()), CharsetKit.UTF_8);
            dto.setPrepareSql(
                    "CREATE TABLE " + tableName + " ( " + preparedSql.substring(0, preparedSql.length() - 1) + " ) ");
        } else {
            throw new Exception("没有初始字段");
        }
        return dto;
    }

    /** 装配 - 单个字段 */
    private static void fieldsSqlFrame(DbBase dbBase, DbTableFieldModel fieldModel, PreparedStatementDTO dto)
            throws Exception {
        // 防止sql注入的参数
        String field = fieldModel.getField();

        // 不会被sql注入的参数
        String dataLength = fieldModel.getDataLength(); // 有做纯数字类型判断
        String dataType = fieldModel.getDataType();
        Integer allowNull = fieldModel.getAllowNull();
        Integer primaryKey = fieldModel.getPrimaryKey();

        // 数据类型格式化
        dataType = dataTypeFormat(dataType, dataLength, primaryKey, dbBase);

        // 允空
        String notNull = DbAliasEnum.ALLOW_NULL.isFalse().equals(allowNull) ? " NOT NULL " : "";
        // 主键
        String primaryKeySqlFragment = DbAliasEnum.PRIMARY_KEY.isTrue().equals(primaryKey) ? " PRIMARY KEY " : "";
        /* ===============字段字段===================
         * 字段：{字段名} + {字段类型} + ({字段长度} + {非空限定} + {主键判断} + [{mysql注释}],
         * @return 单个字段SQL
         */
        // 这个地方会出现关键字问题比如field为name
        // 字段名无法用?占位符的方式防止SQL注入，使用过滤的方法了
        field = HtmlUtils.htmlEscape(String.valueOf(field), CharsetKit.UTF_8);
        String sqlFragment = field + " " + dataType + " " + notNull + " " + primaryKeySqlFragment + ",";

        // 准备参数及SQL语句
        dto.setPrepareSql(dto.getPrepareSql() + sqlFragment);
        LinkedList<Object> prepareDataList = dto.getPrepareDataList();
        dto.setPrepareDataList(prepareDataList);
    }

    /** 设置 - 类型 */
    public static String dataTypeFormat(String dataType, String dataLength, Integer primaryKey, DbBase dbBase)
            throws DataException {
        // 获取数据库指定类型
        DataTypeModel dataTypeModel = DataTypeEnum.getDataTypeModel(dataType, dbBase);
        if (dataTypeModel != null) {
            // MySQL独有,varchar作为主键的特殊处理
            dataTypeModel = SqlUtil.getMysqlDataTypeModel(dbBase, primaryKey, dataType, dataTypeModel);
            return getDataType(dataLength, dataTypeModel);
        } else {
            throw new DataException("数据类型不存在");
            //                dataType = getOriginDataType(dataLength, dataType);
        }
    }

    /** 设置数据库长度 */
    private static String getDataType(String dataLength, DataTypeModel dataTypeModel) {
        // 长度设置
        if (dataTypeModel.getLengthModifyFlag()) {
            String precision = getPrecision(dataTypeModel, dataLength);
            return dataTypeModel.getDbFieldType() + "(" + dataTypeModel.getCurrentLength() + precision + ")";
        } else {
            return dataTypeModel.getDbFieldType();
        }
    }

    private static String getOriginDataType(String dataLength, String dataType) {
        if (numFlag(dataLength) && Integer.parseInt(dataLength) > 0) {
            return dataType + "(" + dataLength + ")";
        }
        return dataType;
    }

    private static String getPrecision(DataTypeModel dataTypeModel, String dataLength) {
        // 精度处理(","英文逗号作为精度标识)
        if (dataLength.contains(",")) {
            String[] split = dataLength.split(",");
            dataLength = split[0];
            dataTypeModel.setCurrentLength(getLengthInfo(dataTypeModel, dataLength));
            String precision = split[1];
            Integer precisionMax = dataTypeModel.getPrecisionMax();
            Integer defaultPrecision = dataTypeModel.getDefaultPrecision();
            // 存在precisionMax说明此类型可以进行精度设置
            if (precisionMax != null) {
                // 精度为数字
                if (numFlag(precision)
                        || Integer.parseInt(precision) > 0
                        || Integer.parseInt(precision) < precisionMax) {
                    return "," + precision;
                }
                return "," + defaultPrecision;
            }
        } else {
            dataTypeModel.setCurrentLength(getLengthInfo(dataTypeModel, dataLength));
        }
        return "";
    }

    private static Integer getLengthInfo(DataTypeModel dataTypeModel, String dataLength) {
        // 1、长度为空 || 字符串不是整型（无效长度）
        if (StringUtils.isEmpty(dataLength) || !numFlag(dataLength)) {
            // 返回默认值
            return dataTypeModel.getDefaultLength();
        } else {
            int lengthNum = Integer.parseInt(dataLength);
            // 2、长度小于1 || 长度大于最大限制
            if (lengthNum < 1 || lengthNum > dataTypeModel.getLengthMax()) {
                return dataTypeModel.getDefaultLength();
            } else {
                return lengthNum;
            }
        }
    }

    /** 数据类型判断 */
    private static Boolean numFlag(String num) {
        return Pattern.compile("^[-\\+]?[\\d]*$").matcher(num).matches();
    }

    /*============================ 注释 ================================*/

    private static List<PreparedStatementDTO> getComments(CreateSqlDTO createSqlDTO) {
        if (StringUtil.isNotEmpty(createSqlDTO.getTableComment())) {
            // MySQL、SQLServer独有注释方法
            List<PreparedStatementDTO> PSDs = SqlUtil.getMysqlSqlServerComments(createSqlDTO);
            if (PSDs != null) {
                return PSDs;
            } else {
                List<PreparedStatementDTO> PSDList = new ArrayList<>();
                SqlBase sqlBase = createSqlDTO.getDbBase().getSqlBase();
                PSDList.add(sqlBase.getTableCommentPSD(createSqlDTO));
                PSDList.addAll(sqlBase.getFieldCommentPSD(createSqlDTO));
                return PSDList;
            }
        }
        return null;
    }
}
