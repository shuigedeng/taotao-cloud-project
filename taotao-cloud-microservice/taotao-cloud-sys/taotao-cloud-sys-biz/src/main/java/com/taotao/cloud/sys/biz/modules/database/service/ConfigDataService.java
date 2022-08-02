package com.taotao.cloud.sys.biz.modules.database.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.sanri.tools.modules.database.service.meta.dtos.Namespace;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ConfigDataService {
    @Autowired
    private JdbcDataService jdbcDataService;

    /**
     * 查询分组信息
     * @param connName
     * @param schemaName
     * @return
     * @throws SQLException
     */
    public List<String> groups(String connName, Namespace namespace) throws SQLException, IOException {
        String sql = "select group_id from config_info group by group_id ";
        return listQuery(connName,sql,namespace);
    }

    /**
     * 组内的所有 dataId 列表
     * @param group
     * @return
     */
    public List<String> dataIds(String connName, Namespace namespace,String group) throws SQLException, IOException {
        String sql = "select data_id from config_info where group_id='"+group+"'";
        return listQuery(connName,sql,namespace);
    }

    /**
     * 配置内容查询
     * @param group
     * @param dataId
     * @return
     */
    public String content(String connName,Namespace namespace,String group,String dataId) throws SQLException, IOException {
        String sql = "select content from config_info where group_id='%s' and data_id = '%s'";
        String formatSql = String.format(sql, group, dataId);
        String executeQuery = jdbcDataService.executeQuery(connName, formatSql, new ScalarHandler<String>(1),namespace);
        return executeQuery;
    }

    /**
     * 查询单列字符串的查询
     * @param connName
     * @param sql
     * @return
     */
    private List<String> listQuery(String connName,String sql,Namespace namespace) throws SQLException, IOException {
        ResultSetHandler<List<String>> resultSetHandler = new ColumnListHandler<String>(1);
        List<String> executeQuery = jdbcDataService.executeQuery(connName, sql, resultSetHandler,namespace);
        return executeQuery;
    }
}
