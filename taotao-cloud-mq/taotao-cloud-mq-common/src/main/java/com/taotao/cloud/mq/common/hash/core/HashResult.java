//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.taotao.cloud.mq.common.hash.core;

import com.taotao.cloud.mq.common.hash.api.IHashResult;
import java.util.Arrays;

public class HashResult implements IHashResult {
    private byte[] hashed;

    public static HashResult newInstance() {
        return new HashResult();
    }

    public byte[] hashed() {
        return this.hashed;
    }

    public HashResult hashed(byte[] hashed) {
        this.hashed = hashed;
        return this;
    }

    public String toString() {
        return "HashResult{hashed=" + Arrays.toString(this.hashed) + '}';
    }
}
