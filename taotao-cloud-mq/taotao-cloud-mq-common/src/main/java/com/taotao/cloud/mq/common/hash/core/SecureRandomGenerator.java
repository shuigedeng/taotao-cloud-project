//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.taotao.cloud.mq.common.hash.core;

import com.taotao.cloud.mq.common.hash.api.IRandomGenerator;
import java.security.SecureRandom;

public class SecureRandomGenerator implements IRandomGenerator {
    protected static final int DEFAULT_NEXT_BYTES_SIZE = 16;
    private int defaultNextBytesSize = 16;
    private SecureRandom secureRandom = new SecureRandom();

    public byte[] nextBytes() {
        return this.nextBytes(this.defaultNextBytesSize);
    }

    public byte[] nextBytes(int numBytes) {
        if (numBytes <= 0) {
            throw new IllegalArgumentException("numBytes argument must be a positive integer (1 or larger)");
        } else {
            byte[] bytes = new byte[numBytes];
            this.secureRandom.nextBytes(bytes);
            return bytes;
        }
    }
}
