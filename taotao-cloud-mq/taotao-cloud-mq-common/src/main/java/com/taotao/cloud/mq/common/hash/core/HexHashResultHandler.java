//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.taotao.cloud.mq.common.hash.core;

import com.taotao.boot.common.utils.secure.HexUtils;
import com.taotao.cloud.mq.common.hash.api.IHashResult;
import com.taotao.cloud.mq.common.hash.api.IHashResultHandler;

public class HexHashResultHandler implements IHashResultHandler<String> {
    public String handle(IHashResult hashResult) {
        return HexUtils.encodeToString(hashResult.hashed());
    }
}
