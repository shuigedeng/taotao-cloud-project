package com.taotao.cloud.sys.biz.controller.tools.elasticsearch.controller;

import com.alibaba.fastjson.JSONObject;
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

	public JSONObject getClusterState() {
		return clusterState;
	}

	public void setClusterState(JSONObject clusterState) {
		this.clusterState = clusterState;
	}

	public JSONObject getStatus() {
		return status;
	}

	public void setStatus(JSONObject status) {
		this.status = status;
	}

	public JSONObject getNodeStats() {
		return nodeStats;
	}

	public void setNodeStats(JSONObject nodeStats) {
		this.nodeStats = nodeStats;
	}

	public JSONObject getClusterNodes() {
		return clusterNodes;
	}

	public void setClusterNodes(JSONObject clusterNodes) {
		this.clusterNodes = clusterNodes;
	}

	public JSONObject getClusterHealth() {
		return clusterHealth;
	}

	public void setClusterHealth(JSONObject clusterHealth) {
		this.clusterHealth = clusterHealth;
	}
}
