package com.taotao.cloud.standalone.system.modules.sys.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Classname RedisVo
 * @Description redisVo
 * @Author shuigedeng
 * @since 2019-07-18 16:17
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedisVo implements Serializable {

    /**
     * key
     */
    private String key;
    /**
     * value
     */
    private String value;

    /**
     * 过期时间
     */
    private long expireTime;
}
