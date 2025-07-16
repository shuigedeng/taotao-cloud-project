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

package com.taotao.cloud.hadoop.mr.component.logenhance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

/**
 * DBLoader
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2020/11/26 下午8:27
 */
public class DBLoader {

    /**
     * 加载数据库规则
     *
     * @param ruleMap ruleMap
     * @return void
     * @author shuigedeng
     * @version 2022.04
     * @since 2020/11/26 下午8:27
     */
    public static void dbLoader(Map<String, String> ruleMap) throws Exception {
        Connection conn = null;
        Statement st = null;
        ResultSet res = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/urldb", "root", "root");
            st = conn.createStatement();
            res = st.executeQuery("select url,content from url_rule");
            while (res.next()) {
                ruleMap.put(res.getString(1), res.getString(2));
            }
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
                if (st != null) {
                    st.close();
                }
                if (conn != null) {
                    conn.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
