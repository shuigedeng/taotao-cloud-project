package com.taotao.cloud.java.javaee.s2.c5_redis.web.java.pojo;

import lombok.Data;

@Data
public class ApiMapping {
    private Integer id;
    private String gatewayApiName;
    private String insideApiUrl;
    private Integer state;
    private String description;
    private String serviceId;
    private String idempotents;//当前服务是否保持幂等性,不允许重复请求,0代表不需要.1 代表需要
    private String needfee;//当前接口是否需要收费, 1 代表需要 ,0 代表免费
}
