//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.taotao.cloud.mq.common.hash.core;


import com.taotao.boot.common.support.instance.impl.Instances;
import com.taotao.cloud.mq.common.hash.api.IHashResult;
import com.taotao.cloud.mq.common.hash.api.IHashResultHandler;

public final class HashResultHandlers {
    private HashResultHandlers() {
    }

    public static IHashResultHandler<String> hex() {
        return (IHashResultHandler)Instances.singleton(HexHashResultHandler.class);
    }

    public static IHashResultHandler<String> base64() {
        return (IHashResultHandler)Instances.singleton(Base64HashResultHandler.class);
    }

    public static IHashResultHandler<IHashResult> defaults() {
        return (IHashResultHandler)Instances.singleton(DefaultHashResultHandler.class);
    }

    public static IHashResultHandler<byte[]> bytes() {
        return (IHashResultHandler) Instances.singleton(BytesHashResultHandler.class);
    }
}
