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

package com.taotao.cloud.workflow.biz.common.util;

import java.util.List;

/** 短信参数解析 */
public class ParameterUtil {

    /**
     * 获取参数
     *
     * @param text 需要解析的文本
     * @param list 存放参数的集合
     * @return
     */
    public static String parse(String openToken, String closeToken, String text, List<String> list) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        // search open token
        int start = text.indexOf(openToken);
        if (start == -1) {
            return text;
        }
        char[] src = text.toCharArray();
        int offset = 0;
        final StringBuilder builder = new StringBuilder();
        StringBuilder expression = null;
        while (start > -1) {
            if (start > 0 && src[start - 1] == '\\') {
                // this open token is escaped. remove the backslash and continue.
                builder.append(src, offset, start - offset - 1).append(openToken);
                offset = start + openToken.length();
            } else {
                // found open token. let's search close token.
                if (expression == null) {
                    expression = new StringBuilder();
                } else {
                    expression.setLength(0);
                }
                builder.append(src, offset, start - offset);
                offset = start + openToken.length();
                int end = text.indexOf(closeToken, offset);
                while (end > -1) {
                    if (end > offset && src[end - 1] == '\\') {
                        // this close token is escaped. remove the backslash and continue.
                        expression.append(src, offset, end - offset - 1).append(closeToken);
                        offset = end + closeToken.length();
                        end = text.indexOf(closeToken, offset);
                    } else {
                        expression.append(src, offset, end - offset);
                        break;
                    }
                }
                // 塞到list中
                list.add(expression.toString());
                if (end == -1) {
                    // close token was not found.
                    builder.append(src, start, src.length - start);
                    offset = src.length;
                } else {
                    offset = end + closeToken.length();
                }
            }
            start = text.indexOf(openToken, offset);
        }
        if (offset < src.length) {
            builder.append(src, offset, src.length - offset);
        }
        return builder.toString();
    }

    /**
     * 得到url
     *
     * @param model
     * @return
     */
    public static String getUrl(TenantLinkModel model) {
        if (model != null) {
            // 如果使用自定义URL
            if (StringUtil.isNotEmpty(model.getConnectionStr())) {
                return model.getConnectionStr();
            }
            // 不是使用自定义URL
            String url = "";
            if ("mysql".equalsIgnoreCase(model.getDbType())) {
                url = "jdbc:mysql://{host}:{port}/{dbname}";
            } else if ("SQLServer".equalsIgnoreCase(model.getDbType())) {
                url = "jdbc:sqlserver://{host}:{port};databaseName={dbname}";
            } else if ("Oracle".equalsIgnoreCase(model.getDbType())) {
                url = "jdbc:oracle:thin:@//{host}:{port}/{schema}";
            } else if ("DM8".equalsIgnoreCase(model.getDbType())) {
                url = "jdbc:dm://{host}:{port}/{schema}";
            } else if ("KingbaseES".equalsIgnoreCase(model.getDbType())) {
                url = "jdbc:kingbase8://{host}:{port}/{dbname}?currentSchema={schema}";
            } else if ("PostgreSQL".equalsIgnoreCase(model.getDbType())) {
                url = "jdbc:postgresql://{host}:{port}/{dbname}";
            }
            url = url.replace("{host}", model.getHost());
            url = url.replace("{port}", model.getPort());
            url = url.replace("{dbname}", model.getServiceName());
            url = url.replace("{schema}", model.getDbSchema());
            return url;
        }
        return null;
    }

    /**
     * 判断数据库类型
     *
     * @param driverName 驱动名称
     * @return
     */
    public static String getDbType(String driverName) {
        if (StringUtil.isNotEmpty(driverName)) {
            // 不是使用自定义URL
            String dbType = "";
            if (driverName.contains("mysql")) {
                dbType = "MySQL";
            } else if (driverName.contains("sqlserver")) {
                dbType = "SQLServer";
            } else if (driverName.contains("oracle")) {
                dbType = "Oracle";
            } else if (driverName.contains("dm")) {
                dbType = "DM8";
            } else if (driverName.contains("kingbase8")) {
                dbType = "KingbaseES";
            } else if (driverName.contains("postgresql")) {
                dbType = "PostgreSQL";
            }
            return dbType;
        }
        return null;
    }
}
