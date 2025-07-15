//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.taotao.cloud.mq.common.hash.core;


import com.taotao.cloud.mq.common.hash.HashType;

public class Sha512Hash extends AbstractMessageDigestHash {
    protected String algorithmName() {
        return HashType.SHA512.getCode();
    }
}
