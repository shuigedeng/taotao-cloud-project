//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.taotao.cloud.mq.common.hash.core;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class HashCodeMurmur extends AbstractHashCode {
    public int doHash(String text) {
        ByteBuffer buf = ByteBuffer.wrap(text.getBytes());
        int seed = 305441741;
        ByteOrder byteOrder = buf.order();
        buf.order(ByteOrder.LITTLE_ENDIAN);
        long m = -4132994306676758123L;
        int r = 47;

        long h;
        for(h = (long)seed ^ (long)buf.remaining() * m; buf.remaining() >= 8; h *= m) {
            long k = buf.getLong();
            k *= m;
            k ^= k >>> r;
            k *= m;
            h ^= k;
        }

        if (buf.remaining() > 0) {
            ByteBuffer finish = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
            finish.put(buf).rewind();
            h ^= finish.getLong();
            h *= m;
        }

        h ^= h >>> r;
        h *= m;
        h ^= h >>> r;
        buf.order(byteOrder);
        return (int)(h & 4294967295L);
    }
}
