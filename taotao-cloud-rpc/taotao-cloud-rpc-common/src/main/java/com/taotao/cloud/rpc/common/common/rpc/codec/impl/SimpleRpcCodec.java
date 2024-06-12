//package com.taotao.cloud.rpc.common.common.rpc.codec.impl;
//
//
///**
// * 默认序列化相关处理
// * TODO: 对于 object 的处理会导致死循环。后期修复掉这个 bug。
// * @author shuigedeng
// * @since 2024.06
// */
//@ThreadSafe
//public class SimpleRpcCodec implements RpcCodec {
//
//    private static final SimpleRpcCodec INSTANCE = new SimpleRpcCodec();
//
//    public static RpcCodec getInstance() {
//        return INSTANCE;
//    }
//
//    @Override
//    public byte[] toBytes(Object object) {
//        return JsonBs.serializeBytes(object);
//    }
//
//    @Override
//    public <T> T toObject(byte[] bytes, Class<T> tClass) {
//        return JsonBs.deserializeBytes(bytes, tClass);
//    }
//
//}
