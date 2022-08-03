package com.taotao.cloud.sys.biz.api.controller.tools.database.service;

import com.taotao.cloud.sys.biz.api.controller.tools.database.service.code.dtos.ProjectGenerateConfig;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.aspect.JdbcConnection;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class MetaConvertService {

    @Autowired
    private TableSearchService tableSearchService;

    @Autowired
    private JdbcMetaService jdbcMetaService;

    @Autowired
    private Configuration configuration;

    /**
     * 批量转换数据表 ddl 语句
     * @param dataSourceConfig 数据源配置
     * @param convertType 需要输出的 ddl 数据库类型
     * @return
     */
    @JdbcConnection
    public List<String> batchConvertTableDDL(ProjectGenerateConfig.DataSourceConfig dataSourceConfig, String convertType) throws IOException, SQLException, TemplateException {
        Template alterTableTemplate = configuration.getTemplate("sqls/altertable."+convertType+".ftl");

        final List<TableMeta> tables = tableSearchService.getTables(dataSourceConfig.getConnName(), dataSourceConfig.getNamespace(), dataSourceConfig.getTableNames());
        final List<TableMetaData> tableMetaDatas = jdbcMetaService.tablesExtend(dataSourceConfig.getConnName(), tables);

        List<String> ddlSqls = new ArrayList<>();

        for (TableMetaData tableMetaData : tableMetaDatas) {
            Map<String,Object> dataModel = new HashMap<>();
            final StringWriter stringWriter = new StringWriter();
            dataModel.put("diffType","ADD");
            dataModel.put("meta",tableMetaData);
            dataModel.put("actualTableName",tableMetaData.getActualTableName());
            alterTableTemplate.process(dataModel,stringWriter);

            ddlSqls.add(stringWriter.toString());
        }

        return ddlSqls;

    }
}
