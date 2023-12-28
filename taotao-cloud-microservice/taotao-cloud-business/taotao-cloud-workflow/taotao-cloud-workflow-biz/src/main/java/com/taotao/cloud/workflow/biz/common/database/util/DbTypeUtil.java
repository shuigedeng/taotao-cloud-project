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

import com.baomidou.mybatisplus.annotation.DbType;
import com.taotao.cloud.workflow.api.common.constant.MsgCode;
import com.taotao.cloud.workflow.biz.common.database.model.dto.DataSourceDTO;
import com.taotao.cloud.workflow.biz.common.database.model.interfaces.DataSourceMod;
import com.taotao.cloud.workflow.biz.common.database.source.DbBase;
import java.sql.Connection;
import java.sql.SQLException;

/** 处理判断数据类型有关工具类 */
public class DbTypeUtil {

    /*===========================数据库对象（重载）=====================================*/

    /**
     * 根据数据库名获取数据库对象 Case insensitive 大小写不敏感
     *
     * @param dbSourceOrDbLink 数据源
     * @return DbTableEnum2 数据表枚举类
     */
    public static DbBase getDb(DataSourceMod dbSourceOrDbLink) throws DataException {
        String dbSourceOrDbLinkEncode = getEncode(dbSourceOrDbLink.convertDTO());
        return getDbCommon(dbSourceOrDbLinkEncode);
    }

    public static DbBase getDb(Connection conn) {
        try {
            return getDb(conn.getMetaData().getURL());
        } catch (SQLException | DataException e) {
            LogUtils.error(e);
        }
        return null;
    }

    public static DbBase getDb(String url) throws DataException {
        String dbType = url.split(":")[1];
        for (DbBase dbBase : DbBase.DB_BASES) {
            if (dbType.equals(dbBase.getConnUrlEncode())) {
                return dbBase;
            }
        }
        throw new DataException(MsgCode.DB003.get());
    }

    public static DbBase getDriver(String dbType) throws DataException {
        for (DbBase dbBase : DbBase.DB_BASES) {
            if (dbBase.getWorkflowDbEncode().contains(dbType)) {
                return dbBase;
            }
        }
        throw new DataException(MsgCode.DB003.get());
    }

    /*===========================校验数据库类型=============================*/
    /**
     * IOC思想
     *
     * @return 是否匹配
     */
    private static Boolean checkDb(DataSourceMod dataSourceMod, String encode) {
        DataSourceDTO dataSourceDTO = dataSourceMod.convertDTO();
        String dataSourDbEncode = null;
        try {
            dataSourDbEncode = getEncode(dataSourceDTO);
        } catch (DataException e) {
            LogUtils.error(e);
        }
        return encode.equals(dataSourDbEncode);
    }

    public static Boolean checkOracle(DataSourceMod dataSourceMod) {
        return checkDb(dataSourceMod, DbBase.ORACLE);
    }

    public static Boolean checkMySQL(DataSourceMod dataSourceMod) {
        return checkDb(dataSourceMod, DbBase.MYSQL);
    }

    public static Boolean checkSQLServer(DataSourceMod dataSourceMod) {
        return checkDb(dataSourceMod, DbBase.SQL_SERVER);
    }

    public static Boolean checkDM(DataSourceMod dataSourceMod) {
        return checkDb(dataSourceMod, DbBase.DM);
    }

    public static Boolean checkKingbase(DataSourceMod dataSourceMod) {
        return checkDb(dataSourceMod, DbBase.KINGBASE_ES);
    }

    public static Boolean checkPostgre(DataSourceMod dataSourceMod) {
        return checkDb(dataSourceMod, DbBase.POSTGRE_SQL);
    }

    /*============================专用代码区域=========================*/

    /** MybatisPlusConfig */
    public static <T extends DataSourceUtil> DbType getMybatisEnum(T dataSourceUtil) throws DataException {
        return getDb(dataSourceUtil).getMpDbType();
    }

    /** 默认数据库与数据连接判断 */
    public static Boolean compare(String dbType1, String dbType2) throws DataException {
        dbType1 = checkDbTypeExist(dbType1, false);
        dbType2 = checkDbTypeExist(dbType2, false);
        if (dbType1 != null && dbType2 != null) {
            return dbType1.equals(dbType2);
        } else {
            return false;
        }
    }

    /*=========================内部复用代码================================*/

    /*====标准类型（重载）==*/

    /**
     * 获取标准类型编码 根据DbType
     *
     * @param dataSourceDTO 数据源
     * @return String
     */
    private static String getEncode(DataSourceDTO dataSourceDTO) throws DataException {
        return checkDbTypeExist(dataSourceDTO.getDbType(), true);
    }
    /**
     * ============**
     *
     * <p>/** 获取数据库对象
     *
     * @param encode 数据标准编码
     * @return 数据库基类
     */
    private static DbBase getDbCommon(String encode) {
        for (DbBase db : DbBase.DB_BASES) {
            if (db.getWorkflowDbEncode().equalsIgnoreCase(encode)) {
                return db;
            }
        }
        return null;
    }

    /**
     * 0、校验数据类型是否符合编码标准（包含即可）
     *
     * @param dbType 数据类型
     * @param exceptionOnOff 无匹配是否抛异常
     * @return 数据标准编码
     * @throws DataException 数据库类型不符合编码
     */
    private static String checkDbTypeExist(String dbType, Boolean exceptionOnOff) throws DataException {
        for (String enEncode : DbBase.DB_ENCODES) {
            if (enEncode.equals(dbType)) {
                return enEncode;
            }
        }
        if (exceptionOnOff) {
            throw new DataException(MsgCode.DB001.get());
        }
        return null;
    }
}
