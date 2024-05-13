package com.taotao.cloud.rpc.registry.apiregistry.code;

import java.lang.reflect.Type;
import java.nio.charset.Charset;

public class JsonCode implements ICode {
    private Charset charset = Charset.forName("utf-8");
    public <T> byte[] encode(T data){
        // return JsonUtils.serialize(data).getBytes(charset);
		return null;
    }
    public <T> T decode(byte[] data,Type type){
        // return JsonUtils.deserialize(new String(data,charset),type);
		return null;
    }
}
