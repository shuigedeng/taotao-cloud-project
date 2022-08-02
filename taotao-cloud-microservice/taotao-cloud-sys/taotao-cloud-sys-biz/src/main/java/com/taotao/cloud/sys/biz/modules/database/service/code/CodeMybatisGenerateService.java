package com.taotao.cloud.sys.biz.modules.database.service.code;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.sanri.tools.modules.core.service.file.FileManager;
import com.sanri.tools.modules.database.service.TableSearchService;
import com.sanri.tools.modules.database.service.code.dtos.ProjectGenerateConfig;
import com.sanri.tools.modules.database.service.code.dtos.MapperBuildConfig;
import com.sanri.tools.modules.database.service.code.dtos.MybatisPlusBuildConfig;
import com.sanri.tools.modules.database.service.connect.ConnDatasourceAdapter;
import com.sanri.tools.modules.database.service.connect.dtos.DatabaseConnect;
import com.sanri.tools.modules.database.service.dtos.meta.TableMeta;
import com.sanri.tools.modules.database.service.meta.dtos.ActualTableName;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.config.*;
import org.mybatis.generator.internal.NullProgressCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.*;

@Service
@Slf4j
public class CodeMybatisGenerateService {
    @Autowired
    private FileManager fileManager;
    @Autowired
    private ConnDatasourceAdapter connDatasourceAdapter;
    @Autowired
    private TableSearchService tableSearchService;

    /**
     * 使用 mybatis-generator 生成 mapper.xml mapper.java entity.java 文件
     * 配置一个插件就支持了 tk.mybatis
     * @return
     */
    public File mapperBuild(MapperBuildConfig mapperBuildConfig) throws IOException, SQLException, InterruptedException {
        ProjectGenerateConfig.DataSourceConfig dataSourceConfig = mapperBuildConfig.getDataSourceConfig();
        ProjectGenerateConfig.PackageConfig packageConfig = mapperBuildConfig.getPackageConfig();
        MapperBuildConfig.FilesConfig filesConfig = mapperBuildConfig.getFilesConfig();

        // context
        Context context = new Context(ModelType.valueOf(mapperBuildConfig.getModelType()));
        context.setId(RandomStringUtils.randomAlphanumeric(3));
        context.setTargetRuntime(mapperBuildConfig.getTargetRunTime());

        // jdbc
        String connName = dataSourceConfig.getConnName();
        final DatabaseConnect databaseConnect = connDatasourceAdapter.databaseConnect(connName);

        JDBCConnectionConfiguration jdbcConnectionConfiguration = new JDBCConnectionConfiguration();
        String url = databaseConnect.getUrl();
        final String keyValueProperties = ConnDatasourceAdapter.keyValueProperties(databaseConnect.getUrl());
        if (StringUtils.isNotBlank(keyValueProperties)){
            url += "?"+keyValueProperties;
        }
        jdbcConnectionConfiguration.setDriverClass(databaseConnect.getDriverClassName());
        jdbcConnectionConfiguration.setConnectionURL(url);
        jdbcConnectionConfiguration.setPassword(databaseConnect.getPassword());
        jdbcConnectionConfiguration.setUserId(databaseConnect.getUsername());
        context.setJdbcConnectionConfiguration(jdbcConnectionConfiguration);

        // java model
        if (filesConfig.isEntity()) {
            JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = new JavaModelGeneratorConfiguration();
            javaModelGeneratorConfiguration.setTargetPackage(packageConfig.getEntity());
            context.setJavaModelGeneratorConfiguration(javaModelGeneratorConfiguration);
        }

        // sqlmap
        if (filesConfig.isXml()) {
            SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = new SqlMapGeneratorConfiguration();
            sqlMapGeneratorConfiguration.setTargetPackage(packageConfig.getMapper());
            context.setSqlMapGeneratorConfiguration(sqlMapGeneratorConfiguration);
        }

        // javaclient
        if (filesConfig.isMapper()) {
            JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = new JavaClientGeneratorConfiguration();
            javaClientGeneratorConfiguration.setTargetPackage(packageConfig.getMapper());
            javaClientGeneratorConfiguration.setImplementationPackage(packageConfig.getMapper());
            javaClientGeneratorConfiguration.setConfigurationType(mapperBuildConfig.getJavaClientType());
            context.setJavaClientGeneratorConfiguration(javaClientGeneratorConfiguration);
        }

        // plugins
        List<MapperBuildConfig.PluginConfig> pluginConfigs = mapperBuildConfig.getPluginConfigs();
        for (MapperBuildConfig.PluginConfig pluginConfig : pluginConfigs) {
            PluginConfiguration pluginConfiguration = new PluginConfiguration();
            pluginConfiguration.setConfigurationType(pluginConfig.getType());
            Map<String, String> properties = pluginConfig.getProperties();
            properties.forEach((k,v) -> pluginConfiguration.addProperty(k,v));
            context.addPluginConfiguration(pluginConfiguration);
        }

        // table configurations
        final List<TableMeta> tableMetaDataList = tableSearchService.getTables(connName, dataSourceConfig.getNamespace(), dataSourceConfig.getTableNames());
        for (TableMeta tableMetaData : tableMetaDataList) {
            TableConfiguration tableConfiguration = new TableConfiguration(context);
            ActualTableName actualTableName = tableMetaData.getTable().getActualTableName();
            tableConfiguration.setCatalog(actualTableName.getCatalog());
            tableConfiguration.setSchema(actualTableName.getSchema());
            tableConfiguration.setTableName(actualTableName.getTableName());
            context.addTableConfiguration(tableConfiguration);
        }

        // generate files
        List<GeneratedJavaFile> generatedJavaFiles = new ArrayList<>();
        List<GeneratedXmlFile> generatedXmlFiles = new ArrayList<>();
        ProgressCallback callback = new NullProgressCallback();
        List<String> warnings = new ArrayList<>();
        Set<String> fullyQualifiedTableNames = new HashSet<>();
        context.introspectTables(callback, warnings,fullyQualifiedTableNames);
        context.generateFiles(callback, generatedJavaFiles, generatedXmlFiles, warnings);

        // 写入生成的数据到文件
        String projectName = mapperBuildConfig.getProjectName();
        File src = fileManager.mkTmpDir(CodeGenerateService.BASE_GENERATE_DIR + "mapper/" + projectName + System.currentTimeMillis());
        for (GeneratedJavaFile generatedJavaFile : generatedJavaFiles) {
            String targetPackage = generatedJavaFile.getTargetPackage();
            File director = getDirector(src, targetPackage);

            File targetFile = new File(director, generatedJavaFile.getFileName());
            FileUtils.writeStringToFile(targetFile, generatedJavaFile.getFormattedContent(), StandardCharsets.UTF_8);
        }

        for (GeneratedXmlFile generatedXmlFile : generatedXmlFiles) {
            String targetPackage = generatedXmlFile.getTargetPackage();
            File director = getDirector(src, targetPackage);

            File targetFile = new File(director, generatedXmlFile.getFileName());
            FileUtils.writeStringToFile(targetFile, generatedXmlFile.getFormattedContent(), StandardCharsets.UTF_8);
        }

        return src;
    }

    /**
     * mybatis plus 代码生成
     * @return
     */
    public File mybatisPlusBuild(MybatisPlusBuildConfig mybatisPlusBuildConfig) throws IOException {
        AutoGenerator mpg = new AutoGenerator();
        File src = fileManager.mkTmpDir(CodeGenerateService.BASE_GENERATE_DIR + "mybatisplus/" + mybatisPlusBuildConfig.getProjectName() + System.currentTimeMillis());

        final GlobalConfig globalConfig = mybatisPlusBuildConfig.getGlobalConfig();
        globalConfig.setAuthor(System.getProperty("user.name"));
        globalConfig.setOutputDir(src.getAbsolutePath());
        globalConfig.setOpen(false);
        globalConfig.setFileOverride(true);
        mpg.setGlobalConfig(globalConfig);

        final ProjectGenerateConfig.DataSourceConfig dsConfig = mybatisPlusBuildConfig.getDataSourceConfig();
        final DatabaseConnect databaseConnect = connDatasourceAdapter.databaseConnect(dsConfig.getConnName());
        final DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl(dataSourceConfig.getUrl());
        dataSourceConfig.setUsername(dataSourceConfig.getUsername());
        dataSourceConfig.setPassword(dataSourceConfig.getPassword());
        mpg.setDataSource(dataSourceConfig);

        mpg.setPackageInfo(mybatisPlusBuildConfig.getPackageConfig());

        mpg.setStrategy(mybatisPlusBuildConfig.getStrategyConfig());
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());

        mpg.execute();

        return src;
    }

    private File getDirector(File base,String targetPackage){
        StringBuilder sb = new StringBuilder();
        StringTokenizer st = new StringTokenizer(targetPackage, "."); //$NON-NLS-1$
        while (st.hasMoreTokens()) {
            sb.append(st.nextToken());
            sb.append(File.separatorChar);
        }
        File directory = new File(base, sb.toString());
        if (!directory.exists()){
            directory.mkdirs();
        }
        return directory;
    }
}
