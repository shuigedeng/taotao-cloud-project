package com.taotao.cloud.sys.biz.tools.elasticsearch.controller;

import com.taotao.cloud.sys.biz.tools.core.dtos.param.SimpleConnectParam;
import com.taotao.cloud.sys.biz.tools.core.service.connect.ConnectService;
import com.taotao.cloud.sys.biz.tools.elasticsearch.remote.apis.ClusterApis;
import java.io.IOException;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONObject;

@RestController
@RequestMapping("/elasticsearch")
public class EsController {

    @Autowired
    private ConnectService connectService;

    @Autowired
    private ClusterApis clusterApis;

//    /**
//     * 数据量太大了, 使用分开的 api
//     * @param connName
//     * @return
//     * @throws IOException
//     */
//    @Deprecated
//    @GetMapping("/cluster/state")
//    public ClusterModel clusterModel(@NotNull String connName) throws IOException {
//        String address = loadAddress(connName);
//
//        JSONObject clusterHealth = clusterApis.clusterHealth(address);
//        JSONObject clusterNodes = clusterApis.clusterNodes(address);
//        JSONObject clusterState = clusterApis.clusterState(address);
//        JSONObject status = clusterApis.status(address);
//        JSONObject nodeStats = clusterApis.nodeStats(address);
//
//        ClusterModel clusterModel = new ClusterModel(clusterState, status, nodeStats, clusterNodes, clusterHealth);
//        return clusterModel;
//    }

    @GetMapping("/cluster/health")
    public JSONObject clusterHealth(@NotNull String connName) throws IOException {
        String address = loadAddress(connName);
        return clusterApis.clusterHealth(address);
    }

    @GetMapping("/cluster/state")
    public JSONObject clusterState(@NotNull String connName) throws IOException {
        String address = loadAddress(connName);
        return clusterApis.clusterState(address);
    }

    @GetMapping("/cluster/nodes")
    public JSONObject clusterNodes(@NotNull String connName) throws IOException {
        String address = loadAddress(connName);
        return clusterApis.clusterNodes(address);
    }

    @GetMapping("/node/stats")
    public JSONObject nodeStats(@NotNull String connName) throws IOException {
        String address = loadAddress(connName);
        return clusterApis.nodeStats(address);
    }

    @GetMapping("/status")
    public JSONObject status(@NotNull String connName) throws IOException {
        String address = loadAddress(connName);
        return clusterApis.status(address);
    }

    @PostMapping("/search/{connName}/{indexName}")
    public JSONObject search(@PathVariable("connName") String connName,@PathVariable("indexName") String indexName, @RequestBody JSONObject dsl) throws IOException {
        String address = loadAddress(connName);
        return clusterApis.indexDataSearch(address,indexName,dsl.toJSONString());
    }

    @PostMapping("/search/{connName}")
    public JSONObject search(@PathVariable("connName") String connName, @RequestBody JSONObject dsl) throws IOException {
        String address = loadAddress(connName);
        return clusterApis.dslSearch(address,dsl.toJSONString());
    }

    /**
     * 获取当前连接的 es 地址
     * @param connName
     * @return
     * @throws IOException
     */
    private String loadAddress(String connName) throws IOException {
        SimpleConnectParam simpleConnectParam = (SimpleConnectParam) connectService.readConnParams("elasticsearch",connName);
        return simpleConnectParam.getConnectParam().httpConnectString();
    }

//    @PostConstruct
//    private void register(){
//        pluginManager.register(PluginDto.builder().module("monitor").name("elasticsearch").author("9420").build());
//    }
}
