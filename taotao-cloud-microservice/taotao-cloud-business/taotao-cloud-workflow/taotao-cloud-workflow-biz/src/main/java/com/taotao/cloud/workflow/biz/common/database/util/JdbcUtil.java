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

import com.taotao.cloud.workflow.biz.common.database.model.dto.PreparedStatementDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

/** jdbc自定义工具类 */
@Slf4j
public class JdbcUtil {

    /** 批量执行sql语句(适合增、删、改) CRUD：增加(Create)、检索(Retrieve)、更新(Update)、删除(Delete) */
    public static Boolean creUpDe(PreparedStatementDTO statementDTO) throws DataException {
        Connection conn = statementDTO.getConn();
        String sql = statementDTO.getPrepareSql();
        LinkedList<Object> data = statementDTO.getPrepareDataList();
        String[] batchSql = sql.split(";");
        boolean flag = false;
        try {
            conn.setAutoCommit(false);
            /*  事务自动提交（默认true，即自动提交事务）
            注意：表引擎不为InnoDB，回滚失败，MySQL中，DDL创建Create、删除Drop和更改Alter表结构等操作回滚无效。*/
            flag = creUpDeBase(conn, Arrays.asList(statementDTO)).get(0);
            // 批量提交事务
            conn.commit();
            return flag;
            // 捕捉回滚操作
        } catch (Exception e) {
            throw DataException.rollbackDataException(e.getMessage(), conn);
        }
    }

    /**
     * 不同语句批量执行
     *
     * @param conn 数据源源连接
     * @param DTOs
     */
    public static List<Boolean> creUpDeBatch(Connection conn, List<PreparedStatementDTO> DTOs) throws DataException {
        try {
            conn.setAutoCommit(false);
            List<Boolean> flags = creUpDeBase(conn, DTOs);
            conn.commit();
            return flags;
        } catch (Exception e) {
            // 捕捉回滚操作
            throw DataException.rollbackDataException(e.getMessage(), conn);
        }
    }

    private static List<Boolean> creUpDeBase(Connection conn, List<PreparedStatementDTO> DTOs) throws SQLException {
        // 执行结果标识
        List<Boolean> flags = new LinkedList<>();
        for (PreparedStatementDTO dto : DTOs) {
            // 执行SQL语句
            String sql = dto.getPrepareSql();
            // ? 参数集合
            LinkedList<Object> data = dto.getPrepareDataList();
            // 执行
            @Cleanup PreparedStatement statement = conn.prepareStatement(XSSEscape.escapeEmpty(sql));
            for (int i = 0; i < data.size(); i++) {
                statement.setObject(i + 1, data.get(i));
            }
            flags.add(statement.execute());
        }
        return flags;
    }

    /**
     * 同一条语句批量执行
     *
     * @param conn 数据源源连接
     * @param sql 准备执行的SQL语句
     * @param DTOs 预备参数
     * @throws DataException
     */
    public static int[] creUpDeBatchOneSql(Connection conn, String sql, List<PreparedStatementDTO> DTOs)
            throws DataException {
        try {
            @Cleanup PreparedStatement statement = conn.prepareStatement(sql);
            conn.setAutoCommit(false);
            for (PreparedStatementDTO dto : DTOs) {
                LinkedList<Object> data = dto.getPrepareDataList();
                for (int i = 0; i < data.size(); i++) {
                    statement.setObject(i + 1, data.get(i));
                }
                statement.addBatch();
            }
            int[] ints = statement.executeBatch();
            conn.commit();
            return ints;
        } catch (Exception e) {
            // 捕捉回滚操作
            throw DataException.rollbackDataException(e.getMessage(), conn);
        }
    }

    /*========================query查询语句==============================*/

    /** 通用：多条查询 */
    public static List<Map<String, Object>> queryList(PreparedStatementDTO statementDTO) throws DataException {
        JdbcDTO<JdbcGetMod> jdbcDTO = new JdbcDTO<>();
        jdbcDTO.setReturnType(DbConst.MAP_MOD);
        return query(statementDTO, jdbcDTO).getMapMods();
    }

    /** 通用：多条查询（查询别名） */
    public static List<Map<String, Object>> queryListAlias(PreparedStatementDTO statementDTO) throws DataException {
        JdbcDTO<JdbcGetMod> jdbcDTO = new JdbcDTO<>();
        jdbcDTO.setReturnType(DbConst.MAP_MOD);
        jdbcDTO.setAliasFlag(true);
        return query(statementDTO, jdbcDTO).getMapMods();
    }

    /** 通用：多条查询（开启小写） */
    public static List<Map<String, Object>> queryListLowercase(PreparedStatementDTO statementDTO) throws DataException {
        JdbcDTO<JdbcGetMod> jdbcDTO = new JdbcDTO<>();
        jdbcDTO.setReturnType(DbConst.MAP_MOD);
        // 开启小写模式
        jdbcDTO.setLowercaseFlag(true);
        return query(statementDTO, jdbcDTO).getMapMods();
    }

    /** 通用：单条查询 */
    public static Map<String, Object> queryOne(PreparedStatementDTO statementDTO) throws DataException {
        List<Map<String, Object>> mapList = queryList(statementDTO);
        return mapList.size() > 0 ? mapList.get(0) : new HashMap<>();
    }

    /** 查单条Int类型返回值 */
    public static Integer queryOneInt(PreparedStatementDTO statementDTO, String keyWord) throws DataException {
        List<Map<String, Object>> mapList = queryList(statementDTO);
        if (mapList.size() > 0) {
            keyWord = DbAliasEnum.getAsByDb(DbTypeUtil.getDb(statementDTO.getConn()), keyWord);
            Map<String, Object> map = CollectionUtils.mapKeyToLower(queryOne(statementDTO));
            return Integer.parseInt(String.valueOf(map.get(keyWord.toLowerCase())));
        } else {
            throw new DataException(MsgCode.FA020.get());
        }
    }

    /** 专用：查询模板 说明：DbJdbcModel对象，为通用的数据返回对象，每条信息不同字段对应的数据，包含此相应字段的信息 */
    public static List<List<DbFieldMod>> queryIncludeFieldMods(PreparedStatementDTO statementDTO) throws DataException {
        JdbcDTO<JdbcGetMod> jdbcDTO = new JdbcDTO<>();
        jdbcDTO.setReturnType(DbConst.INCLUDE_FIELD_MOD);
        return query(statementDTO, jdbcDTO).getIncludeFieldMods();
    }

    /** 专用：打印模板使用 */
    public static List<List<DbFieldMod>> queryTableFields(PreparedStatementDTO statementDTO) throws DataException {
        JdbcDTO<JdbcGetMod> jdbcDTO = new JdbcDTO<>();
        jdbcDTO.setReturnType(DbConst.TABLE_FIELD_MOD);
        return query(statementDTO, jdbcDTO).getTableFieldMods();
    }

    /** 自定义末班查询 */
    public static <T extends JdbcGetMod> List<T> queryCustomMods(PreparedStatementDTO statementDTO, Class<T> modType)
            throws DataException {
        JdbcDTO<T> jdbcDTO = new JdbcDTO<>();
        jdbcDTO.setReturnType(DbConst.CUSTOM_MOD);
        jdbcDTO.setModType(modType);
        jdbcDTO.setDbBase(DbTypeUtil.getDb(statementDTO.getConn()));
        return query(statementDTO, jdbcDTO).getCustomMods();
    }

    public static JdbcPageMod queryPage(
            PreparedStatementDTO statementDTO, String sortType, Integer currentPage, Integer pageSize, JdbcDTO jdbcDTO)
            throws DataException {
        DbBase db = DbTypeUtil.getDb(statementDTO.getConn());
        SqlBase sqlBase = db.getSqlBase();
        String[] sqlArray = sqlBase.getPageSql(statementDTO.getPrepareSql(), sortType, currentPage, pageSize);
        JdbcPageMod pageModel = new JdbcPageMod<>();
        String selectSql = "";
        try {
            selectSql = sqlArray[0];
            // 方便测试打印到控制台
            LogUtils.info("列表sql语句为:" + selectSql);
            statementDTO.setPrepareSql(selectSql);
            List<?> resultData = query(statementDTO, jdbcDTO).getData();
            pageModel.setDataList(resultData);
            selectSql = sqlArray[1];
            statementDTO.setPrepareSql(selectSql);
            pageModel.setTotalRecord(queryOneInt(statementDTO, DbAliasEnum.TOTAL_RECORD.asByDb(db)));
            pageModel.setCurrentPage(currentPage);
            pageModel.setPageSize(pageSize);
        } catch (DataException e) {
            log.error("在线列表sql语句错误：" + selectSql);
            throw new DataException("sql异常：" + selectSql);
        }
        return pageModel;
    }

    public static JdbcPageMod queryPage(
            PreparedStatementDTO statementDTO, String sortType, Integer currentPage, Integer pageSize)
            throws DataException {
        JdbcDTO<JdbcGetMod> jdbcDTO = new JdbcDTO<>();
        jdbcDTO.setReturnType(DbConst.MAP_MOD);
        jdbcDTO.setAliasFlag(true);
        return queryPage(statementDTO, sortType, currentPage, pageSize, jdbcDTO);
    }

    /*=====================================================*/
    /**
     * Jdbc查询
     *
     * @param statementDTO 数据库执行相关信息
     * @param jdbcDTO 所需相关参数
     * @param <T> 自定义对象模型类型
     * @return 查询结果
     * @throws DataException ignore
     */
    private static <T extends JdbcGetMod> JdbcDTO<T> query(PreparedStatementDTO statementDTO, JdbcDTO<T> jdbcDTO)
            throws DataException {
        try {
            String sql = statementDTO.getPrepareSql();
            //            String prepareSql = HtmlUtils.htmlEscape(sql, CharsetKit.UTF_8);
            Connection conn = statementDTO.getConn();
            LinkedList<Object> prepareDataList = statementDTO.getPrepareDataList();
            @Cleanup PreparedStatement statement = conn.prepareStatement(XSSEscape.escapeEmpty(sql));
            if (!prepareDataList.isEmpty()) {
                for (int i = 0; i < prepareDataList.size(); i++) {
                    Object dataObject = prepareDataList.get(i);
                    //                    if(dataObject!=null){
                    //                        dataObject =
                    // HtmlUtils.htmlEscape(String.valueOf(dataObject),CharsetKit.UTF_8);
                    //                    }
                    LogUtils.info(dataObject);
                    statement.setObject(i + 1, dataObject);
                }
            }
            @Cleanup ResultSet result = statement.executeQuery();
            if (result != null) {
                return getList(result, jdbcDTO);
            } else {
                throw new DataException(MsgCode.DB004.get());
            }
        } catch (Exception e) {
            throw new DataException(e.getMessage());
        }
    }

    private static void close(Connection conn, PreparedStatement pps, ResultSet result) {
        // 关闭ResultSet
        try {
            if (result != null) {
                result.close();
            }
        } catch (SQLException e) {
            LogUtils.error(e);
        } finally {
            // 关闭PreparedStatement
            try {
                if (pps != null) {
                    pps.close();
                }
            } catch (SQLException e) {
                LogUtils.error(e);
            }
        }
    }

    private static <T extends JdbcGetMod> JdbcDTO<T> getList(ResultSet result, JdbcDTO<T> jdbcDTO) throws Exception {
        // 自定义jdbc模型对象
        List<T> customMods = new ArrayList<>();
        List<Map<String, Object>> mapMods = new ArrayList<>();
        List<List<DbFieldMod>> includeFieldMods = new ArrayList<>();
        List<List<DbFieldMod>> tableFieldMods = new ArrayList<>();
        // 给这些集合赋值
        if (DbConst.MAP_MOD.equals(jdbcDTO.getReturnType())) {
            while (result.next()) {
                mapMods.add(getModel0(result, jdbcDTO));
            }
        } else if (DbConst.INCLUDE_FIELD_MOD.equals(jdbcDTO.getReturnType())) {
            while (result.next()) {
                includeFieldMods.add(getModel1(result, jdbcDTO));
            }
        } else if (DbConst.TABLE_FIELD_MOD.equals(jdbcDTO.getReturnType())) {
            // DbJdbcModel集合保底为1条数据，为了返回字段相关信息
            tableFieldMods.add(getModel3(result, jdbcDTO));
        } else if (DbConst.CUSTOM_MOD.equals(jdbcDTO.getReturnType())) {
            while (result.next()) {
                T newT = jdbcDTO.getModType().newInstance();
                customMods.add(getModel2(new ModelDTO(result, jdbcDTO.getDbBase()), newT));
            }
        }
        // 返回值：自定义jdbc模型对象
        jdbcDTO.setCustomMods(customMods);
        // 返回值：List<Map<String, Object>>
        jdbcDTO.setMapMods(mapMods);
        // 返回值：包含字段模型
        jdbcDTO.setIncludeFieldMods(includeFieldMods);
        // 返回值：返回表字段信息
        jdbcDTO.setTableFieldMods(tableFieldMods);
        return jdbcDTO;
    }

    /**
     * ResultSet转Map
     *
     * @param jdbcDTO 所需相关参数
     * @param <T> 未使用
     * @return 结果集的Map集合
     * @throws SQLException ignore
     */
    private static <T> Map<String, Object> getModel0(ResultSet rs, JdbcDTO<T> jdbcDTO) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        Map<String, Object> map = new HashMap<>();
        // 获取字段集合信息
        int columnCount = md.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            String fieldName = jdbcDTO.getAliasFlag() ? md.getColumnLabel(i) : md.getColumnName(i);
            fieldName = jdbcDTO.getLowercaseFlag() ? fieldName.toLowerCase() : fieldName;
            map.put(fieldName, XSSEscape.escapeEmpty(rs.getString(i)));
        }
        return map;
    }

    /**
     * 基本对象模型 （包含类型）
     *
     * @param jdbcDTO 所需相关参数
     * @param <T> 未使用
     * @return 包含字段信息的结果集对象
     * @throws SQLException ignore
     */
    private static <T> List<DbFieldMod> getModel1(ResultSet rs, JdbcDTO<T> jdbcDTO) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columnCount = md.getColumnCount();
        List<DbFieldMod> list = new ArrayList<>();
        for (int i = 1; i <= columnCount; i++) {
            list.add(new DbFieldMod(
                    md.getTableName(i),
                    md.getColumnLabel(i),
                    md.getColumnName(i),
                    md.getColumnTypeName(i),
                    md.getColumnType(i),
                    XSSEscape.escapeEmpty(rs.getString(i)),
                    jdbcDTO.getLowercaseFlag()));
        }
        return list;
    }

    /**
     * 获取自定义对象模型集合
     *
     * @param t 自定义对象模型一个实例
     * @param <T> 自定义对象模型类型
     * @return 自定义对象集合
     * @throws SQLException ignore
     */
    private static <T extends JdbcGetMod> T getModel2(ModelDTO modelDTO, T t) throws SQLException {
        t.setMod(modelDTO);
        t = XSSEscape.escapeObj(t);
        return t;
    }

    /**
     * 获取表字段信息
     *
     * @param jdbcDTO 所需相关参数
     * @param <T> 未使用
     * @return 返回字段信息
     * @throws SQLException ignore
     */
    private static <T> List<DbFieldMod> getModel3(ResultSet rs, JdbcDTO<T> jdbcDTO) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        List<DbFieldMod> dbFieldModList = new ArrayList<>();
        for (int i = 1; i <= md.getColumnCount(); i++) {
            dbFieldModList.add(new DbFieldMod(
                    md.getTableName(i),
                    md.getColumnLabel(i),
                    md.getColumnName(i),
                    md.getColumnTypeName(i),
                    md.getColumnType(i),
                    null,
                    jdbcDTO.getLowercaseFlag()));
        }
        return dbFieldModList;
    }

    /**
     * 分页处理
     *
     * @param rs
     * @param jdbcDTO
     * @param <T>
     * @return
     * @throws SQLException
     */
    private static <T> Map<String, Object> getModel4(ResultSet rs, JdbcDTO<T> jdbcDTO) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        Map<String, Object> map = new HashMap<>();
        // 获取字段集合信息
        int columnCount = md.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            String fieldName = jdbcDTO.getLowercaseFlag() ? md.getColumnName(i).toLowerCase() : md.getColumnName(i);
            map.put(fieldName, rs.getString(i));
        }
        return map;
    }
}
