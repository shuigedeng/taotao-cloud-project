//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.taotao.cloud.mq.common.hash.core;


import com.taotao.cloud.mq.common.hash.api.IHash;
import com.taotao.cloud.mq.common.hash.api.IHashContext;
import com.taotao.cloud.mq.common.hash.api.IHashResult;

public abstract class AbstractHash implements IHash {
    protected abstract byte[] doHash(byte[] var1, IHashContext var2);

    public IHashResult hash(byte[] source, IHashContext context) {
        byte[] hashed = this.doHash(source, context);
        return HashResult.newInstance().hashed(hashed);
    }
}
