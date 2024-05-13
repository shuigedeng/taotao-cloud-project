package com.taotao.cloud.ttcrpc.registry.apiregistry.code;

import java.lang.reflect.Type;

public interface ICode {
    <T> byte[]  encode(T data) ;
    <T> T decode(byte[] data, Type type);
}
