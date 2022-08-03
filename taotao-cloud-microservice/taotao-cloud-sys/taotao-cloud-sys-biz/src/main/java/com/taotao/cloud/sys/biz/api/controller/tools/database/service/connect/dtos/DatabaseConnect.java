package com.taotao.cloud.sys.biz.api.controller.tools.database.service.connect.dtos;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据源连接
 */
@Data
public class DatabaseConnect {
    private String url;
    private String username;
    private String password;
    private String driverClassName;
    /**
     * 连接属性配置
     */
    private Map<String,String> properties = new HashMap<>();
}
