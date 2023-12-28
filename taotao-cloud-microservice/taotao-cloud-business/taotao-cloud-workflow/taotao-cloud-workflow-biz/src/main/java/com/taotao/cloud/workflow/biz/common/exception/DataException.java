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

package com.taotao.cloud.workflow.biz.common.exception;

import com.taotao.cloud.workflow.biz.common.constant.MsgCode;
import java.sql.Connection;
import java.sql.SQLException;

/** 数据库异常类 */
public class DataException extends Exception {

    public DataException() {
        super();
    }

    public DataException(String message) {
        super(message);
    }

    public static DataException errorLink(String warning) {
        return new DataException(MsgCode.DB002.get() + warning);
    }

    /**
     * mysql表重复
     *
     * @param error 错误信息
     */
    public static DataException tableExists(String error, Connection rollbackConn) {
        executeRollback(rollbackConn);
        // Mysql英文报错，临时解决方案
        error = error.replace("Table", "表").replace("already exists", "已经存在。");
        return new DataException(error);
    }

    public static DataException rollbackDataException(String message, Connection rollbackConn) {
        executeRollback(rollbackConn);
        return new DataException(message);
    }

    private static void executeRollback(Connection conn) {
        try {
            conn.rollback();
        } catch (SQLException e) {
            LogUtils.error(e);
        }
    }
}
