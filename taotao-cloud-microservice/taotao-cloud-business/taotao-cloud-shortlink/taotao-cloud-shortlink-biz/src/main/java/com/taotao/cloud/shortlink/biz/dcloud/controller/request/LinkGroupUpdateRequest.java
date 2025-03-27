package com.taotao.cloud.shortlink.biz.dcloud.controller.request;

import lombok.Data;
import lombok.experimental.*;

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
