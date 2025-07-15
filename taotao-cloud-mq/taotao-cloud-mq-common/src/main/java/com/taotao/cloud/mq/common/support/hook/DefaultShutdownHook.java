package com.taotao.cloud.mq.common.support.hook;

import com.taotao.cloud.mq.common.api.Destroyable;
import com.taotao.cloud.mq.common.support.invoke.IInvokeService;
import com.taotao.cloud.mq.common.support.status.IStatusManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 默认的 hook 实现
 * @since 2024.05
 */
public class DefaultShutdownHook extends AbstractShutdownHook {

    /**
     * DefaultShutdownHook logger
     */
	private static final Logger logger = LoggerFactory.getLogger(DefaultShutdownHook.class);

    /**
     * 调用管理类
     * @since 2024.05
     */
    private IInvokeService invokeService;

    /**
     * 销毁管理类
     * @since 2024.05
     */
    private Destroyable destroyable;

    /**
     * 状态管理类
     * @since 2024.05
     */
    private IStatusManager statusManager;

    /**
     * 为剩余的请求等待时间
     * @since 2024.05
     */
    private long waitMillsForRemainRequest = 60 * 1000;

    public IInvokeService getInvokeService() {
        return invokeService;
    }

    public void setInvokeService(IInvokeService invokeService) {
        this.invokeService = invokeService;
    }

    public Destroyable getDestroyable() {
        return destroyable;
    }

    public void setDestroyable(Destroyable destroyable) {
        this.destroyable = destroyable;
    }

    public IStatusManager getStatusManager() {
        return statusManager;
    }

    public void setStatusManager(IStatusManager statusManager) {
        this.statusManager = statusManager;
    }

    public long getWaitMillsForRemainRequest() {
        return waitMillsForRemainRequest;
    }

    public void setWaitMillsForRemainRequest(long waitMillsForRemainRequest) {
        this.waitMillsForRemainRequest = waitMillsForRemainRequest;
    }

    /**
     * （1）设置 status 状态为等待关闭
     * （2）查看是否 {@link IInvokeService#remainsRequest()} 是否包含请求
     * （3）超时检测-可以不添加，如果难以关闭成功，直接强制关闭即可。
     * （4）关闭所有线程池资源信息
     * （5）设置状态为成功关闭
     */
    @Override
    protected void doHook() {
        statusManager.status(false);
        // 设置状态为等待关闭
        logger.info("[Shutdown] set status to wait for shutdown.");

        // 循环等待当前执行的请求执行完成
        long startMills = System.currentTimeMillis();
        while (invokeService.remainsRequest()) {
            long currentMills = System.currentTimeMillis();
            long costMills = currentMills - startMills;
            if(costMills >= waitMillsForRemainRequest) {
                logger.warn("[Shutdown] still remains request, but timeout, break.");
                break;
            }

            logger.info("[Shutdown] still remains request, wait for a while.");
           // DateUtil.sleep(100);
        }

        // 销毁
        destroyable.destroyAll();

        // 设置状态为关闭成功
        statusManager.status(false);
        logger.info("[Shutdown] set status to shutdown success.");
    }

}
