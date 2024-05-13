package com.taotao.cloud.rpc.registry.apiregistry.registry;


import java.util.List;
import java.util.Map;

/**
 * 注册中心实现
 */
public class BaseRegistry implements AutoCloseable {
    public BaseRegistry(){
        // ThreadUtils.shutdown(()->{
        //     try {
        //         this.close();
        //     }catch (Exception e){}
        // },1,false);
    }

    public void register(){

    }
    /**获取服务列表*/
    public Map<String, List<String>> getServerList(){
        return null;
    }
    /**获取报表信息*/
    public String getReport(){
        return "";
    }

    @Override
    public void close() {

    }
}
