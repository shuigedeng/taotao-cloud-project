package com.taotao.cloud.rpc.registry.apiregistry.code;

import java.lang.reflect.Type;

public interface ICode {
    <T> byte[]  encode(T data) ;
    <T> T decode(byte[] data, Type type);
}
