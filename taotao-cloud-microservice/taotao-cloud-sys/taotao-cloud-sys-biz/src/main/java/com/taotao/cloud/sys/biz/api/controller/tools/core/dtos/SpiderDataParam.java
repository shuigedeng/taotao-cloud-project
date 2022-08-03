package com.taotao.cloud.sys.biz.api.controller.tools.core.dtos;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Data
public class SpiderDataParam {
    /**
     * 类名
     */
    @NotNull
    private String className;
    /**
     * 类加载器名称
     */
    @NotNull
    private String classloaderName;
    /**
     * 其它参数
     */
    private Map<String,String> params = new HashMap<>();
}
