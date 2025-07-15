//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.taotao.cloud.mq.common.hash.core;


import com.taotao.boot.common.support.instance.impl.Instances;
import com.taotao.cloud.mq.common.hash.api.IHashCode;

public final class HasheCodes {
    private HasheCodes() {
    }

    public static IHashCode crc() {
        return (IHashCode) Instances.singleton(HashCodeCRC.class);
    }

    public static IHashCode fnv() {
        return (IHashCode)Instances.singleton(HashCodeFnv.class);
    }

    public static IHashCode jdk() {
        return (IHashCode)Instances.singleton(HashCodeJdk.class);
    }

    public static IHashCode ketama() {
        return (IHashCode)Instances.singleton(HashCodeKetama.class);
    }

    public static IHashCode murmur() {
        return (IHashCode)Instances.singleton(HashCodeMurmur.class);
    }
}
