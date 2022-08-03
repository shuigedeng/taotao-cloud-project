package com.taotao.cloud.sys.biz.api.controller.tools.versioncontrol.dtos;

import lombok.Data;

@Data
public class ModuleLocation {
    private ProjectLocation projectLocation;
    private String relativePath;
}
