package com.taotao.cloud.sys.biz.api.controller.tools.database.service.code.dtos;

import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MybatisPlusBuildConfig {
    /**
     * 项目名
     */
    @NotNull
    private String projectName;
    private GlobalConfig globalConfig;
    private ProjectGenerateConfig.DataSourceConfig dataSourceConfig;
    private PackageConfig packageConfig;
    private StrategyConfig strategyConfig;
}
