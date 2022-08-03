package com.taotao.cloud.sys.biz.api.controller.tools.core.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 *  {module:[{},{}],module2:[{},{}]}
 */
@Data
public class ConnectDto {
    /**
     * 模块名称
     */
    private String module;
    /**
     * 连接名称
     */
    private String name;
    /**
     * 上次修改时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastModified;

    public ConnectDto() {
    }

    public ConnectDto(String module, String name, Date lastModified) {
        this.module = module;
        this.name = name;
        this.lastModified = lastModified;
    }
}
