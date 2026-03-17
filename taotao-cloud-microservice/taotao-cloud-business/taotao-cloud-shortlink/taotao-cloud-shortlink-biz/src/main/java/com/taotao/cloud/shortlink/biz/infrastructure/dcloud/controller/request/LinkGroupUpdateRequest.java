package com.taotao.cloud.shortlink.biz.infrastructure.dcloud.controller.request;

import lombok.Data;

/**
 * @Description
 * @Author:刘森飚
 **/

@Data
public class LinkGroupUpdateRequest {

    /**
     * 组id
     */
    private Long id;
    /**
     * 组名
     */
    private String title;
}
