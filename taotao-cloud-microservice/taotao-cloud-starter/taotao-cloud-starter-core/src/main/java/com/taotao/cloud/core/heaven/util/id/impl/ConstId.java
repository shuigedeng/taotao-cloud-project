package com.taotao.cloud.core.heaven.util.id.impl;


import com.taotao.cloud.core.heaven.annotation.ThreadSafe;
import com.taotao.cloud.core.heaven.util.id.Id;

/**
 * 恒定 id 返回
 */
@ThreadSafe
@Deprecated
public class ConstId implements Id {

    /**
     * 恒定标识
     */
    private final String id;

    public ConstId(String id) {
        this.id = id;
    }

    @Override
    public String genId() {
        return id;
    }

}
