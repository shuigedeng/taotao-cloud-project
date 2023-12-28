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
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/** 类功能 */
public class ConnUtil2 {

    /*=====================超时方案=====================*/

    public static Connection getConnTimeout(DataSourceMod dbSourceOrDbLink) throws DataException {
        return getConnTimeout(dbSourceOrDbLink, null);
    }

    public static Connection getConnTimeout(DataSourceMod dataSourceUtil, String dbName) throws DataException {
        DataSourceDTO dsd = dataSourceUtil.convertDTO(dbName);
        DbBase dbBase = DbTypeUtil.getDb(dataSourceUtil);
        return createConn(dbBase.getDriver(), dsd.getUserName(), dsd.getPassword(), ConnUtil.getUrl(dsd), 3L, "254");
    }

    public static Connection getConnTimeout(String userName, String password, String url) throws DataException {
        return createConn(DbTypeUtil.getDb(url).getDriver(), userName, password, url, 3L, "254");
    }

    /**
     * （超时方案）获取数据连接 - （防止有些数据库，长时间连接不成功也也不报错的情况）
     *
     * @param userName 用户
     * @param password 密码
     * @param url url
     * @param driverClass 驱动
     * @param timeoutNum 超时时间
     * @param warning 错误类型
     * @param warning 报错编码
     * @throws DataException 连接错误
     */
    private static Connection createConn(
            String driverClass, String userName, String password, String url, Long timeoutNum, String warning)
            throws DataException {
        final Connection[] conn = {null};
        Callable<String> task = getTask(userName, password, url, driverClass, conn);
        futureGo(task, warning, timeoutNum);
        try {
            Connection cConn = conn[0];
            return cConn;
        } catch (Exception e) {
            throw new DataException(e.getMessage());
        }
    }

    private static void futureGo(Callable<String> task, String warning, Long timeoutNum) throws DataException {
        ThreadPoolTaskExecutor executor = SpringContext.getBean(ThreadPoolTaskExecutor.class);
        Future<String> future = executor.submit(task);
        try {
            // 设置超时时间（默认3L）
            String rst = future.get(timeoutNum, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            throw DataException.errorLink("连接超时");
        } catch (Exception e) {
            throw DataException.errorLink("连接错误");
        } finally {
            executor.shutdown();
        }
    }

    private static Callable<String> getTask(
            String userName, String password, String url, String driverClass, Connection[] conn) throws DataException {
        Callable<String> task = () -> {
            // 执行耗时代码
            try {
                Class.forName(driverClass);
                conn[0] = DriverManager.getConnection(url, userName, password);
            } catch (Exception e) {
                LogUtils.error(e);
                throw new DataException(e.getMessage());
            }
            return "jdbc连接成功";
        };
        return task;
    }
}
