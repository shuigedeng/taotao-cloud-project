package com.taotao.cloud.standalone.generator.domain;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Classname SysDatasource
 * @Description TODO
 * @Author Created by Lihaodong (alias:小东啊) lihaodongmail@163.com
 * @since 2019-08-04 16:52
 * @Version 1.0
 */
@Data
public class SysDatasource {
    /**
     * 主键
     */
    private Integer id;
    /**
     * 名称
     */
    private String name;
    /**
     * jdbcurl
     */
    private String url;

    /**
     * 驱动
     */
    private String driverName;

    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 创建时间
     */
    private LocalDateTime createDate;
    /**
     * 更新
     */
    private LocalDateTime updateDate;
}
