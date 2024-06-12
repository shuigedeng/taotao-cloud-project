//package com.taotao.cloud.rpc.common.common.rpc.codec.impl;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.parser.ParserConfig;
//import com.alibaba.fastjson.serializer.SerializeConfig;
//import com.taotao.cloud.rpc.common.common.rpc.codec.RpcCodec;
//
//import java.nio.charset.StandardCharsets;
//
///**
// * 默认序列化相关处理
// * @author shuigedeng
// * @since 2024.06
// */
//public class DefaultRpcCodec implements RpcCodec {
//
//    private static final DefaultRpcCodec INSTANCE = new DefaultRpcCodec();
//
//    public static RpcCodec getInstance() {
//        return INSTANCE;
//    }
//
//    @Override
//    public byte[] toBytes(Object object) {
//        String string = JSON.toJSONString(object, new SerializeConfig(true));
//        return string.getBytes(StandardCharsets.UTF_8);
//    }
//
//    @Override
//    public <T> T toObject(byte[] bytes, Class<T> tClass) {
//        String string = new String(bytes, StandardCharsets.UTF_8);
//        return JSON.parseObject(string, tClass, new ParserConfig(true));
//    }
//
//}
