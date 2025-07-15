//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.taotao.cloud.mq.common.hash.core;

import com.taotao.cloud.mq.common.hash.api.IHashContext;
import java.nio.charset.Charset;
import java.util.Arrays;

public class HashContext implements IHashContext {
    private byte[] salt;
    private int times;
    private Charset charset;

    public static HashContext newInstance() {
        return new HashContext();
    }

    public byte[] salt() {
        return this.salt;
    }

    public HashContext salt(byte[] salt) {
        this.salt = salt;
        return this;
    }

    public int times() {
        return this.times;
    }

    public HashContext times(int times) {
        this.times = times;
        return this;
    }

    public Charset charset() {
        return this.charset;
    }

    public HashContext charset(Charset charset) {
        this.charset = charset;
        return this;
    }

    public String toString() {
        return "HashContext{salt=" + Arrays.toString(this.salt) + ", times=" + this.times + ", charset=" + this.charset + '}';
    }
}
