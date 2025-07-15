//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.taotao.cloud.mq.common.hash.core;

import static com.alibaba.fastjson2.JSONB.toBytes;

import com.taotao.cloud.mq.common.hash.HashBs;
import com.taotao.cloud.mq.common.hash.api.IHash;
import com.taotao.cloud.mq.common.hash.api.IHashResultHandler;

public final class HashHelper {
    private HashHelper() {
    }

    public static String hash(String text) {
        return hash(Hashes.md5(), text);
    }

    public static String hash(IHash hash, String text) {
        return (String)hash(hash, text, HashResultHandlers.hex());
    }

    public static <T> T hash(IHash hash, String text, IHashResultHandler<T> hashResultHandler) {
        return (T)hash(hash, text, (String)null, hashResultHandler);
    }

    public static <T> T hash(IHash hash, String text, String salt, IHashResultHandler<T> hashResultHandler) {
        return (T)hash(hash, text, salt, 1, hashResultHandler);
    }

    public static <T> T hash(IHash hash, String text, String salt, int times, IHashResultHandler<T> hashResultHandler) {
        return (T) HashBs.newInstance().hash(hash).salt(toBytes(salt)).times(times).execute(toBytes(text), hashResultHandler);
    }
}
