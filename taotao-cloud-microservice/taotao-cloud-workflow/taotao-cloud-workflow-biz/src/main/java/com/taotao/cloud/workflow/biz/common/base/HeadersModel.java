package com.taotao.cloud.workflow.biz.common.base;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * token
 *
 */
@Data
public class HeadersModel {
    @JSONField(name = "Token")
    private String token;
}
