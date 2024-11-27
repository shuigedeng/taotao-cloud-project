package com.taotao.cloud.job.core.worker.common.constant;

public enum TransportTypeEnum {
    /** serverDiscover **/
    ASSERT_APP,
    HEARTBEAT_CHECK,
    PING_SERVER,
    /** schedule **/
    HEARTBEAT_HEALTH_REPORT,
    /** register **/
    REGISTER_TO_NAMESERVER

}
