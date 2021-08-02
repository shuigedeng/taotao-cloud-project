package com.taotao.cloud.java.javaee.s2.c5_redis.web.java.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @author menglili
 */
public class UserToken {
    private Integer id;
    private Integer cusId;
    private String accessToken;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expireTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
}
