package com.taotao.cloud.rpc.registry.apiregistry.code;

import com.taotao.cloud.rpc.registry.apiregistry.ApiRegistryProperties;
import com.taotao.cloud.rpc.registry.apiregistry.base.ApiRegistryException;
import java.util.HashMap;
import java.util.Map;

public class CodeFactory {

    public static ICode create(String contentType){
//        contentType = StringUtils.nullToEmpty(contentType).toLowerCase();
        if(contentType.contains("json")){
            return new JsonCode();
        }else if(contentType.contains("protostuff")||contentType.contains("protobuf")){
            return new ProtostuffCode();
        }
        return create();
    }

    public static ICode create(){
//		String codeType = ApiRegistryProperties.getRegistryCodeType();
//        if("json".equalsIgnoreCase(codeType)){
//            return new JsonCode();
//        }else if("protostuff".equalsIgnoreCase(codeType)||"protobuf".equalsIgnoreCase(codeType)){
//            return new ProtostuffCode();
//        }
        throw new ApiRegistryException("请配置ttc.apiRegistry.code.type");
    }

    public static String getHeader(){
//		String codeType = ApiRegistryProperties.getRegistryCodeType();
//		String header = codeHeadersEnum.get(codeType);
//        if(header==null){
//            throw new ApiRegistryException("请配置ttc.apiRegistry.code.type");
//        }
        return null;
    }

    private static Map<String,String> codeHeadersEnum = new HashMap<String,String>(){{
        put("json","application/json");
        put("protobuf","application/x-protobuf");
        put("protostuff","application/x-protobuf");
    }};
}
