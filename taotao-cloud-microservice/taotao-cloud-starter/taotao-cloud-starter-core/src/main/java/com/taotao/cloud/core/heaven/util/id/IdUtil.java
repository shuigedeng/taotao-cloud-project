/*
 * Copyright (c)  2019. houbinbin Inc.
 * heaven All rights reserved.
 */

package com.taotao.cloud.core.heaven.util.id;


import com.taotao.cloud.core.heaven.util.id.support.Sequence;

/**
 * <p> 分布式 id 生成 </p>
 *
 * <pre> Created: 2018/6/15 下午3:41  </pre>
 * <pre> Project: lombok-ex  </pre>
 *
 * @author houbinbin
 * @since 0.0.1
 */
@Deprecated
public final class IdUtil {

    /**
     * 主机和进程的机器码
     */
    private static final Sequence WORKER = new Sequence();

    /**
     * 雪花算法
     * @return id
     */
    public static long nextId() {
        return WORKER.nextId();
    }

}
