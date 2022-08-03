package com.taotao.cloud.sys.biz.api.controller.tools.database.service;

import com.taotao.cloud.sys.biz.api.controller.tools.core.service.file.FileManager;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.dtos.search.SearchParam;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.aspect.JdbcConnection;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MetaDocService {
    @Autowired
    private JdbcMetaService jdbcMetaService;
    @Autowired
    private TableSearchService tableSearchService;

    @Autowired
    private FileManager fileManager;

    @Autowired
    private Configuration configuration;

    /**
     * 元数据文档导出
     * @param connName
     * @param searchParam
     * @return
     */
    @JdbcConnection
    public File metaDoc(String connName, SearchParam searchParam) throws IOException, SQLException, TemplateException {
        final List<TableMeta> tableMetas = tableSearchService.searchTables(connName, searchParam);

        final String catalog = searchParam.getCatalog();
        final List<String> schemas = searchParam.getSchemas();

        final List<TableMetaData> tableMetaData = jdbcMetaService.tablesExtend(connName, tableMetas);
        final File docDir = fileManager.mkTmpDir("doc/database");
        final File outputFile = new File(docDir, connName + "_" + catalog + "_" + StringUtils.join(schemas, "+") + System.currentTimeMillis() + ".md");
        try(final FileWriter fileWriter = new FileWriter(outputFile)){
            Template template = configuration.getTemplate("database-doc.md.ftl");
            Map<String,Object> dataModel = new HashMap<>();
            dataModel.put("connName",connName);
            dataModel.put("catalog",catalog);
            dataModel.put("schemas",schemas);
            dataModel.put("date", DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.format(System.currentTimeMillis()));
            dataModel.put("time", DateFormatUtils.ISO_8601_EXTENDED_TIME_FORMAT.format(System.currentTimeMillis()));
            dataModel.put("author", System.getProperty("user.name"));
            dataModel.put("tables",tableMetaData);
            template.process(dataModel,fileWriter);
        }
        return outputFile;
    }
}
