package com.taotao.cloud.rpc.common.common.support.status.service.impl;

import com.taotao.cloud.rpc.common.common.support.status.enums.StatusEnum;
import com.taotao.cloud.rpc.common.common.support.status.service.StatusManager;
import javax.annotation.concurrent.ThreadSafe;

/**
 * 状态管理
 *
 * <p> project: rpc-StatusManager </p>
 * <p> create on 2019/10/30 20:48 </p>
 *
 * @author Administrator
 * @since 0.1.3
 */
@ThreadSafe
public class DefaultStatusManager implements StatusManager {

    /**
     * 状态
     * @since 0.1.3
     */
    private volatile int status = StatusEnum.INIT.code();

    @Override
    public int status() {
        return status;
    }

    @Override
    public DefaultStatusManager status(int status) {
        this.status = status;
        return this;
    }

    @Override
    public String toString() {
        return "DefaultStatusManager{" +
                "status=" + status +
                '}';
    }

}
