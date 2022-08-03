package com.taotao.cloud.sys.biz.api.controller.tools.core.dtos.param;

import lombok.Data;

@Data
public class RedisConnectParam extends AbstractConnectParam {
    private AuthParam authParam = new AuthParam();
}
