package com.taotao.cloud.sys.biz.modules.database.service.code.frameworkcode;

import com.taotao.cloud.sys.biz.modules.database.service.code.CodeMybatisGenerateService;
import com.taotao.cloud.sys.biz.modules.database.service.code.dtos.MapperBuildConfig;
import com.taotao.cloud.sys.biz.modules.database.service.code.dtos.ProjectGenerateConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

@Component("code_mybatis-spring-boot-starter")
@Slf4j
public class MybatisGenerateCode implements GenerateFrameCode {
    @Autowired
    private CodeMybatisGenerateService codeMybatisGenerateService;

    @Override
    public void generate(ConfigData configData) throws Exception {
        final ProjectGenerateConfig projectGenerateConfig = configData.getProjectGenerateConfig();
        MapperBuildConfig mapperBuildConfig = new MapperBuildConfig();
        mapperBuildConfig.setDataSourceConfig(projectGenerateConfig.getDataSourceConfig());
        mapperBuildConfig.setPackageConfig(projectGenerateConfig.getPackageConfig());
        mapperBuildConfig.setProjectName(configData.getProjectDir().getName());
        final File mapperBuild = codeMybatisGenerateService.mapperBuild(mapperBuildConfig);
        final ProjectGenerateConfig.PackageConfig packageConfig = projectGenerateConfig.getPackageConfig();
        final File targetDir = ProjectGenerateConfig.PackageConfig.targetDir(configData.getJavaDir(), packageConfig.getMapper());
        FileUtils.copyDirectory(mapperBuild,targetDir);
    }
}
