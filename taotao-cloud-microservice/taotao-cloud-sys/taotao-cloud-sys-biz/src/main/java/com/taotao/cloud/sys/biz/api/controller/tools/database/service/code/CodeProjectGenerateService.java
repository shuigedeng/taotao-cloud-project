package com.taotao.cloud.sys.biz.api.controller.tools.database.service.code;

import com.taotao.cloud.sys.biz.api.controller.tools.core.service.file.FileManager;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.TableSearchService;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.code.dtos.ProjectGenerateConfig;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.code.frameworkcode.ConfigData;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.code.frameworkcode.GenerateFrameCode;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.dtos.meta.TableMeta;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.*;

@Service
@Slf4j
public class CodeProjectGenerateService {
    @Autowired
    private FileManager fileManager;
    @Autowired
    private TableSearchService tableSearchService;
    @Autowired
    private CodeTemplateService codeTemplateService;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired(required = false)
    private Map<String, GenerateFrameCode> generateFrameCodeMap = new HashMap<>();

    /**
     * 支持的依赖项列表
     * @return
     */
    public List<ProjectGenerateConfig.Dependency> supportDependencies() throws IOException {
        List<ProjectGenerateConfig.Dependency> dependencies = new ArrayList<>();

        final Resource resource = applicationContext.getResource("classpath:dependencies.conf");
        final String value = IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
        final String[] split = StringUtils.split(value, "\n");
        for (String line : split) {
            final String[] items = StringUtils.split(line, "|");
            final ProjectGenerateConfig.Dependency dependency = new ProjectGenerateConfig.Dependency(items[0], items[1], items[3]);
            dependencies.add(dependency);
        }
        return dependencies;
    }

    /**
     * 直接构建一个项目
     * @param codeGeneratorConfig
     * @return
     * @throws IOException
     * @throws SQLException
     * @throws InterruptedException
     */
    public File projectBuild(ProjectGenerateConfig codeGeneratorConfig) throws IOException, SQLException, InterruptedException, TemplateException {
        File targetDir = fileManager.mkTmpDir(CodeGenerateService.BASE_GENERATE_DIR+"buildSpringBoot");

        //项目基本目录
        File projectDir = new File(targetDir, codeGeneratorConfig.getProjectName()+System.currentTimeMillis());
        ProjectGenerateConfig.PackageConfig packageConfig = codeGeneratorConfig.getPackageConfig();

        //自己生成 maven 骨架
        File javaDir = new File(projectDir, "src/main/java");javaDir.mkdirs();
        File resourcesDir = new File(projectDir, "src/main/resources");resourcesDir.mkdirs();
        File testJavaDir = new File(projectDir, "src/test/java");testJavaDir.mkdirs();
        File testResourcesDir = new File(projectDir, "src/test/resources");testResourcesDir.mkdirs();

        // 生成 service,controller,vo,dto,param
        mkdirs(javaDir, packageConfig);
        File entityDir = new File(javaDir, StringUtils.replace(packageConfig.getEntity(),".","/"));

        // 写入 pom 文件
        Map<String,Object> pomDataModel = new HashMap<>();
        pomDataModel.put("mavenConfig",codeGeneratorConfig.getMavenConfig());
        final String pomCode = codeTemplateService.templateCode("code/pom.xml.ftl", pomDataModel);
        final File pomFile = new File(projectDir, "pom.xml");
        FileUtils.writeStringToFile(pomFile,pomCode, StandardCharsets.UTF_8);

        // 准备数据源,获取表元数据
        ProjectGenerateConfig.DataSourceConfig dataSourceConfig = codeGeneratorConfig.getDataSourceConfig();
        String connName = dataSourceConfig.getConnName();
        final List<TableMeta> tableMetas = tableSearchService.getTables(connName, dataSourceConfig.getNamespace(), dataSourceConfig.getTableNames());

        // 生成相应框架代码
        final ConfigData configData = new ConfigData(codeGeneratorConfig, projectDir, javaDir, resourcesDir, tableMetas);
        final List<ProjectGenerateConfig.Dependency> dependencies = codeGeneratorConfig.getMavenConfig().getDependencies();
        for (ProjectGenerateConfig.Dependency dependency : dependencies) {
            final ProjectGenerateConfig.Gav gav = dependency.getGav();
            final String artifactId = gav.getArtifactId();
            final GenerateFrameCode generateFrameCode = generateFrameCodeMap.get("code_" + artifactId);
            if (generateFrameCode != null){
                try {
                    generateFrameCode.generate(configData);
                } catch (Exception e) {
                    log.error("当前框架[{}]生成代码时异常: {}",artifactId,e.getMessage(),e);
                }
            }
        }

        return projectDir;
    }

    /**
     * 用于创建 PackageConfig 中的所有目录 , 基于基本目录 {@param javaDir}
     * @param javaDir
     * @param packageConfig
     */
    private void mkdirs(File javaDir, ProjectGenerateConfig.PackageConfig packageConfig) {
        PropertyDescriptor[] beanGetters = ReflectUtils.getBeanGetters(ProjectGenerateConfig.PackageConfig.class);
        for (int i = 0; i < beanGetters.length; i++) {
            Method readMethod = beanGetters[i].getReadMethod();
            String path = Objects.toString(ReflectionUtils.invokeMethod(readMethod, packageConfig));
            String[] split = StringUtils.split(path, '.');
            StringBuffer currentPath = new StringBuffer();
            for (String partPath : split) {
                currentPath.append("/").append(partPath);
                File dir = new File(javaDir, currentPath.toString());
                if(!dir.exists()){
                    log.info("创建目录 : {} ",dir);
                    dir.mkdir();
                }
            }
        }
    }

}
