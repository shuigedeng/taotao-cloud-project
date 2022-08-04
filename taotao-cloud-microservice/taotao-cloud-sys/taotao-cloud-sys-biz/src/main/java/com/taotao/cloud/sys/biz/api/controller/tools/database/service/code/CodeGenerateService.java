package com.taotao.cloud.sys.biz.api.controller.tools.database.service.code;


import com.taotao.cloud.sys.biz.api.controller.tools.database.service.code.dtos.JavaBeanInfo;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.dtos.meta.TableMeta;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.dtos.meta.TableMetaData;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.*;

import com.taotao.cloud.sys.biz.api.controller.tools.core.service.file.FileManager;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.JdbcMetaService;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.TableSearchService;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.code.dtos.JavaBeanBuildConfig;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.code.rename.RenameStrategy;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.aspect.JdbcConnection;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;


/**
 * 代码生成
 */
@Service
@Slf4j
public class CodeGenerateService {
    @Autowired
    private TableSearchService tableSearchService;
    @Autowired
    private JdbcMetaService jdbcMetaService;

    @Autowired
    private FileManager fileManager;

    @Autowired
    private Configuration configuration;

    /**
     * 生成代码的目录
     */
    public static final String BASE_GENERATE_DIR = "code/generate/";

    @Autowired(required = false)
    private Map<String, RenameStrategy> renameStrategyMap = new HashMap<>();

    /**
     * 所有的命名策略
     * @return
     */
    public Set<String> renameStrategies(){
        return renameStrategyMap.keySet();
    }

    /**
     * 数据表生成 javaBean
     * 支持 swagger , lombok , persistence-api
     * @param connName
     * @param catalog
     * @param schema
     * @throws IOException
     * @throws SQLException
     */
    @JdbcConnection
    public File javaBeanBuild(JavaBeanBuildConfig javaBeanBuildConfig) throws IOException, SQLException {
        String connName = javaBeanBuildConfig.getConnName();
        final List<TableMeta> tableMetas = tableSearchService.getTables(connName, javaBeanBuildConfig.getNamespace(), javaBeanBuildConfig.getTableNames());
        final List<TableMetaData> filterTables = jdbcMetaService.tablesExtend(connName, tableMetas);

        return generateCode(javaBeanBuildConfig, filterTables);
    }

    /**
     * 根据数据表的元数据信息, 生成数据表
     * @param javaBeanBuildConfig
     * @param filterTables
     * @return
     * @throws IOException
     */
    public File generateCode(JavaBeanBuildConfig javaBeanBuildConfig, List<TableMetaData> filterTables) throws IOException {
        // 获取重命名工具
        String renameStrategy = javaBeanBuildConfig.getRenameStrategy();
        RenameStrategy renameStrategyImpl = renameStrategyMap.get(renameStrategy);

        File javaBeanDir = fileManager.mkTmpDir(BASE_GENERATE_DIR + "javabean" + System.currentTimeMillis());

        // 对过滤出来的表生成 javaBean
        for (TableMetaData filterTable : filterTables) {
            JavaBeanInfo javaBeanInfo = renameStrategyImpl.mapping(filterTable);
            Template entityTemplate = configuration.getTemplate("code/entity.java.ftl");
            Map<String, Object> context = new HashMap<>();
            context.put("bean", javaBeanInfo);
            context.put("beanConfig", javaBeanBuildConfig);
            context.put("table", filterTable);
            context.put("author", System.getProperty("user.name"));
            context.put("date", DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.format(System.currentTimeMillis()));
            context.put("time", DateFormatUtils.ISO_8601_EXTENDED_TIME_FORMAT.format(System.currentTimeMillis()));
            // 准备 context
            File entityFile = new File(javaBeanDir, javaBeanInfo.getClassName() + ".java");
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(entityFile));
            try {
                entityTemplate.process(context, outputStreamWriter);
            } catch (TemplateException e) {
                log.error("javaBeanBuild template error : {}",e.getMessage(),e);
            } finally {
                IOUtils.closeQuietly(outputStreamWriter);
            }
        }
        return javaBeanDir;
    }

}
