package com.taotao.cloud.sys.biz.modules.core.dtos.param;

import lombok.Data;

@Data
public class MongoAuthParam extends AuthParam {
    private String database;
}
