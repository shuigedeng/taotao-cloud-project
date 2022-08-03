package com.taotao.cloud.sys.biz.api.controller.tools.core.dtos.param;

import lombok.Data;

@Data
public class GitParam extends AbstractConnectParam{
    private AuthParam authParam;
    private String sshKey;
    private String mavenHome;
    private String mavenConfigFilePath;
}
