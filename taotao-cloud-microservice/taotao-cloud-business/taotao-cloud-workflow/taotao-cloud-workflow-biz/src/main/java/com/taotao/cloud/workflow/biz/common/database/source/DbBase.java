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

package com.taotao.cloud.workflow.biz.common.database.source;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;
import com.taotao.cloud.workflow.biz.common.database.constant.DbConst;
import com.taotao.cloud.workflow.biz.common.database.data.DataSourceContextHolder;
import com.taotao.cloud.workflow.biz.common.database.enums.ParamEnum;
import com.taotao.cloud.workflow.biz.common.database.enums.datatype.interfaces.DtInterface;
import com.taotao.cloud.workflow.biz.common.database.enums.datatype.viewshow.DataTypeEnum;
import com.taotao.cloud.workflow.biz.common.database.model.DataTypeModel;
import com.taotao.cloud.workflow.biz.common.database.model.DbTableFieldModel;
import com.taotao.cloud.workflow.biz.common.database.model.dto.DataSourceDTO;
import com.taotao.cloud.workflow.biz.common.database.model.interfaces.DataSourceMod;
import com.taotao.cloud.workflow.biz.common.database.source.impl.DbDM;
import com.taotao.cloud.workflow.biz.common.database.source.impl.DbKingbase;
import com.taotao.cloud.workflow.biz.common.database.source.impl.DbMySQL;
import com.taotao.cloud.workflow.biz.common.database.source.impl.DbOracle;
import com.taotao.cloud.workflow.biz.common.database.source.impl.DbPostgre;
import com.taotao.cloud.workflow.biz.common.database.source.impl.DbSQLServer;
import com.taotao.cloud.workflow.biz.common.database.sql.SqlBase;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import lombok.Data;

/** 数据库基础模型表 */
@Data
public abstract class DbBase {

    /** WORKFLOW数据库编码标准 */
    public static final String MYSQL = "MySQL";

    public static final String DM = "DM8";
    public static final String KINGBASE_ES = "KingbaseES";
    public static final String ORACLE = "Oracle";
    public static final String POSTGRE_SQL = "PostgreSQL";
    public static final String SQL_SERVER = "SQLServer";

    public static final DbBase[] DB_BASES = {
        new DbMySQL(), new DbSQLServer(), new DbDM(), new DbOracle(), new DbKingbase(), new DbPostgre()
    };

    public static final String[] DB_ENCODES = {MYSQL, DM, KINGBASE_ES, ORACLE, POSTGRE_SQL, SQL_SERVER};

    /** WORKFLOW数据库编码 */
    protected String workflowDbEncode;
    /** MybatisPlus数据库编码 */
    protected DbType mpDbType;
    /** url里数据库标识 */
    protected String connUrlEncode;
    /** 数据库驱动 */
    protected String driver;
    /** 默认端口 */
    protected String defaultPort;
    /** 默认预备url */
    protected String defaultPrepareUrl;
    /** 获取SQL基础模型 */
    protected SqlBase sqlBase;

    /** oracle连接扩展参数 */
    public String oracleParam;

    /** 无参构造 */
    protected DbBase() {
        init();
    }

    /** 初始赋值 */
    protected void init() {}

    /** 数据库对象初始化 指定子类被创建时，需要提供的参数 */
    protected void setInstance(
            String workflowDbEncode,
            DbType mpDbType,
            String defaultPort,
            String connUrlEncode,
            String driver,
            String defaultPrepareUrl,
            SqlBase sqlBase) {
        // 绑定：WORKFLOW数据库编码
        this.workflowDbEncode = workflowDbEncode;
        // 绑定：MybatisPlus数据库编码
        this.mpDbType = mpDbType;
        // 绑定：Url数据库标识
        this.connUrlEncode = connUrlEncode;
        // 绑定：数据库SQL对象类
        this.sqlBase = sqlBase;
        this.driver = driver;
        this.defaultPrepareUrl = defaultPrepareUrl;
        // 默认端口
        this.defaultPort = defaultPort;
    }

    /**
     * 获取数据类型 使用反射方法，抽取所有子类里面的复用方法。
     *
     * @param dte 数据类型枚举
     * @return 数据类型code
     */
    public DataTypeModel getDataTypeModel(DataTypeEnum dte) throws DataException {
        try {
            // DM8 后期要统一改成 DM
            String workflowDbEncode = this.getWorkflowDbEncode().equals(DM) ? "DM" : this.getWorkflowDbEncode();
            Class<DataTypeEnum> clz =
                    (Class<DataTypeEnum>) Class.forName("workflow.database.enums.datatype.viewshow.DataTypeEnum");
            // 方法命名规则：getDt + workflowDbEncode
            Method method = clz.getMethod("getDt" + workflowDbEncode);
            DtInterface dt = (DtInterface) method.invoke(dte, null);
            return dt.getDataTypeModel();
        } catch (Exception e) {
            throw new DataException(e.getMessage());
        }
    }

    /**
     * 获取数据库字段类型模型 通过view字段类型
     *
     * @param dbFieldType 数据库字段类型模型
     * @return 数据库字段类型模型
     */
    public DataTypeModel getDataTypeModel(String dbFieldType) throws Exception {
        // DM8 后期要统一改成 DM
        String workflowDbEncode = this.getWorkflowDbEncode().equals(DM) ? "DM" : this.getWorkflowDbEncode();
        // 数据类型枚举类命名规则：Dt + workflowDbEncode
        Class<DtInterface> clz =
                (Class<DtInterface>) Class.forName("workflow.database.enums.datatype.Dt" + workflowDbEncode);
        Method method = clz.getMethod("values");
        DtInterface[] dataTypes = (DtInterface[]) method.invoke(null, null);
        for (DtInterface dataType : dataTypes) {
            if (dbFieldType.equals(dataType.getDbFieldType())) {
                return dataType.getDataTypeModel();
            }
        }
        return null;
    }

    public static List<String> dynamicAllTableName = Collections.emptyList();

    /**
     * 获取最终动态表名， 处理是否动态表名
     *
     * @return
     */
    public TableNameHandler getDynamicTableNameHandler() {
        return (sql, tableName) -> {
            // 是否租户系统指定数据源
            boolean isAssignDataSource = StringUtil.isNotEmpty(DataSourceContextHolder.getDatasourceName())
                    && "true".equals(DataSourceContextHolder.getDatasourceName());
            if (isAssignDataSource) {
                return tableName;
            } else {
                // 是否指定数据源, 且在初始库中包含的表
                boolean hasDataSource = StringUtil.isNotEmpty(DataSourceContextHolder.getDatasourceName())
                        && dynamicAllTableName.contains(tableName.toLowerCase());
                return hasDataSource ? getDynamicTableName(tableName) : tableName;
            }
        };
    }

    /**
     * 获取动态组合表名
     *
     * @param tableName
     * @return
     */
    protected String getDynamicTableName(String tableName) {
        return DataSourceContextHolder.getDatasourceName() + "." + tableName;
    }

    /**
     * 获取部分字段信息
     *
     * @param result 结果集
     * @return 表字段模型
     * @throws DataException ignore
     */
    public DbTableFieldModel getPartFieldModel(ResultSet result) throws Exception {
        return new DbTableFieldModel();
    }

    /**
     * 获取数据库连接Url 关键参数： 1、地址 2、端口 3、数据库名 4、模式 （参数：?currentSchema = schema） 5、jdbc-url自定义参数
     *
     * <p>此方法对DbTypeUtil与内部开放，对外关闭。外部调用url，用DbTypeUtil.getUrl()方法
     *
     * @return String 连接
     */
    protected String getConnUrl(String prepareUrl, String host, Integer port, String dbName, String schema) {
        // 配置文件是否存在自定义数据连接url
        if (StringUtil.isEmpty(prepareUrl)) {
            prepareUrl = this.defaultPrepareUrl;
        }
        if (StringUtil.isNotEmpty(dbName)) {
            prepareUrl = prepareUrl.replace(DbConst.DB_NAME, dbName);
        }
        // 模式替换
        if (StringUtil.isNotEmpty(schema)) {
            prepareUrl = prepareUrl.replace(DbConst.DB_SCHEMA, schema);
        }
        if (StringUtil.isNotEmpty(host)) {
            prepareUrl = prepareUrl.replace(DbConst.HOST, host);
        }
        if (port != null) {
            prepareUrl = prepareUrl.replace(DbConst.PORT, port.toString());
        }
        return prepareUrl;
    }

    /**
     * 不同数据库结构性
     *
     * @param structParams 结构参数
     * @param table 表
     * @return 转换后SQL语句
     */
    public LinkedList<Object> getStructParams(String structParams, String table, DataSourceMod dbSourceOrDbLink) {
        DataSourceDTO dto = dbSourceOrDbLink.convertDTO();
        LinkedList<Object> data = new LinkedList<>();
        for (String paramStr : structParams.split(":")) {
            if (paramStr.equals(ParamEnum.TABLE.getTarget())) {
                data.add(table);
            } else if (paramStr.equals(ParamEnum.DB_NAME.getTarget())) {
                // 自动库名
                String dbName = dto.getDbName();
                if (dbName == null) {
                    dbName = dto.getUserName();
                }
                data.add(dbName);
            } else if (paramStr.equals(ParamEnum.DB_SCHEMA.getTarget())) {
                data.add(dto.getDbSchema());
            } else if (paramStr.equals(ParamEnum.TABLE_SPACE.getTarget())) {
                data.add(dto.getDbTableSpace());
            }
        }
        return data;
    }

    /*
     * 提供内部封装方法所独有的方法调用
     * 保持全局只有一处显性getUrl,getConn的方法（即在ConnUtil里）======================================
     */

    public static class BaseCommon {
        public static String getDbBaseConnUrl(
                DbBase dbBase, String prepareUrl, String host, Integer port, String dbName, String schema) {
            return dbBase.getConnUrl(prepareUrl, host, port, dbName, schema);
        }
    }
}
