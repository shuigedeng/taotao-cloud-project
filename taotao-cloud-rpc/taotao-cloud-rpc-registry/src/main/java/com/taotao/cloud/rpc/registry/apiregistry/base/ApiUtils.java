package com.taotao.cloud.rpc.registry.apiregistry.base;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.taotao.cloud.rpc.registry.apiregistry.ApiRegistryProperties;
import com.taotao.cloud.rpc.registry.apiregistry.anno.ApiClient;
import com.taotao.cloud.rpc.registry.apiregistry.anno.ApiIgnore;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.springframework.core.annotation.AnnotationUtils;

public class ApiUtils {
//    public static Class FeignClientAnnotationClass = ReflectionUtils.tryClassForName(ApiRegistryProperties.FeignClientClassPath);
//    public static ApiClientInfo getApiClient(Method method){
//		ApiClient apiClient= AnnotationUtils.getAnnotation(method.getDeclaringClass(), ApiClient.class);
//        if(apiClient != null){
//            return new ApiClientInfo(StringUtils.nullToEmpty(apiClient.name()),StringUtils.nullToEmpty(apiClient.path()));
//        }
//
//		Annotation feignClient = AnnotationUtils.getAnnotation(method.getDeclaringClass(), FeignClientAnnotationClass);
//		if(feignClient != null){
//			Object name = ReflectionUtils.tryCallMethod(feignClient, "name", null, null);
//			Object path = ReflectionUtils.tryCallMethod(feignClient,"path",null,null);
//            return new ApiClientInfo(StringUtils.nullToEmpty(name),StringUtils.nullToEmpty(path));
//        }
//        return null;
//    }
//
//    public static ApiIgnore getApiIgnore(Method method){
//		ApiIgnore apiIgnore= method.getAnnotation(ApiIgnore.class);
//        if(apiIgnore != null){
//            return apiIgnore;
//        }
//        return null;
//    }
//
//    public static String getUrl(String host,String contextPath){
//        if(StringUtils.isEmpty(host)){
//            return "";
//        }
//        String url=host;
//        if(!host.startsWith("http://")&&!host.startsWith("https://")){
//            url="http://"+host;
//        }
//        if(!StringUtils.isEmpty(contextPath)){
//            url= StringUtils.trimRight(url,'/')+contextPath;
//        }
//        return url;
//    }
//
//    public static void checkObjectEqual(Object from,Object to){
//        if(from==null&&to==null) {
//            return;
//        }
//        String message;
//        try {
//            // val fromStr = JsonUtils.serialize(from);
//            // val toStr = JsonUtils.serialize(to);
//            // val fromJson = clearJsonNode(JsonUtils.Default.getMapper().readTree(fromStr));
//            // val toJson = clearJsonNode(JsonUtils.Default.getMapper().readTree(toStr));
//            // if(fromJson.equals(toJson)) {
//            //     return;
//            // }
//            // message ="checkObjectEqual对象不一致,fromJson:"+StringUtils.nullToEmpty(fromJson)+" toJson:"+StringUtils.nullToEmpty(toJson);
//			message = "";
//        }catch (Exception e){
//            message ="checkObjectEqual时发生错误,error:"+e.getMessage();
//        }
//        throw new ApiRegistryException(message);
//    }
//
//    //清理多余节点
//    private static JsonNode clearJsonNode(JsonNode jsonNode){
//        if(jsonNode!=null){
//            if(jsonNode.isObject()){
//                ((ObjectNode)jsonNode).remove("debug");
//            }
//        }
//        return jsonNode;
//    }

}
