package com.taotao.cloud.sys.biz.shortlink.src.main.java.com.taotao.cloud.shortlink.biz.dcloud.controller.request;

import lombok.Data;

/**
 * @Description
 * @Author:刘森飚
 **/

@Data
public class ShortLinkPageRequest {


    /**
     * 组
     */
    private Long groupId;

    /**
     * 第几页
     */
    private int page;

    /**
     * 每页多少条
     */
    private int size;

}
