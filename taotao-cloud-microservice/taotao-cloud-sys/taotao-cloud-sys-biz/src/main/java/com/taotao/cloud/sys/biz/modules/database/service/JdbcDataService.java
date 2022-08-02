package com.taotao.cloud.sys.biz.modules.database.service;

import com.alibaba.druid.pool.DruidDataSource;
import com.sanri.tools.modules.database.service.connect.ConnDatasourceAdapter;
import com.sanri.tools.modules.database.service.dtos.data.DynamicQueryDto;
import com.sanri.tools.modules.database.service.meta.aspect.JdbcConnection;
import com.sanri.tools.modules.database.service.meta.aspect.JdbcConnectionManagerAspect;
import com.sanri.tools.modules.database.service.meta.dtos.Namespace;
import com.sanri.tools.modules.database.service.meta.processor.CloseableResultSetHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class JdbcDataService {
    @Autowired
    private ConnDatasourceAdapter connDatasourceAdapter;

    /**
     * 执行 sql ,在某个连接上
     * @param connName
     * @param sql
     * @return
     */
    public List<Integer> executeUpdate(String connName, List<String> sqls, Namespace namespace) throws SQLException, IOException {
        List<Integer> updates = new ArrayList<>();
        final DruidDataSource dataSource = connDatasourceAdapter.poolDataSource(connName,namespace);
        QueryRunner queryRunner = new QueryRunner(dataSource);
        for (String sql : sqls) {
            int update = queryRunner.update(sql);
            updates.add(update);
        }

        return updates;
    }

    /**
     * 执行查询
     * @param connName
     * @param sql
     * @param resultSetHandler
     * @param params
     * @param <T>
     * @return
     * @throws SQLException
     */
    public <T> T executeQuery(String connName, String sql, ResultSetHandler<T> resultSetHandler,Namespace namespace, Object...params) throws SQLException, IOException {
        final DruidDataSource dataSource = connDatasourceAdapter.poolDataSource(connName,namespace);

        QueryRunner queryRunner = new QueryRunner(dataSource);
        return queryRunner.query(sql,resultSetHandler,params);
    }

    /**
     * 给出 sql ,查询出数据,将头信息和结果一并给出
     * @param connName
     * @param sqls
     * @return
     * @throws IOException
     * @throws SQLException
     */
    public List<DynamicQueryDto> executeDynamicQuery(String connName, List<String> sqls, Namespace namespace) throws IOException, SQLException {
        List<DynamicQueryDto> dynamicQueryDtos = new ArrayList<>();
        final DruidDataSource dataSource = connDatasourceAdapter.poolDataSource(connName,namespace);
        QueryRunner queryRunner = new QueryRunner(dataSource);
        for (String sql : sqls) {
            try {
                DynamicQueryDto dynamicQueryDto = queryRunner.query(sql, dynamicQueryProcessor);
                dynamicQueryDto.setSql(sql);
                dynamicQueryDtos.add(dynamicQueryDto);
            } catch (SQLException e) {
                log.error("当前 sql [{}],在 connName [{}] 执行失败，原因为 [{}]",sql,connName,e.getMessage(),e);
                throw e;
            }
        }
        return dynamicQueryDtos;
    }

    public static final ResultSetHandler<DynamicQueryDto> dynamicQueryProcessor = new CloseableResultSetHandler<>(new DynamicQueryProcessor());

    /**
     * 动态查询数据处理器
     */
    public static class DynamicQueryProcessor implements ResultSetHandler<DynamicQueryDto>{
        @Override
        public DynamicQueryDto handle(ResultSet resultSet) throws SQLException {
            DynamicQueryDto dynamicQueryDto = new DynamicQueryDto();

            //添加头部
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String columnLabel = metaData.getColumnLabel(i);
                int columnType = metaData.getColumnType(i);
                String columnTypeName = metaData.getColumnTypeName(i);
                dynamicQueryDto.addHeader(new DynamicQueryDto.Header(columnLabel,columnType,columnTypeName));
            }

            // 添加数据
            while (resultSet.next()) {
                Map<String,Object> row = new LinkedHashMap();
                for (int i = 1; i <= columnCount; i++) {
                    String columnLabel = metaData.getColumnLabel(i);
                    Object columnData = resultSet.getObject(i);
                    row.put(columnLabel,columnData);
                }

                dynamicQueryDto.addRow(row);
            }

            return dynamicQueryDto;
        }
    }
}
