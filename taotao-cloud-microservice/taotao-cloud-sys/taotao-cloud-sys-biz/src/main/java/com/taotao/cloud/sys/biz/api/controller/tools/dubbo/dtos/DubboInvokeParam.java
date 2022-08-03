package com.taotao.cloud.sys.biz.api.controller.tools.dubbo.dtos;

import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.JSONArray;

import lombok.Data;

@Data
public class DubboInvokeParam  {
    @NotNull
    private String connName;
    @NotNull
    private String serviceName;
    private String classloaderName;
    private String methodName;
    private JSONArray args;
    @NotNull
    private String providerURL;
}
