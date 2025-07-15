//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.taotao.cloud.mq.common.hash.core;


import com.taotao.cloud.mq.common.hash.HashType;

public class Md2Hash extends AbstractMessageDigestHash {
    protected String algorithmName() {
        return HashType.MD2.getCode();
    }
}
