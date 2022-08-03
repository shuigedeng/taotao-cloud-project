package com.taotao.cloud.sys.biz.api.controller.tools.core.dtos.param;

import lombok.Data;

@Data
public class MongoConnectParam extends AbstractConnectParam {
    private MongoAuthParam authParam;
}
