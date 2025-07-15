//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.taotao.cloud.mq.common.hash.core;


import com.taotao.cloud.mq.common.hash.api.IHashContext;

public class EmptyHash extends AbstractHash {
    private static final byte[] EMPTY = new byte[0];

    protected byte[] doHash(byte[] source, IHashContext context) {
        return EMPTY;
    }
}
