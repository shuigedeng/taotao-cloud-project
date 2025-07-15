//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.taotao.cloud.mq.common.hash.api;

import java.nio.charset.Charset;

public interface IHashContext {
    byte[] salt();

    int times();

    Charset charset();
}
