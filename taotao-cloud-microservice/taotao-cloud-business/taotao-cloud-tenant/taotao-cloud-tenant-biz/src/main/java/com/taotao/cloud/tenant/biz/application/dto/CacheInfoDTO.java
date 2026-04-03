package com.taotao.cloud.tenant.biz.application.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 缓存信息DTO
 */
@Data
public class CacheInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 缓存键
     */
    private String key;

    /**
     * 缓存类型(STRING/HASH/SET/LIST等)
     */
    private String type;

    /**
     * 缓存值
     */
    private Object value;

    /**
     * 值预览(前100字符)
     */
    private String valuePreview;

    /**
     * 过期时间(秒, -1表示永不过期, -2表示key不存在)
     */
    private Long ttl;

    /**
     * 过期时间描述
     */
    private String ttlDesc;
}
