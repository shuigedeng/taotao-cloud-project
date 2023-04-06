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

package com.taotao.cloud.workflow.biz.common.database.source.impl;

import com.baomidou.mybatisplus.annotation.DbType;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;

/** Oracle模型 */
public class DbOracle extends DbBase {

    /*================Oracle4种数据库连接url=================*/
    /** SID 或：jdbc:oracle:thin:@{host}:{port}/{SID} */
    private static final String SID_URL = "jdbc:oracle:thin:@{host}:{port}:{schema}";

    private static final String SID_SIGN = "SID";
    /** SERVICE：服务名 */
    private static final String SERVICE_URL = "jdbc:oracle:thin:@//{host}:{port}/{schema}";

    private static final String SERVICE_SIGN = "SERVICE";
    /** SCHEMA：模式 */
    private static final String SCHEMA_URL =
            "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST={host})(PORT={port})))(CONNECT_DATA=(SERVICE_NAME={schema})))";

    private static final String SCHEMA_SIGN = "SCHEMA";
    /** TNS */
    private static final String TNS_URL = "jdbc:oracle:thin:@{schema}";

    private static final String TNS_SIGN = "TNS";

    public static final String ORACLE_SERVICE = "oracleService";
    /** 连接扩展参数 */
    public static final String ORACLE_LINK_TYPE = "oracleLinkType";

    public static final String ORACLE_ROLE = "oracleRole";

    @Override
    protected void init() {
        setInstance(
                DbBase.ORACLE, DbType.ORACLE, "1521", "oracle", "oracle.jdbc.OracleDriver", "", new SqlOracle(this));
    }

    /** Oracle特殊添加数据连接方式 */
    public Connection getOracleConn(DataSourceDTO dsd, String url) throws DataException {
        String logonUser = null;
        if (StringUtil.isNotEmpty(dsd.getOracleParam())) {
            Map<String, Object> oracleParamMap = JsonUtil.stringToMap(dsd.getOracleParam());
            if (oracleParamMap.size() > 0) {
                logonUser = oracleParamMap.get(DbOracle.ORACLE_ROLE).toString();
            }
        }
        return createOracleConn(driver, logonUser, dsd.getUserName(), dsd.getPassword(), url);
    }

    @Override
    public String getConnUrl(String prepareUrl, String host, Integer port, String dbName, String schema) {
        if (StringUtil.isNotEmpty(this.getOracleParam()) && StringUtil.isEmpty(prepareUrl)) {
            Map<String, Object> oracleParamMap = JsonUtil.stringToMap(this.getOracleParam());
            if (oracleParamMap.size() > 0) {
                schema = oracleParamMap.get(DbOracle.ORACLE_SERVICE).toString();
                String urlType = oracleParamMap.get(DbOracle.ORACLE_LINK_TYPE).toString();
                switch (urlType) {
                    case SID_SIGN:
                        prepareUrl = SID_URL;
                        break;
                    case SERVICE_SIGN:
                        prepareUrl = SERVICE_URL;
                        break;
                    case SCHEMA_SIGN:
                        prepareUrl = SCHEMA_URL;
                        break;
                    case TNS_SIGN:
                        prepareUrl = TNS_URL;
                        break;
                    default:
                        // throw new DataException("Oracle连接类型指定失败");
                        break;
                }
            }
        }
        if (StringUtil.isEmpty(prepareUrl)) {
            prepareUrl = SID_URL;
        }
        return super.getConnUrl(prepareUrl, host, port, dbName, schema);
    }

    @Override
    protected String getDynamicTableName(String tableName) {
        return DataSourceContextHolder.getDatasourceName().toUpperCase() + "." + tableName;
    }

    @Override
    public LinkedList<Object> getStructParams(String structParams, String table, DataSourceMod dbSourceOrDbLink) {
        DataSourceDTO dataSourceDTO = dbSourceOrDbLink.convertDTO();
        dataSourceDTO.setDbName(dataSourceDTO.getUserName());
        return super.getStructParams(structParams, table, dataSourceDTO);
    }

    private static Connection createOracleConn(
            String driver, String logonUser, String userName, String password, String url) throws DataException {
        // Oracle登录角色设置（Default，SYSDBA，SYSOPER）
        Properties conProps = DbOracle.setConnProp(logonUser, userName, password);
        return ConnUtil.ConnCommon.createConnByProp(driver, conProps.getProperty("user"), password, url, conProps);
    }

    public static Properties setConnProp(String logonUser, String userName, String password) {
        Properties conProps = new Properties();
        // 使用角色登录：userName + :@{角色}
        if (StringUtil.isNotEmpty(logonUser)) {
            // defaultRowPrefetch：从服务器预取的默认行数(默认值为“10”) String (containing integer value)
            conProps.put("defaultRowPrefetch", "15");
            /* 这里有一个风险：由于客户userName中含有一个或多个:@导致连接失败 */
            // internal_logon：允许您作为sys登录的权限，如sysdba或sysoper
            conProps.put("internal_logon", logonUser);
            conProps.put("user", userName);
        }
        conProps.put("user", userName);
        conProps.setProperty("password", password);
        return conProps;
    }
}
