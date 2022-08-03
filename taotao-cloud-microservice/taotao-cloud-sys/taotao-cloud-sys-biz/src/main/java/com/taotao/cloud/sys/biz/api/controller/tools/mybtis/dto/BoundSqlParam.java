package com.taotao.cloud.sys.biz.api.controller.tools.mybtis.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取绑定 sql 的参数信息
 */
@Data
public class BoundSqlParam {
    @NotNull
    private String project;
    @NotNull
    private String fileName;
    @NotNull
    private String classloaderName;
    @NotNull
    private String statementId;

    /**
     * 参数值对象信息
     */
    private List<ValueObject> params = new ArrayList<>();

    @Data
    public static class ValueObject{
        private StatementIdInfo.ParameterInfo parameterInfo;
        private String value;
    }
}
