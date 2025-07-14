/*
 * Copyright (c)  2019. houbinbin Inc.
 * sisyphus All rights reserved.
 */

package com.taotao.cloud.mq.common.retry.core.support.recover;

import com.taotao.cloud.mq.common.retry.api.support.recover.Recover;

/**
 * <p> 恢复现场工具类 </p>
 *
 * <pre> Created: 2019/5/28 10:47 PM  </pre>
 * <pre> Project: sisyphus  </pre>
 *
 * @author houbinbin
 * @since 0.0.6
 */
public final class Recovers {

    private Recovers(){}

    /**
     * 没有任何恢复操作实例
     * @return recover 实例
     */
    public static Recover noRecover() {
        return NoRecover.getInstance();
    }

}
