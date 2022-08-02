package com.taotao.cloud.sys.biz.modules.database.service.code.dtos;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.sanri.tools.modules.database.service.meta.dtos.ActualTableName;

import lombok.Data;

@Data
public class PreviewCodeParam {
    @NotNull
    private String template;
    @NotNull
    private String connName;
    @Valid
    private ActualTableName actualTableName;
    private String renameStrategyName;
    @Valid
    private ProjectGenerateConfig.PackageConfig packageConfig;

    public PreviewCodeParam() {
    }

    public PreviewCodeParam(String template, String connName, ActualTableName actualTableName, String renameStrategyName) {
        this.template = template;
        this.connName = connName;
        this.actualTableName = actualTableName;
        this.renameStrategyName = renameStrategyName;
    }
}
