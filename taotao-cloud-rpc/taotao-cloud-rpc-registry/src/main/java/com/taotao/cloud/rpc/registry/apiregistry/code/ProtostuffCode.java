package com.taotao.cloud.rpc.registry.apiregistry.code;

import com.taotao.cloud.rpc.registry.apiregistry.base.ApiRegistryException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ProtostuffCode implements ICode {
    public <T> byte[] encode(T data){
        // return ProtostuffUtils.serialize(data);
		return null;
    }
    public <T> T decode(byte[] data, Type type){
        if(type instanceof ParameterizedType){
            // return ProtostuffUtils.deserialize(data,(Class<T>)((ParameterizedType)type).getRawType());
			return null;
        }
        if(type instanceof Class<?>){
            // return ProtostuffUtils.deserialize(data,(Class<T>)type);
			return null;
        }
        throw new ApiRegistryException("Protostuff不支持该类型反序列化");
    }
}
