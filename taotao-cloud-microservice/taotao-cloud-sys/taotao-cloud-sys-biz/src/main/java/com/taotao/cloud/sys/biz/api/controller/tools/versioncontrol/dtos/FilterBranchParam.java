package com.taotao.cloud.sys.biz.api.controller.tools.versioncontrol.dtos;

import lombok.Data;

@Data
public class FilterBranchParam {
    private String group;
    private String repository;
    private String filterValue = "";
}
