//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.taotao.cloud.mq.common.hash.core;


import com.taotao.cloud.mq.common.hash.api.IHash;

public final class Hashes {
    private Hashes() {
    }

    public static IHash md2() {
        return new Md2Hash();
    }

    public static IHash md5() {
        return new Md5Hash();
    }

    public static IHash sha1() {
        return new Sha1Hash();
    }

    public static IHash sha256() {
        return new Sha256Hash();
    }

    public static IHash sha384() {
        return new Sha384Hash();
    }

    public static IHash sha512() {
        return new Sha512Hash();
    }

    public static IHash empty() {
        return new EmptyHash();
    }
}
