package com.taotao.cloud.mq.common.support.status;

/**
 * @author shuigedeng
 * @since 2024.05
 */
public class StatusManager implements IStatusManager {

    private boolean status;

    private boolean initFailed;

    @Override
    public boolean status() {
        return this.status;
    }

    @Override
    public IStatusManager status(boolean status) {
        this.status = status;

        return this;
    }

    @Override
    public boolean initFailed() {
        return initFailed;
    }

    @Override
    public StatusManager initFailed(boolean initFailed) {
        this.initFailed = initFailed;
        return this;
    }
}
