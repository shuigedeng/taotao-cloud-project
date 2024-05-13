package com.taotao.cloud.mq.common.dto.req.component;

/**
 * @author shuigedeng
 * @since 2024.05
 */
public class MqConsumerUpdateStatusDto {

    /**
     * 消息唯一标识
     * @since 2024.05
     */
    private String messageId;

    /**
     * 消息状态
     * @since 2024.05
     */
    private String messageStatus;

    /**
     * 消费者分组名称
     * @since 2024.05
     */
    private String consumerGroupName;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String getConsumerGroupName() {
        return consumerGroupName;
    }

    public void setConsumerGroupName(String consumerGroupName) {
        this.consumerGroupName = consumerGroupName;
    }

    @Override
    public String toString() {
        return "MqConsumerUpdateStatusDto{" +
                "messageId='" + messageId + '\'' +
                ", messageStatus='" + messageStatus + '\'' +
                ", consumerGroupName='" + consumerGroupName + '\'' +
                '}';
    }

}
