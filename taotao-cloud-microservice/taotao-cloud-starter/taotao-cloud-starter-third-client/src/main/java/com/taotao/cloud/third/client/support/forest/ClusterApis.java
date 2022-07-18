package com.taotao.cloud.third.client.support.forest;

import com.alibaba.fastjson2.JSONObject;
import com.dtflys.forest.annotation.Body;
import com.dtflys.forest.annotation.Post;
import com.dtflys.forest.annotation.Request;
import com.dtflys.forest.annotation.Var;

public interface ClusterApis {

    @Request(
            url = "${baseUrl}/_cluster/state?format=json",
            dataType = "json"
    )
	JSONObject clusterState(@Var("baseUrl") String baseUrl);

    @Request(
            url = "${baseUrl}/_stats?format=json",
            dataType = "json"
    )
    JSONObject status(@Var("baseUrl")  String baseUrl);

    @Request(
            url = "${baseUrl}/_nodes?format=json",
            dataType = "json"
    )
    JSONObject clusterNodes(@Var("baseUrl")  String baseUrl);

    @Request(
            url = "${baseUrl}/_nodes/stats?format=json",
            dataType = "json"
    )
    JSONObject nodeStats(@Var("baseUrl")  String baseUrl);


    @Request(
            url = "${baseUrl}/_cluster/health?format=json",
            dataType = "json"
    )
    JSONObject clusterHealth(@Var("baseUrl")  String baseUrl);

    @Post(
            url = "${baseUrl}/${indexName}/_search?format=json",
            dataType = "json",
            contentType = "application/json"
    )
    JSONObject indexDataSearch(@Var("baseUrl") String baseUrl, @Var("indexName") String indexName, @Body String dsl);

    @Post(
            url = "${baseUrl}/_search?format=json",
            dataType = "json",
            contentType = "application/json"
    )
    JSONObject dslSearch(@Var("baseUrl") String baseUrl,@Body String dsl);
}
