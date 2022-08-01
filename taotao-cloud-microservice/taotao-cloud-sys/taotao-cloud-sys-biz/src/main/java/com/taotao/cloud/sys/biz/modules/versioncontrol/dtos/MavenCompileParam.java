package com.taotao.cloud.sys.biz.modules.versioncontrol.dtos;

import lombok.Data;

@Data
public class MavenCompileParam {
    private String settings;
    private ChoseCommits choseCommits;
}
