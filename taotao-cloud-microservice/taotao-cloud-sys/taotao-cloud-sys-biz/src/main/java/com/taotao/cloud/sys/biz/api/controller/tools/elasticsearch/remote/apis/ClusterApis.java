package com.taotao.cloud.sys.biz.api.controller.tools.elasticsearch.remote.apis;

import com.alibaba.fastjson.JSONObject;
import com.dtflys.forest.annotation.*;

public interface ClusterApis {

    @Request(
            url = "${baseUrl}/_cluster/state?format=json",
            dataType = "json"
    )
    JSONObject clusterState(@DataVariable("baseUrl") String baseUrl);

    @Request(
            url = "${baseUrl}/_stats?format=json",
            dataType = "json"
    )
    JSONObject status(@DataVariable("baseUrl")  String baseUrl);

    @Request(
            url = "${baseUrl}/_nodes?format=json",
            dataType = "json"
    )
    JSONObject clusterNodes(@DataVariable("baseUrl")  String baseUrl);

    @Request(
            url = "${baseUrl}/_nodes/stats?format=json",
            dataType = "json"
    )
    JSONObject nodeStats(@DataVariable("baseUrl")  String baseUrl);


    @Request(
            url = "${baseUrl}/_cluster/health?format=json",
            dataType = "json"
    )
    JSONObject clusterHealth(@DataVariable("baseUrl")  String baseUrl);

    @Post(
            url = "${baseUrl}/${indexName}/_search?format=json",
            dataType = "json",
            contentType = "application/json"
    )
    JSONObject indexDataSearch(@DataVariable("baseUrl") String baseUrl, @DataVariable("indexName") String indexName, @Body String dsl);

    @Post(
            url = "${baseUrl}/_search?format=json",
            dataType = "json",
            contentType = "application/json"
    )
    JSONObject dslSearch(@DataVariable("baseUrl") String baseUrl,@Body String dsl);
}
