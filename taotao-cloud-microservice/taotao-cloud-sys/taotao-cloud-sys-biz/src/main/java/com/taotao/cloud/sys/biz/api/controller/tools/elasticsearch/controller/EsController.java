package com.taotao.cloud.sys.biz.api.controller.tools.elasticsearch.controller;

import java.io.IOException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.taotao.cloud.sys.biz.api.controller.tools.core.dtos.param.SimpleConnectParam;
import com.taotao.cloud.sys.biz.api.controller.tools.core.service.connect.ConnectService;
import com.taotao.cloud.sys.biz.api.controller.tools.elasticsearch.remote.apis.ClusterApis;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONObject;

@AllArgsConstructor
@Validated
@RestController
@Tag(name = "工具管理端-dubbo管理API", description = "工具管理端-dubbo管理API")
@RequestMapping("/sys/tools/es")
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
    public JSONObject search(@PathVariable("connName") @NotBlank String connName, @PathVariable("indexName") String indexName, @RequestBody JSONObject dsl) throws IOException {
        String address = loadAddress(connName);
        return clusterApis.indexDataSearch(address,indexName,dsl.toJSONString());
    }

    @PostMapping("/search/{connName}")
    public JSONObject search(@PathVariable("connName") @NotBlank String connName, @RequestBody JSONObject dsl) throws IOException {
        String address = loadAddress(connName);
        return clusterApis.dslSearch(address,dsl.toJSONString());
    }

    /**
     * 获取当前连接的 es 地址
     * @param connName
     * @return
     * @throws IOException
     */
    private String loadAddress(@NotBlank String connName) throws IOException {
        SimpleConnectParam simpleConnectParam = (SimpleConnectParam) connectService.readConnParams("elasticsearch",connName);
        return simpleConnectParam.getConnectParam().httpConnectString();
    }

}
