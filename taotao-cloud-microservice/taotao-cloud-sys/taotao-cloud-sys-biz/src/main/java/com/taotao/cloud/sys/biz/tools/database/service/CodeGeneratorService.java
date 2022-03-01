package com.taotao.cloud.sys.biz.tools.database.service;

import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.sys.biz.tools.core.service.file.FileManager;
import com.taotao.cloud.sys.biz.tools.database.dtos.CodeGeneratorConfig;
import com.taotao.cloud.sys.biz.tools.database.dtos.CodeGeneratorParam;
import com.taotao.cloud.sys.biz.tools.database.dtos.ConnectionMetaData;
import com.taotao.cloud.sys.biz.tools.database.dtos.JavaBeanBuildConfig;
import com.taotao.cloud.sys.biz.tools.database.dtos.MapperBuildConfig;
import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.ActualTableName;
import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.TableMetaData;
import com.taotao.cloud.sys.biz.tools.database.service.rename.JavaBeanInfo;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.*;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.config.*;
import org.mybatis.generator.internal.NullProgressCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;


import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public class CodeGeneratorService {
    @Autowired
    private JdbcService jdbcService;

    @Autowired
    private FileManager fileManager;

    @Autowired
    private Configuration configuration;

    // 生成代码的目录
    private static final String BASE_GENERATE_DIR = "code/generate/";

    @Autowired(required = false)
    private Map<String,RenameStrategy> renameStrategyMap = new HashMap<>();

    /**
     * 所有的命名策略
     * @return
     */
    public Set<String> renameStrategies(){
        return renameStrategyMap.keySet();
    }

    /** 使用模板生成代码 **/
    @Autowired
    private TemplateService templateService;

    /**
     * 使用某一张表进行代码的预览
     * 模板代码预览
     * @param previewCodeParam
     */
    public String previewCode(PreviewCodeParam previewCodeParam) throws IOException, SQLException, TemplateException {
        // 获取表元数据
        List<TableMetaData> tableMetaData = jdbcService.filterChoseTables(previewCodeParam.getConnName(), previewCodeParam.getActualTableName().getCatalog(), Collections.singletonList(previewCodeParam.getActualTableName()));
        if (!CollectionUtils.isEmpty(tableMetaData)){
            TableMetaData previewTable = tableMetaData.get(0);
            RenameStrategy renameStrategy = renameStrategyMap.get(previewCodeParam.getRenameStrategyName());
            // 生成代码
            return templateService.preview(previewCodeParam,previewTable,renameStrategy);
        }
        return "";
    }

    /**
     * 使用模板方案代码生成
     * @param template
     * @param connName
     * @param actualTableName
     * @param renameStrategyName
     * @return
     */
    public Path codeGenerator(CodeGeneratorParam codeGeneratorParam) throws IOException, SQLException, TemplateException {
        CodeGeneratorConfig.DataSourceConfig dataSourceConfig = codeGeneratorParam.getDataSourceConfig();
        String connName = dataSourceConfig.getConnName();
        String catalog = dataSourceConfig.getCatalog();
        List<TableMetaData> filterTables = jdbcService.filterChoseTables(connName, catalog, dataSourceConfig.getTables());

        String renameStrategyName = codeGeneratorParam.getRenameStrategyName();
        RenameStrategy renameStrategy = renameStrategyMap.get(renameStrategyName);

        File file = templateService.processBatch(codeGeneratorParam,filterTables,renameStrategy);
        return fileManager.relativePath(file.toPath());
    }

    /**
     * 直接构建一个项目
     * @param codeGeneratorConfig
     * @return
     * @throws IOException
     * @throws SQLException
     * @throws InterruptedException
     */
    public File projectBuild(CodeGeneratorConfig codeGeneratorConfig) throws IOException, SQLException, InterruptedException {
        File targetDir = fileManager.mkTmpDir(BASE_GENERATE_DIR+"buildSpringBoot");

        //项目基本目录
        File projectDir = new File(targetDir, codeGeneratorConfig.getProjectName()+System.currentTimeMillis());
        CodeGeneratorConfig.GlobalConfig globalConfig = codeGeneratorConfig.getGlobalConfig();
        CodeGeneratorConfig.PackageConfig packageConfig = codeGeneratorConfig.getPackageConfig();

        //自己生成 maven 骨架
        File javaDir = new File(projectDir, "src/main/java");javaDir.mkdirs();
        File resourcesDir = new File(projectDir, "src/main/resources");resourcesDir.mkdirs();
        File testJavaDir = new File(projectDir, "src/test/java");testJavaDir.mkdirs();
        File testResourcesDir = new File(projectDir, "src/test/resources");testResourcesDir.mkdirs();

        // 生成 service,controller,vo,dto,param
        mkdirs(javaDir, packageConfig);
        File entityDir = new File(javaDir,StringUtils.replace(packageConfig.getEntity(),".","/"));

        // 准备数据源,获取表元数据
        CodeGeneratorConfig.DataSourceConfig dataSourceConfig = codeGeneratorConfig.getDataSourceConfig();
        String connName = dataSourceConfig.getConnName();
        String catalog = dataSourceConfig.getCatalog();
        List<TableMetaData> tableMetaDataList = jdbcService.filterChoseTables(connName, catalog, dataSourceConfig.getTables());

        if (tableMetaDataList.size() > 0) {
            // 生成实体 Bean
            JavaBeanInfo javaBeanInfo = new JavaBeanInfo();
        }

        return projectDir;
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
    public File javaBeanBuild(JavaBeanBuildConfig javaBeanBuildConfig) throws IOException, SQLException {
        String connName = javaBeanBuildConfig.getConnName();
        String catalog = javaBeanBuildConfig.getCatalog();
        List<TableMetaData> filterTables = jdbcService.filterChoseTables(connName, catalog, javaBeanBuildConfig.getTables());

        // 获取重命名工具
        String renameStrategy = javaBeanBuildConfig.getRenameStrategy();
        RenameStrategy renameStrategyImpl = renameStrategyMap.get(renameStrategy);

        File javaBeanDir = fileManager.mkTmpDir(BASE_GENERATE_DIR + "javabean" + System.currentTimeMillis());

        // 对过滤出来的表生成 javaBean
        for (TableMetaData filterTable : filterTables) {
            JavaBeanInfo javaBeanInfo = renameStrategyImpl.mapping(filterTable);
            Template entityTemplate = configuration.getTemplate("code/entity.xml.ftl");
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
                LogUtil.error("javaBeanBuild template error : {}",e.getMessage(),e);
            } finally {
                IOUtils.closeQuietly(outputStreamWriter);
            }
        }

        return javaBeanDir;
    }

    /**
     * 使用 mybatis-generator 生成 mapper.xml mapper.java entity.java 文件
     * 配置一个插件就支持了 tk.mybatis
     * @return
     */
    public File mapperBuild(MapperBuildConfig mapperBuildConfig) throws IOException, SQLException, InterruptedException {
        CodeGeneratorConfig.DataSourceConfig dataSourceConfig = mapperBuildConfig.getDataSourceConfig();
        CodeGeneratorConfig.PackageConfig packageConfig = mapperBuildConfig.getPackageConfig();
        MapperBuildConfig.FilesConfig filesConfig = mapperBuildConfig.getFilesConfig();

        // context
        Context context = new Context(ModelType.valueOf(mapperBuildConfig.getModelType()));
        context.setId(RandomStringUtils.randomAlphanumeric(3));
        context.setTargetRuntime(mapperBuildConfig.getTargetRunTime());

        // jdbc
        String connName = dataSourceConfig.getConnName();
        ConnectionMetaData connectionMetaData = jdbcService.connectionMetaData(connName);
        JDBCConnectionConfiguration jdbcConnectionConfiguration = new JDBCConnectionConfiguration();
        jdbcConnectionConfiguration.setDriverClass(connectionMetaData.getDriverClass());
        jdbcConnectionConfiguration.setConnectionURL(connectionMetaData.getConnectionURL());
        jdbcConnectionConfiguration.setPassword(connectionMetaData.getAuthParam().getPassword());
        jdbcConnectionConfiguration.setUserId(connectionMetaData.getAuthParam().getUsername());
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
        List<TableMetaData> tableMetaDataList = jdbcService.filterChoseTables(connName, dataSourceConfig.getCatalog(), dataSourceConfig.getTables());
        for (TableMetaData tableMetaData : tableMetaDataList) {
            TableConfiguration tableConfiguration = new TableConfiguration(context);
            ActualTableName actualTableName = tableMetaData.getActualTableName();
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
        File src = fileManager.mkTmpDir(BASE_GENERATE_DIR + "mapper/" + projectName + System.currentTimeMillis());
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

    /**
     * 用于创建 PackageConfig 中的所有目录 , 基于基本目录 {@param javaDir}
     * @param javaDir
     * @param packageConfig
     */
    private void mkdirs(File javaDir, CodeGeneratorConfig.PackageConfig packageConfig) {
        PropertyDescriptor[] beanGetters = ReflectUtils.getBeanGetters(CodeGeneratorConfig.PackageConfig.class);
        for (int i = 0; i < beanGetters.length; i++) {
            Method readMethod = beanGetters[i].getReadMethod();
            String path = Objects.toString(ReflectionUtils.invokeMethod(readMethod, packageConfig));
            String[] split = StringUtils.split(path, '.');
            StringBuffer currentPath = new StringBuffer();
            for (String partPath : split) {
                currentPath.append("/").append(partPath);
                File dir = new File(javaDir, currentPath.toString());
                if(!dir.exists()){
                    LogUtil.info("创建目录 : {} ",dir);
                    dir.mkdir();
                }
            }
        }
    }



//    @PostConstruct
//    public void register(){
//        pluginManager.register(PluginDto.builder()
//                .module(MODULE).name("codeGenerate").author("sanri").envs("default")
//                .logo("mysql.jpg")
//                .desc("代码生成功能")
//                .help("代码生成.md")
//                .build());
//    }
}
