package com.taotao.cloud.sys.biz.modules.database.service;

import com.alibaba.druid.pool.DruidDataSource;
import com.sanri.tools.modules.core.service.NamedThreadFactory;
import com.sanri.tools.modules.database.service.code.dtos.ProjectGenerateConfig;
import com.sanri.tools.modules.database.service.connect.ConnDatasourceAdapter;
import com.sanri.tools.modules.database.service.dtos.compare.DiffType;
import com.sanri.tools.modules.database.service.dtos.data.transfer.DataChange;
import com.sanri.tools.modules.database.service.dtos.data.transfer.DataTransferContext;
import com.sanri.tools.modules.database.service.dtos.data.transfer.TransferDataRow;
import com.sanri.tools.modules.database.service.dtos.meta.TableMeta;
import com.sanri.tools.modules.database.service.meta.dtos.Column;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.beans.BeanMap;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DataTransferService {
    @Autowired
    private ConnDatasourceAdapter connDatasourceAdapter;
    @Autowired
    private TableSearchService tableSearchService;
    @Autowired
    private Configuration configuration;

    /**
     * 数据读写取线程池
     */
    private ThreadPoolExecutor threadPoolExecutor =  new ThreadPoolExecutor(2,5,0, TimeUnit.SECONDS,new ArrayBlockingQueue<>(100),new NamedThreadFactory("dataTransfer"));

    /**
     * 数据缓存队列, 每一个队列代表一个批次
     */
    private List<BlockingQueue<TransferDataRow>> queues = new ArrayList<>();

    /**
     *
     * @param dataTransferContext
     */
    public void transfer(DataTransferContext dataTransferContext) throws IOException, SQLException {
        final ProjectGenerateConfig.DataSourceConfig dataSourceConfig = new ProjectGenerateConfig.DataSourceConfig(dataTransferContext.getSourceConnName(), dataTransferContext.getSourceNamespace(), dataTransferContext.getTableNames());

        // 创建数据缓存队列
        final DataTransferContext.TransferConfig transferConfig = dataTransferContext.getTransferConfig();
        final int cacheBatch = transferConfig.getTableData().getNormalTableReadWriteConfig().getCacheBatch();
        final int cacheBatch1 = transferConfig.getTableData().getBigColumnTableReadWriteConfig().getCacheBatch();
        int createQueues = cacheBatch > cacheBatch1 ? cacheBatch : cacheBatch1;
        if (createQueues < 1){
            // 如果没有配置, 则创建 2 个队列
            createQueues = 2;
        }
        for (int i = 0; i < createQueues; i++) {
            queues.add(new ArrayBlockingQueue<>(1024));
        }

        // 创建读数据线程
        createReadThread(dataSourceConfig, transferConfig);

        // 创建写数据线程
        final ProjectGenerateConfig.DataSourceConfig targetDataSourceConfig = new ProjectGenerateConfig.DataSourceConfig(dataTransferContext.getTargetConnName(), dataTransferContext.getTargetNamespace(), dataTransferContext.getTableNames());
        createWriteThread(targetDataSourceConfig,transferConfig, dataTransferContext.getTableMirrorConfig());

    }

    /**
     * 创建读数据线程
     * @param sourceConfig
     * @param transferConfig
     * @throws IOException
     * @throws SQLException
     */
    public void createReadThread(ProjectGenerateConfig.DataSourceConfig sourceConfig, DataTransferContext.TransferConfig transferConfig) throws IOException, SQLException {
        final DruidDataSource druidDataSource = connDatasourceAdapter.poolDataSource(sourceConfig.getConnName(), sourceConfig.getNamespace());
        final List<String> tableNames = sourceConfig.getTableNames();
        final List<TableMeta> tableMetas = tableSearchService.getTables(sourceConfig.getConnName(), sourceConfig.getNamespace(),sourceConfig.getTableNames());

        final ReadTask readThread = new ReadTask(druidDataSource, tableMetas, transferConfig);
        threadPoolExecutor.submit(readThread);
    }

    /**
     * 创建写数据线程
     * @param targetConfig
     * @param transferConfig
     * @param tableMirrorConfig
     */
    public void createWriteThread(ProjectGenerateConfig.DataSourceConfig targetConfig, DataTransferContext.TransferConfig transferConfig, Map<String,TableMeta> tableMirrorConfig) throws IOException, SQLException {
        final DruidDataSource druidDataSource = connDatasourceAdapter.poolDataSource(targetConfig.getConnName(), targetConfig.getNamespace());

        final WriteTask writeTask = new WriteTask(druidDataSource, transferConfig, tableMirrorConfig);
        threadPoolExecutor.submit(writeTask);
    }

    /**
     * 写数据线程
     */
    public final class WriteTask implements Runnable{
        private DruidDataSource dataSource;
        private DataTransferContext.TransferConfig transferConfig;
        private Map<String,TableMeta> tableMirrorConfig;

        public WriteTask(DruidDataSource dataSource, DataTransferContext.TransferConfig transferConfig, Map<String, TableMeta> tableMirrorConfig) {
            this.dataSource = dataSource;
            this.transferConfig = transferConfig;
            this.tableMirrorConfig = tableMirrorConfig;
        }

        @Override
        public void run() {
            QueryRunner queryRunner = new QueryRunner(dataSource);

            final String dbType = dataSource.getDbType();

            // 暂时只使用一个队列 , 多个队列还不知道怎么弄
            final BlockingQueue<TransferDataRow> transferDataRows = queues.get(0);
            while (true){
                try {
                    final TransferDataRow transferDataRow = transferDataRows.take();

                    final String tableName = transferDataRow.getActualTableName().getTableName();
                    final DataChange dataChange = new DataChange(DiffType.ADD,tableName);
                    DataChange.Insert insert = new DataChange.Insert(transferDataRow.getColumnNames());
                    dataChange.setInsert(insert);

                    final List<TransferDataRow.TransferDataField> fields = transferDataRow.getFields();
                    List<DataChange.ColumnValue> columnValues = new ArrayList<>();
                    insert.setColumnValues(columnValues);
                    for (TransferDataRow.TransferDataField field : fields) {
                        final Column column = field.getColumn();
                        final Object value = field.getValue();
                        columnValues.add(new DataChange.ColumnValue(Objects.toString(value,null),field.getColumnType()));
                    }

                    Template createTableTemplate = configuration.getTemplate("sqls/datachange."+dbType+".sql.ftl");
                    final StringWriter stringWriter = new StringWriter();
                    Map<String,Object> dataModel = new HashMap<>();

                    final BeanMap beanMap = BeanMap.create(dataChange);
                    beanMap.forEach((key,value) -> {
                        dataModel.put((String) key,value);
                    });
                    createTableTemplate.process(dataModel,stringWriter);

                    queryRunner.update(stringWriter.toString());
                } catch (InterruptedException | IOException | TemplateException | SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 数据读取线程
     */
    public final class ReadTask implements Runnable{
        private DruidDataSource dataSource;
        private List<TableMeta> tableMetas = new ArrayList<>();
        private DataTransferContext.TransferConfig transferConfig;

        public ReadTask(DruidDataSource dataSource, List<TableMeta> tableMetas, DataTransferContext.TransferConfig transferConfig){
            this.dataSource = dataSource;
            this.tableMetas = tableMetas;
            this.transferConfig = transferConfig;
        }

        @Override
        public void run() {
            final DataTransferContext.TransferConfig.DataReadWriteConfig bigColumnTableReadWriteConfig = transferConfig.getTableData().getBigColumnTableReadWriteConfig();
            final DataTransferContext.TransferConfig.DataReadWriteConfig normalTableReadWriteConfig = transferConfig.getTableData().getNormalTableReadWriteConfig();

            QueryRunner queryRunner = new QueryRunner(dataSource);

            for (TableMeta tableMeta : tableMetas) {
                // 是否大字段数据表
                final boolean bigColumnTable = isBigColumnTable(tableMeta);
                final Map<String, Column> columnMap = tableMeta.getColumns().stream().collect(Collectors.toMap(Column::getColumnName, Function.identity()));

                final String tableName = tableMeta.getTable().getActualTableName().getTableName();
                // 查询当前表数据量
                String sql = "select count(*) from "+tableName;
                try {
                    final Long count = queryRunner.query(sql, new ScalarHandler<Long>());
                    log.info("将读取数据表[{}]数据量[{}]",tableMeta.getTable().getTableName(),count);

                    // 读取, 每 1000 条一批
                    String readSql = "select * from "+tableMeta.getTable().getTableName();
                    final MapListHandler columnListHandler = new MapListHandler();
                    final List<Map<String, Object>> query = queryRunner.query(readSql, columnListHandler);
                    for (Map<String, Object> stringObjectMap : query) {
                        final TransferDataRow transferDataRow = new TransferDataRow(tableMeta.getTable().getActualTableName());
                        final Iterator<Map.Entry<String, Object>> iterator = stringObjectMap.entrySet().iterator();
                        while (iterator.hasNext()){
                            final Map.Entry<String, Object> entry = iterator.next();
                            final String columnName = entry.getKey();
                            final Object value = entry.getValue();
                            final TransferDataRow.TransferDataField transferDataField = new TransferDataRow.TransferDataField(columnMap.get(columnName), value);
                            transferDataRow.addField(transferDataField);
                        }

                        queues.get(0).offer(transferDataRow);
                    }
                } catch (SQLException throwables) {
                    log.error("查询数据表[{}]数据量时出错:",tableName,throwables.getMessage(),throwables);
                }

            }
        }

        /**
         * 是否是大字段表
         * @param tableMeta
         * @return
         */
        private boolean isBigColumnTable(TableMeta tableMeta){
            final List<Column> columns = tableMeta.getColumns();
            for (Column column : columns) {
                final int dataType = column.getDataType();
                if (dataType == 2004){
                    return true;
                }
            }
            return false;
        }
    }
}
