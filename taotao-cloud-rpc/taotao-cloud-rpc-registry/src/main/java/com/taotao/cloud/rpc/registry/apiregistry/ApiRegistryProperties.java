/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.rpc.registry.apiregistry;

public class ApiRegistryProperties {
    public static final String Project = "ApiRegistry";
    public static final String Enabled = "ttc.apiRegistry.enabled";
    public static final String ApiClientAspectEnabled = "ttc.apiRegistry.apiClientAspect.enabled";
    public static final String HealthEnabled = "ttc.apiRegistry.health.enabled";
    public static final String FeignClientClassPath =
            "org.springframework.cloud.openfeign.FeignClient";
    //    public static boolean getEnabled(){
    //        return PropertyUtils.getPropertyCache(Enabled,false);
    //    }
    //    public static boolean getTestEnabled(){
    //        return PropertyUtils.getPropertyCache("ttc.apiRegistry.test.enabled",false);
    //    }
    //    public static String getTestSkipMethods(){
    //        return PropertyUtils.getPropertyCache("ttc.apiRegistry.test.skipMethods","");
    //    }
    //    public static boolean getWarnEnabled(){
    //        return PropertyUtils.getPropertyCache("ttc.apiRegistry.warn.enabled",false);
    //    }
    //    public static boolean getApiClientAspectEnabled(){
    //        return PropertyUtils.getPropertyCache(ApiClientAspectEnabled,false);
    //    }
    //    public static String[] getAppNameHosts(String appName){
    //        return
    // StringUtils.spilt(PropertyUtils.getPropertyCache("ttc.apiRegistry.{appName}.hosts".replace("{appName}",appName),""),",");
    //    }
    //
    //    public static Integer getHttpUrlConnectionConnectTimeOut(){
    //        return
    // PropertyUtils.getPropertyCache("ttc.apiRegistry.rpcClient.httpUrlConnection.connectTimeOut",3000);
    //    }
    //    public static Integer getHttpUrlConnectionReadTimeOut(){
    //        return
    // PropertyUtils.getPropertyCache("ttc.apiRegistry.rpcClient.httpUrlConnection.readTimeOut",60000);
    //    }
    //    public static Boolean getHttpUrlConnectionPoolEnabled(){
    //        return
    // PropertyUtils.getPropertyCache("ttc.apiRegistry.rpcClient.httpUrlConnection.poolEnabled",true);
    //    }
    //    public static String getRpcClientType(){
    //        return
    // PropertyUtils.getPropertyCache("ttc.apiRegistry.rpcClient.type","HttpUrlConnectionRpcClient");
    //    }
    //    public static String[] getRpcClientBasePackages(){
    //        return
    // StringUtils.spilt(PropertyUtils.getPropertyCache("ttc.apiRegistry.rpcClient.basePackages",""),",");
    //    }
    //    //重写beanName命名规则
    //    public static Boolean getRpcClientTypeBeanNameEnabled(){
    //        return
    // PropertyUtils.getPropertyCache("ttc.apiRegistry.rpcClient.typeBeanName.enabled",true);
    //    }
    //    public static Integer getRegistryFailRetryTimes(){
    //        return PropertyUtils.getPropertyCache("ttc.apiRegistry.rcpClient.failRetryTimes",3);
    //    }
    //    public static Integer getRegistryFailRetryTimeSpan(){
    //        return
    // PropertyUtils.getPropertyCache("ttc.apiRegistry.rcpClient.failRetryTimeSpan",30000);
    //    }
    //    public static String getRegistryType(){
    //        return
    // PropertyUtils.getPropertyCache("ttc.apiRegistry.registry.type","RedisRegistry");
    //    }
    //    public static boolean getRegistryClientRegistered(){
    //        return
    // PropertyUtils.getPropertyCache("ttc.apiRegistry.registry.clientRegistered",true);
    //    }
    //    public static String getRegistryRedisHost(){
    //        return PropertyUtils.getPropertyCache("ttc.apiRegistry.registry.redis.host","");
    //    }
    //    public static Integer getRegistryRedisHeartBeatTime(){
    //        return
    // PropertyUtils.getPropertyCache("ttc.apiRegistry.registry.redis.heartBeatTime",3000);
    //    }
    //    public static Integer getRegistryRedisExpireTime(){
    //        return PropertyUtils.getPropertyCache("ttc.apiRegistry.registry.redis.expireTime",5);
    //    }
    //    public static Integer getRegistryNacosServerListCacheHeartBeatTime(){
    //        return
    // PropertyUtils.getPropertyCache("ttc.apiRegistry.registry.nacos.serverListCache.heartBeatTime",10000);
    //    }
    //    public static String getRegistryLoadBalanceType(){
    //        return
    // PropertyUtils.getPropertyCache("ttc.apiRegistry.loadBalance.type","RoundRobinLoadBalance");
    //    }
    //    public static String getRegistryCodeType(){
    //        return PropertyUtils.getPropertyCache("ttc.apiRegistry.code.type","json");
    //    }
    //    public static Boolean getRegistryHealthEnabled(){
    //        return PropertyUtils.getPropertyCache(HealthEnabled,true);
    //    }
}
