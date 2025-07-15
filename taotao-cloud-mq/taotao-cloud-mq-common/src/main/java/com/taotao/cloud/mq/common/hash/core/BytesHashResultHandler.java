//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.taotao.cloud.mq.common.hash.core;

import com.taotao.cloud.mq.common.hash.api.IHashResult;
import com.taotao.cloud.mq.common.hash.api.IHashResultHandler;

public class BytesHashResultHandler implements IHashResultHandler<byte[]> {
    public byte[] handle(IHashResult hashResult) {
        return hashResult.hashed();
    }
}
