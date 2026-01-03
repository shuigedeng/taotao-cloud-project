package com.taotao.cloud.message.biz.channels.sse;

/**
 * MessageDTO
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public class MessageDTO<T> {

    private String fromUserName;
    private String targetUserName;
    private T message;
    private String messageType;

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName( String fromUserName ) {
        this.fromUserName = fromUserName;
    }

    public T getMessage() {
        return message;
    }

    public void setMessage( T message ) {
        this.message = message;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType( String messageType ) {
        this.messageType = messageType;
    }

    public String getTargetUserName() {
        return targetUserName;
    }

    public void setTargetUserName( String targetUserName ) {
        this.targetUserName = targetUserName;
    }

    public static enum Type {
        TYPE_NEW("0000"), TYPE_TEXT("1000"), TYPE_BYTE("1001");
        private String messageType;

        Type( String messageType ) {
            this.messageType = messageType;
        }

        public String getMessageType() {
            return messageType;
        }

        public void setMessageType( String messageType ) {
            this.messageType = messageType;
        }

    }
}
