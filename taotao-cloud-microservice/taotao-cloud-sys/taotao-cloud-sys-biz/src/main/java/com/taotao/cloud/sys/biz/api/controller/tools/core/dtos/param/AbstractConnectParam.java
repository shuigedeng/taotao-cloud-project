package com.taotao.cloud.sys.biz.api.controller.tools.core.dtos.param;

import lombok.Data;

@Data
public abstract class AbstractConnectParam {
    protected ConnectIdParam connectIdParam;
    protected ConnectParam connectParam;
}
