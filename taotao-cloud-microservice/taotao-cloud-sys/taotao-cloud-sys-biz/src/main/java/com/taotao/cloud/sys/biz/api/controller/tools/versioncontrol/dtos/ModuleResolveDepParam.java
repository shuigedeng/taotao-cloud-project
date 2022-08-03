package com.taotao.cloud.sys.biz.api.controller.tools.versioncontrol.dtos;

import lombok.Data;

@Data
public class ModuleResolveDepParam {
    private ProjectLocation projectLocation;
    private String relativePomFile;
    private String settings;
}
