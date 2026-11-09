package com.taotao.cloud.mq.consistency.ttc.store;

/**
 * 写入消息状态（对标 RocketMQ PutMessageStatus）.
 */
public enum PutMessageStatus {
    PUT_OK,
    FLUSH_DISK_TIMEOUT,
    CREATE_MAPPED_FILE_FAILED,
    MESSAGE_ILLEGAL,
    PROPERTIES_SIZE_EXCEEDED,
    OS_PAGE_CACHE_BUSY,
    UNKNOWN_ERROR,
    SERVICE_NOT_AVAILABLE
}
