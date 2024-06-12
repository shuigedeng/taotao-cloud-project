package com.taotao.cloud.rpc.common.common.support.hook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * rpc 关闭 hook
 * （1）可以添加对应的 hook 管理类
 *
 * @since 0.1.3
 */
public abstract class AbstractShutdownHook implements RpcShutdownHook {

    /**
     * AbstractShutdownHook logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(AbstractShutdownHook.class);

    @Override
    public void hook() {
        LOG.info("[Shutdown Hook] start");
        this.doHook();
        LOG.info("[Shutdown Hook] end");
    }

    /**
     * 执行 hook 操作
     * @since 0.1.3
     */
    protected abstract void doHook();

}
