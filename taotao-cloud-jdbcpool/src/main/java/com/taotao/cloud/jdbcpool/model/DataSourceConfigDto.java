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

package com.taotao.cloud.jdbcpool.model;

import java.io.Serializable;

/**
 * 配置对象
 *
 * @since 1.7.0
 */
public class DataSourceConfigDto implements Serializable {

    /**
     * 驱动类
     */
    protected String driverClass;

    /**
     * jdbc url
     */
    protected String jdbcUrl;

    /**
     * 用户
     */
    protected String user;

    /**
     * 密码
     */
    protected String password;

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "DataSourceConfigDto{"
                + "driverClass='"
                + driverClass
                + '\''
                + ", jdbcUrl='"
                + jdbcUrl
                + '\''
                + ", user='"
                + user
                + '\''
                + ", password='"
                + password
                + '\''
                + '}';
    }
}
