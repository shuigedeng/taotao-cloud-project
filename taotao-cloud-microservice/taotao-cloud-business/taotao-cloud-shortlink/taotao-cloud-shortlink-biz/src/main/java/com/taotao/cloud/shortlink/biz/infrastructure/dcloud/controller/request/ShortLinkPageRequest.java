package com.taotao.cloud.shortlink.biz.infrastructure.dcloud.controller.request;

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
