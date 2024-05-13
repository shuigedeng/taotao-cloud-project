package com.github.houbb.rpc.common.support.status.enums;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.rpc.common.exception.ShutdownException;

/**
 * <p> project: rpc-StatusEnum </p>
 * <p> create on 2019/10/30 20:37 </p>
 *
 * status=0 初始化
 *
 * status=1 可用
 *
 * status=2 等待关闭
 *
 * status=3 正常关闭完成
 *
 * status=4 超时关闭完成
 *
 * @author Administrator
 * @since 0.1.3
 */
public enum StatusEnum {

    /**
     * 初始化
     * @since 0.1.3
     */
    INIT(0),

    /**
     * 可用
     * @since 0.1.3
     */
    ENABLE(1),

    /**
     * 等待关闭
     * @since 0.1.3
     */
    WAIT_SHUTDOWN(2),

    /**
     * 关闭成功
     * @since 0.1.3
     */
    SHUTDOWN_SUCCESS(3),

    /**
     * 关闭超时
     * @since 0.1.3
     */
    SHUTDOWN_TIMEOUT(4),
    ;

    private static final Log LOG = LogFactory.getLog(StatusEnum.class);

    /**
     * 编码信息
     * @since 0.1.3
     */
    private final int code;

    StatusEnum(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }


    /**
     * 断言可用
     * @param statusCode 状态码
     * @see com.github.houbb.rpc.common.exception.ShutdownException 关闭异常
     * @since 0.1.4
     */
    public static void assertEnable(final int statusCode) {
        if(StatusEnum.ENABLE.code() != statusCode) {
            LOG.error("[Client] current status is: {} , not enable to send request", statusCode);
            throw new ShutdownException("Status is not enable to send request, may be during shutdown.");
        }
    }
}
