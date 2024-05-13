package com.taotao.cloud.rpc.registry.apiregistry;


import com.taotao.cloud.common.extension.StringUtils;
import com.taotao.cloud.common.utils.common.PropertyUtils;

public class ApiRegistryProperties{
    public static final String Project="ApiRegistry";
    public static final String Enabled="bsf.apiRegistry.enabled";
    public static final String ApiClientAspectEnabled="bsf.apiRegistry.apiClientAspect.enabled";
    public static final String HealthEnabled="bsf.apiRegistry.health.enabled";
    public static final String FeignClientClassPath="org.springframework.cloud.openfeign.FeignClient";
    public static boolean getEnabled(){
        return PropertyUtils.getPropertyCache(Enabled,false);
    }
    public static boolean getTestEnabled(){
        return PropertyUtils.getPropertyCache("bsf.apiRegistry.test.enabled",false);
    }
    public static String getTestSkipMethods(){
        return PropertyUtils.getPropertyCache("bsf.apiRegistry.test.skipMethods","");
    }
    public static boolean getWarnEnabled(){
        return PropertyUtils.getPropertyCache("bsf.apiRegistry.warn.enabled",false);
    }
    public static boolean getApiClientAspectEnabled(){
        return PropertyUtils.getPropertyCache(ApiClientAspectEnabled,false);
    }
    public static String[] getAppNameHosts(String appName){
        return StringUtils.spilt(PropertyUtils.getPropertyCache("bsf.apiRegistry.{appName}.hosts".replace("{appName}",appName),""),",");
    }

    public static Integer getHttpUrlConnectionConnectTimeOut(){
        return PropertyUtils.getPropertyCache("bsf.apiRegistry.rpcClient.httpUrlConnection.connectTimeOut",3000);
    }
    public static Integer getHttpUrlConnectionReadTimeOut(){
        return PropertyUtils.getPropertyCache("bsf.apiRegistry.rpcClient.httpUrlConnection.readTimeOut",60000);
    }
    public static Boolean getHttpUrlConnectionPoolEnabled(){
        return PropertyUtils.getPropertyCache("bsf.apiRegistry.rpcClient.httpUrlConnection.poolEnabled",true);
    }
    public static String getRpcClientType(){
        return PropertyUtils.getPropertyCache("bsf.apiRegistry.rpcClient.type","HttpUrlConnectionRpcClient");
    }
    public static String[] getRpcClientBasePackages(){
        return StringUtils.spilt(PropertyUtils.getPropertyCache("bsf.apiRegistry.rpcClient.basePackages",""),",");
    }
    //重写beanName命名规则
    public static Boolean getRpcClientTypeBeanNameEnabled(){
        return PropertyUtils.getPropertyCache("bsf.apiRegistry.rpcClient.typeBeanName.enabled",true);
    }
    public static Integer getRegistryFailRetryTimes(){
        return PropertyUtils.getPropertyCache("bsf.apiRegistry.rcpClient.failRetryTimes",3);
    }
    public static Integer getRegistryFailRetryTimeSpan(){
        return PropertyUtils.getPropertyCache("bsf.apiRegistry.rcpClient.failRetryTimeSpan",30000);
    }
    public static String getRegistryType(){
        return PropertyUtils.getPropertyCache("bsf.apiRegistry.registry.type","RedisRegistry");
    }
    public static boolean getRegistryClientRegistered(){
        return PropertyUtils.getPropertyCache("bsf.apiRegistry.registry.clientRegistered",true);
    }
    public static String getRegistryRedisHost(){
        return PropertyUtils.getPropertyCache("bsf.apiRegistry.registry.redis.host","");
    }
    public static Integer getRegistryRedisHeartBeatTime(){
        return PropertyUtils.getPropertyCache("bsf.apiRegistry.registry.redis.heartBeatTime",3000);
    }
    public static Integer getRegistryRedisExpireTime(){
        return PropertyUtils.getPropertyCache("bsf.apiRegistry.registry.redis.expireTime",5);
    }
    public static Integer getRegistryNacosServerListCacheHeartBeatTime(){
        return PropertyUtils.getPropertyCache("bsf.apiRegistry.registry.nacos.serverListCache.heartBeatTime",10000);
    }
    public static String getRegistryLoadBalanceType(){
        return PropertyUtils.getPropertyCache("bsf.apiRegistry.loadBalance.type","RoundRobinLoadBalance");
    }
    public static String getRegistryCodeType(){
        return PropertyUtils.getPropertyCache("bsf.apiRegistry.code.type","json");
    }
    public static Boolean getRegistryHealthEnabled(){
        return PropertyUtils.getPropertyCache(HealthEnabled,true);
    }
}
