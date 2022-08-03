package com.taotao.cloud.sys.biz.api.controller.tools.database.service.code.dtos;

import com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.dtos.Namespace;
import lombok.Data;

@Data
public class CodeFromSqlParam {
    private String connName;
    private Namespace namespace;
    private String sql;
    private ProjectGenerateConfig.PackageConfig packageConfig;
    private String bizName;
}
