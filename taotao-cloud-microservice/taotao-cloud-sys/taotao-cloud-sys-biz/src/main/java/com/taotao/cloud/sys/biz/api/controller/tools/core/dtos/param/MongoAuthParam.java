package com.taotao.cloud.sys.biz.api.controller.tools.core.dtos.param;

import lombok.Data;

@Data
public class MongoAuthParam extends AuthParam {
    private String database;
}
