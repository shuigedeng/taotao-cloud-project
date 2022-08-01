package com.taotao.cloud.sys.biz.modules.mybtis.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StatementInfo {
    private String namespace;
    private List<StatementIdInfo> statementIdInfos = new ArrayList<>();

    public StatementInfo() {
    }

    public StatementInfo(String namespace, List<StatementIdInfo> statementIdInfos) {
        this.namespace = namespace;
        this.statementIdInfos = statementIdInfos;
    }
}
