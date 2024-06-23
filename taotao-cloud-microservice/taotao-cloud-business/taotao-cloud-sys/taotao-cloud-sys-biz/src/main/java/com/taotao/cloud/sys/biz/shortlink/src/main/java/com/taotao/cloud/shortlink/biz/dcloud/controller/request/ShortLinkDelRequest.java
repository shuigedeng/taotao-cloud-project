package com.taotao.cloud.sys.biz.shortlink.src.main.java.com.taotao.cloud.shortlink.biz.dcloud.controller.request;

import lombok.Data;

/**
 * @Description
 * @Author:刘森飚
 **/

@Data
public class ShortLinkDelRequest {


    /**
     * 组
     */
    private Long groupId;

    /**
     * 映射id
     */
    private Long mappingId;


    /**
     * 短链码
     */
    private String code;

}
