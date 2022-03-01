package com.taotao.cloud.sys.biz.tools.database.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ConfigDataService {
    @Autowired
    private JdbcService jdbcService;


    /**
     * 查询分组信息
     * @param connName
     * @param schemaName
     * @return
     * @throws SQLException
     */
    public List<String> groups(String connName, String schemaName) throws SQLException, IOException {
        String sql = "select group_id from "+schemaName+".config_info group by group_id ";
        return listQuery(connName,sql);
    }

    /**
     * 组内的所有 dataId 列表
     * @param group
     * @return
     */
    public List<String> dataIds(String connName, String schemaName,String group) throws SQLException, IOException {
        String sql = "select data_id from "+schemaName+".config_info where group_id='"+group+"'";
        return listQuery(connName,sql);
    }

    /**
     * 配置内容查询
     * @param group
     * @param dataId
     * @return
     */
    public String content(String connName,String schemaName,String group,String dataId) throws SQLException, IOException {
        String sql = "select content from "+schemaName+".config_info where group_id='%s' and data_id = '%s'";
        String formatSql = String.format(sql, group, dataId);
        String executeQuery = jdbcService.executeQuery(connName, formatSql, new ScalarHandler<String>(1));
        return executeQuery;
    }

    /**
     * 查询单列字符串的查询
     * @param connName
     * @param sql
     * @return
     */
    private List<String> listQuery(String connName,String sql) throws SQLException, IOException {
        ResultSetHandler<List<String>> resultSetHandler = new ColumnListHandler<String>(1);
        List<String> executeQuery = jdbcService.executeQuery(connName, sql, resultSetHandler);
        return executeQuery;
    }

//    @PostConstruct
//    public void init(){
//        pluginManager.register(PluginDto.builder().module(JdbcService.MODULE)
//                .name("configData").author("9420")
//                .logo("nacos.jpg")
//                .help("配置数据.md")
//                .desc("配置数据查询").build());
//    }
}
