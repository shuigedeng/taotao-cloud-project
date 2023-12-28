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

package com.taotao.cloud.workflow.biz.common.database.util;

import com.taotao.cloud.workflow.biz.common.database.model.interfaces.DataSourceMod;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/** Connection数据连接相关工具类 */
public class ConnUtil {

    /** 连接Connection */
    public static Connection getConn(DataSourceMod dbSourceOrDbLink) throws DataException {
        return getConn(dbSourceOrDbLink, null);
    }

    /**
     * 指定库名（多租户）
     *
     * @param dataSourceUtil 配置数据源信息
     * @param dbName 数据库名
     * @return url连接
     */
    public static Connection getConn(DataSourceMod dataSourceUtil, String dbName) throws DataException {
        // 避免spring托管的数据源DataSource数据被修改
        DataSourceDTO dsd = dataSourceUtil.convertDTO(dbName);
        if (DbTypeUtil.checkOracle(dsd)) {
            return getOracleConn(dsd);
        }
        return getConn(dsd.getUserName(), dsd.getPassword(), getUrl(dsd));
    }

    public static Connection getConn(String userName, String password, String url) throws DataException {
        DbBase db = DbTypeUtil.getDb(url);
        return ConnCommon.createConn(db.getDriver(), userName, password, url);
    }

    private static Connection getOracleConn(DataSourceDTO dsd) throws DataException {
        DbOracle dbOracle = new DbOracle();
        return dbOracle.getOracleConn(dsd, getUrl(dsd));
    }

    /*==============获取数据连接URL==============*/

    public static String getUrl(DataSourceMod dbSourceOrDbLink) {
        return getUrl(dbSourceOrDbLink, null);
    }

    /**
     * 指定库名（多租户）
     *
     * @param dbSourceOrDbLink 数据源
     * @param dbName 数据库名
     * @return url连接
     */
    public static String getUrl(DataSourceMod dbSourceOrDbLink, String dbName) {
        try {
            DataSourceDTO dsd = dbSourceOrDbLink.convertDTO(dbName);
            DbBase db = DbTypeUtil.getDb(dbSourceOrDbLink);
            String dbSchema;
            if (db.getClass() == DbOracle.class || db.getClass() == DbDM.class) {
                // 当Oracle与DM的时候模式与用户同名, 自定义Schema时不修改
                if (dsd.getDbSchema() != null && !dsd.getDbSchema().trim().isEmpty()) {
                    dbSchema = dsd.getDbSchema();
                } else {
                    dbSchema = dsd.getUserName();
                }
                db.setOracleParam(dsd.getOracleParam());
            } else {
                dbSchema = dsd.getDbSchema();
            }
            // 当数据库不填写的时候，取用户的用户名作为数据名
            dbName = dsd.getDbName() == null ? dsd.getUserName() : dsd.getDbName();
            // 当地址为空，用本地回环地址
            String host = dsd.getHost() == null ? "127.0.0.1" : dsd.getHost();
            // 当端口为空，用数据库一般默认端口
            Integer port = dsd.getPort() == null ? Integer.parseInt(db.getDefaultPort()) : dsd.getPort();
            return DbBase.BaseCommon.getDbBaseConnUrl(db, dsd.getPrepareUrl(), host, port, dbName, dbSchema);
        } catch (DataException e) {
            LogUtils.error(e);
        }
        return null;
    }

    /** 以下为ConnUtil上面非显性的公开方法 让调用者只关注getConn方法而不造成干扰 */
    public static class ConnCommon {

        /**
         * （基础）获取数据连接
         *
         * @param driver 驱动
         * @param userName 用户
         * @param password 密码
         * @param url url
         * @return 数据库连接
         * @throws DataException ignore
         */
        public static Connection createConn(String driver, String userName, String password, String url)
                throws DataException {
            try {
                Class.forName(driver);
                return DriverManager.getConnection(url, userName, password);
            } catch (Exception e) {
                LogUtils.error(e);
                throw DataException.errorLink(e.getMessage());
            }
        }

        public static Connection createConnByProp(
                String driver, String userName, String password, String url, Properties conProps) throws DataException {
            try {
                conProps.put("user", userName);
                conProps.put("password", password);
                Class.forName(driver);
                return DriverManager.getConnection(url, conProps);
            } catch (Exception e) {
                throw new DataException(e.getMessage());
            }
        }

        /**
         * 开启数据库获取表注解连接
         *
         * @param dbSourceOrDbLink 数据源对象
         * @return ignore
         */
        public static Connection getConnRemarks(DataSourceUtil dbSourceOrDbLink) throws DataException {
            Properties props = new Properties();
            props.setProperty("remarks", "true");
            props.setProperty("useInformationSchema", "true");
            props.setProperty("remarksReporting", "true");
            return createConnByProp(
                    dbSourceOrDbLink.getDriver(),
                    dbSourceOrDbLink.getUserName(),
                    dbSourceOrDbLink.getPassword(),
                    getUrl(dbSourceOrDbLink),
                    props);
        }
    }
}
