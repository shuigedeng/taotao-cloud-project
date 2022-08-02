package com.taotao.cloud.sys.biz.modules.core.dtos.param;

import lombok.Data;

@Data
public class RedisConnectParam extends AbstractConnectParam {
    private AuthParam authParam = new AuthParam();
}
