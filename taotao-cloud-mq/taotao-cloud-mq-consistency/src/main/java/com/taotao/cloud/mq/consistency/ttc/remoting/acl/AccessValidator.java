package com.taotao.cloud.mq.consistency.ttc.remoting.acl;

import com.taotao.cloud.mq.consistency.ttc.common.message.Message;
import com.taotao.cloud.mq.consistency.ttc.remoting.protocol.RemotingCommand;

/**
 * 访问校验接口（对标 RocketMQ AccessValidator）.
 * <p>
 * Broker 端在处理请求前调用此接口进行权限校验：
 * <ul>
 *   <li>校验请求是否有权限执行（Topic 读写权限）</li>
 *   <li>支持用户名/密码认证</li>
 *   <li>支持 Topic 级别的访问控制</li>
 * </ul>
 */
public interface AccessValidator {

    /**
     * 校验请求权限。
     *
     * @param request 待校验的请求
     * @return 校验结果
     */
    AccessResult validate(RemotingCommand request);

    /**
     * 校验消息发送权限。
     *
     * @param topic   目标 Topic
     * @param message 待发送的消息
     * @return 校验结果
     */
    AccessResult validateSendMessage(String topic, Message message);

    /**
     * 校验消息拉取权限。
     *
     * @param topic         目标 Topic
     * @param consumerGroup 消费者组
     * @return 校验结果
     */
    AccessResult validatePullMessage(String topic, String consumerGroup);

    /**
     * 校验管理命令权限。
     *
     * @param command 管理命令
     * @return 校验结果
     */
    AccessResult validateAdminCommand(RemotingCommand command);

    /**
     * 访问校验结果。
     */
    class AccessResult {
        private final boolean passed;
        private final String message;
        private final int code;

        public AccessResult(boolean passed) {
            this(passed, null, 0);
        }

        public AccessResult(boolean passed, String message, int code) {
            this.passed = passed;
            this.message = message;
            this.code = code;
        }

        public boolean isPassed() {
            return passed;
        }

        public String getMessage() {
            return message;
        }

        public int getCode() {
            return code;
        }

        public static AccessResult ok() {
            return new AccessResult(true);
        }

        public static AccessResult deny(String message) {
            return new AccessResult(false, message, 101);
        }

        public static AccessResult deny(String message, int code) {
            return new AccessResult(false, message, code);
        }
    }
}
