package com.taotao.cloud.java.javaee.s2.c5_redis.web.java.util;


/**
 * @author menglili
 * 调用搜索服务用到的对象，与搜索服务中的数据格式对应
 */
public class SearchPojo {
    private String apiName;
    private Integer start;
    private String appkey;
    private Long startTime;
    private String highLightPreTag;
    private Long endTime;
    private Integer rows;
    private String highLightPostTag;
    private String requestContent;

}
