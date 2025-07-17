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

package com.taotao.cloud.jdbcpool.util;

import com.taotao.cloud.jdbcpool.exception.JdbcPoolException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.util.StringUtils;

/**
 * 驱动类工具
 *
 * 1. 默认添加对应的驱动类信息
 *
 * @author shuigedeng
 * @since 1.2.0
 */
public final class DriverClassUtil {

    /**
     * 存放驱动类信息
     */
    private static final Map<String, String> DRIVER_CLASS_MAP;

    static {
        DRIVER_CLASS_MAP = new HashMap<>(32);
        DRIVER_CLASS_MAP.put("jdbc:db2", "COM.ibm.db2.jdbc.app.DB2Driver");
        DRIVER_CLASS_MAP.put("jdbc:firebirdsql", "org.firebirdsql.jdbc.FBDriver");
        DRIVER_CLASS_MAP.put("jdbc:edbc", "ca.edbc.jdbc.EdbcDriver");
        DRIVER_CLASS_MAP.put("jdbc:pointbase", "com.pointbase.jdbc.jdbcUniversalDriver");
        DRIVER_CLASS_MAP.put("jdbc:fake", "com.alibaba.druid.mock.MockDriver");
        DRIVER_CLASS_MAP.put("jdbc:informix-sqli", "com.informix.jdbc.IfxDriver");
        DRIVER_CLASS_MAP.put("jdbc:sqlite", "org.sqlite.JDBC");
        DRIVER_CLASS_MAP.put("jdbc:microsoft", "com.microsoft.jdbc.sqlserver.SQLServerDriver");
        DRIVER_CLASS_MAP.put("jdbc:hsqldb", "org.hsqldb.jdbcDriver");
        DRIVER_CLASS_MAP.put("jdbc:postgresql", "org.postgresql.Driver");
        DRIVER_CLASS_MAP.put("jdbc:ingres", "com.ingres.jdbc.IngresDriver");
        DRIVER_CLASS_MAP.put("jdbc:cloudscape", "COM.cloudscape.core.JDBCDriver");
        DRIVER_CLASS_MAP.put("jdbc:JSQLConnect", "com.jnetdirect.jsql.JSQLDriver");
        DRIVER_CLASS_MAP.put("jdbc:derby", "org.apache.derby.jdbc.EmbeddedDriver");
        DRIVER_CLASS_MAP.put("jdbc:timesten", "com.timesten.jdbc.TimesTenDriver");
        DRIVER_CLASS_MAP.put("jdbc:interbase", "interbase.interclient.Driver");
        DRIVER_CLASS_MAP.put("jdbc:h2", "org.h2.Driver");
        DRIVER_CLASS_MAP.put("jdbc:as400", "com.ibm.as400.access.AS400JDBCDriver");
        DRIVER_CLASS_MAP.put("jdbc:sybase:Tds", "com.sybase.jdbc2.jdbc.SybDriver");
        DRIVER_CLASS_MAP.put("jdbc:mock", "com.alibaba.druid.mock.MockDriver");
        DRIVER_CLASS_MAP.put("jdbc:oracle", "oracle.jdbc.driver.OracleDriver");
        DRIVER_CLASS_MAP.put("jdbc:mysql", "com.mysql.cj.jdbc.Driver");
        DRIVER_CLASS_MAP.put("jdbc:odps", "com.aliyun.odps.jdbc.OdpsDriver");
        DRIVER_CLASS_MAP.put("jdbc:mckoi", "com.mckoi.JDBCDriver");
        DRIVER_CLASS_MAP.put("jdbc:jtds", "net.sourceforge.jtds.jdbc.Driver");
        DRIVER_CLASS_MAP.put("jdbc:sapdb", "com.sap.dbtech.jdbc.DriverSapDB");
        DRIVER_CLASS_MAP.put("jdbc:JTurbo", "com.newatlanta.jturbo.driver.Driver");
        DRIVER_CLASS_MAP.put("jdbc:mimer:multi1", "com.mimer.jdbc.Driver");
    }

    public static String trim(final String original) {
        if (StringUtils.isEmpty(original)) {
            return original;
        }
        return original.trim();
    }

    public static boolean isEmptyTrim(final String string) {
        if (isEmpty(string)) {
            return true;
        }

        String trim = trim(string);
        return isEmpty(trim);
    }

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * 加载驱动类信息
     * @param driverClass 驱动类
     * @param url 连接信息
     * @since 1.2.0
     */
    public static void loadDriverClass(String driverClass, final String url) {

        if (isEmptyTrim(driverClass)) {
            driverClass = getDriverClassByUrl(url);
        }

        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException e) {
            throw new JdbcPoolException(e);
        }
    }

    /**
     * 根据 URL 获取对应的驱动类
     *
     * 1. 禁止 url 为空
     * 2. 如果未找到，则直接报错。
     * @param url url
     * @return 驱动信息
     */
    private static String getDriverClassByUrl(final String url) {

        for (Map.Entry<String, String> entry : DRIVER_CLASS_MAP.entrySet()) {
            String urlPrefix = entry.getKey();
            if (url.startsWith(urlPrefix)) {
                return entry.getValue();
            }
        }

        throw new JdbcPoolException("Can't auto find match driver class for url: " + url);
    }
}
