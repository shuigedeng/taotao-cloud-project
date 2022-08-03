package com.taotao.cloud.sys.biz.api.controller.tools.elasticsearch.remote.dtos;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class ClusterModel {
    private JSONObject clusterState;
    private JSONObject status;
    private JSONObject nodeStats;
    private JSONObject clusterNodes;
    private JSONObject clusterHealth;

    public ClusterModel() {
    }

    public ClusterModel(JSONObject clusterState, JSONObject status, JSONObject nodeStats, JSONObject clusterNodes, JSONObject clusterHealth) {
        this.clusterState = clusterState;
        this.status = status;
        this.nodeStats = nodeStats;
        this.clusterNodes = clusterNodes;
        this.clusterHealth = clusterHealth;
    }
}
