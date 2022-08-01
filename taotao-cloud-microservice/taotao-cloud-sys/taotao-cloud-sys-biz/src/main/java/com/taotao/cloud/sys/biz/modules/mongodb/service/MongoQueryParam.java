package com.taotao.cloud.sys.biz.modules.mongodb.service;

import com.alibaba.fastjson.JSONObject;
import com.sanri.tools.modules.core.dtos.param.PageParam;
import lombok.Data;
import org.bson.conversions.Bson;

import javax.validation.constraints.NotNull;

@Data
public class MongoQueryParam {
    /**
     * 连接名称
     */
    @NotNull
    private String connName;
    /**
     * 数据库名
     */
    @NotNull
    private String databaseName;
    /**
     * 集合名称
     */
    @NotNull
    private String collectionName;
    /**
     * filter json
     */
    private String filter;
    /**
     * sort json
     */
    private String sort;
}
