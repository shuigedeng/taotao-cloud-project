package com.taotao.cloud.sys.biz.api.controller.tools.versioncontrol.dtos;

import lombok.Data;

@Data
public class MavenCompileParam {
    private String settings;
    private ChoseCommits choseCommits;
}
