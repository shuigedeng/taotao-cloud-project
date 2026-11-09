package com.taotao.cloud.mq.consistency.ttc.remoting.protocol;

/**
 * 请求码（对标 RocketMQ RequestCode）.
 * <p>
 * 协议层专用的命令字定义；具体业务实现在 {@code z-mq-broker} / {@code z-mq-nameserver}
 * 两个模块各注册对应的 Processor。
 * <p>
 * 数值划分：
 * <ul>
 *   <li>1-99   NameServer 内部请求（注册、注销、查询）</li>
 *   <li>100-199 Broker 内部请求（心跳）</li>
 *   <li>200-299 客户端 → Broker（发送、拉取、心跳）</li>
 *   <li>300-399 客户端 → Nameserver（路由查询）</li>
 *   <li>400-499 管理命令（创建/更新 Topic 等）</li>
 * </ul>
 */
public class RequestCode {

    private RequestCode() {
    }

    // ============== NameServer 内部 (1-99) ==============

    /** Broker 注册到 NameServer. */
    public static final int REGISTER_BROKER = 1;

    /** Broker 从 NameServer 注销. */
    public static final int UNREGISTER_BROKER = 2;

    /** 查询指定 Topic 的路由信息. */
    public static final int GET_ROUTEINFO_BY_TOPIC = 3;

    /** 查询所有 Broker 列表. */
    public static final int GET_BROKER_CLUSTER_INFO = 4;

    /** 更新/创建 Topic 配置. */
    public static final int UPDATE_AND_CREATE_TOPIC = 5;

    /** 更新所有 NameServer 上 Topic 配置. */
    public static final int UPDATE_AND_CREATE_TOPIC_LIST = 6;

    /** 删除 Topic. */
    public static final int DELETE_TOPIC = 7;

    /** 查询所有 Topic 列表. */
    public static final int GET_ALL_TOPIC_LIST = 8;

    /** 获取 NameServer KV 配置. */
    public static final int GET_KV_CONFIG = 9;

    /** 更新 NameServer KV 配置. */
    public static final int PUT_KV_CONFIG = 10;

    /** 删除 NameServer KV 配置. */
    public static final int DELETE_KV_CONFIG = 11;

    /** 查询 Cluster 列表. */
    public static final int GET_CLUSTER_INFO = 12;

    // ============== Broker 内部 (100-199) ==============

    /** Broker 主动上报心跳到 NameServer. */
    public static final int BROKER_HEARTBEAT = 100;

    // ============== 客户端 → Broker (200-299) ==============

    /** 发送消息（通用入口） */
    public static final int SEND_MESSAGE = 200;

    /** 发送消息 v2（含事务） */
    public static final int SEND_MESSAGE_V2 = 201;

    /** 发送消息 批量 */
    public static final int SEND_BATCH_MESSAGE = 202;

    /** 客户端主动向 Broker 拉取消息 */
    public static final int PULL_MESSAGE = 210;

    /** 拉取消息（带时间戳） */
    public static final int PULL_MESSAGE_V2 = 211;

    /** 消费者向 Broker 汇报消费进度 */
    public static final int UPDATE_CONSUMER_OFFSET = 220;

    /** 消费者心跳. */
    public static final int CONSUMER_HEARTBEAT = 221;

    /** 消费者心跳 v2（带 Set 信息） */
    public static final int CONSUMER_HEARTBEAT_V2 = 222;

    /** 查询消息（按 Key/Offset） */
    public static final int QUERY_MESSAGE = 230;

    /** 查询消息（按 Key） */
    public static final int VIEW_MESSAGE_BY_KEY = 231;

    /** 客户端 → Broker 端事务检查. */
    public static final int CHECK_TRANSACTION_STATE = 250;

    /** 通知 Broker 事务结束（commit/rollback） */
    public static final int END_TRANSACTION = 251;

    /** Broker 之间的主从复制. */
    public static final int SEND_TRANSFER_MSG = 300;

    // ============== 客户端 → Nameserver (300-399) ==============

    /** 客户端查询某 Topic 路由. */
    public static final int GET_ROUTE_BY_TOPIC = 311;

    // ============== Broker 内部 RPC (Broker → Broker, 320-399) ==============

    /** Slave 从 Master 拉取所有 Topic 配置. */
    public static final int GET_ALL_TOPIC_CONFIG = 320;

    /** Slave 从 Master 拉取所有 ConsumerOffset. */
    public static final int GET_ALL_CONSUMER_OFFSET = 321;

    /** Slave 从 Master 拉取所有 DelayOffset. */
    public static final int GET_ALL_DELAY_OFFSET = 322;

    /** Slave 从 Master 拉取所有 SubscriptionGroup 配置. */
    public static final int GET_ALL_SUBSCRIPTION_GROUP = 323;

    /** Slave 查询 Master 的数据版本号 (用于增量同步判断). */
    public static final int QUERY_DATA_VERSION = 324;

    /** Master 向 Slave 推送 CommitLog 数据块 (HA 通道). */
    public static final int HA_PUSH_COMMITLOG = 350;

    /** Slave 向 Master 报告本地最大 offset (HA 通道). */
    public static final int HA_REPORT_OFFSET = 351;

    // ============== 管理命令 (400-499) ==============

    /** 创建/更新 Topic. */
    public static final int CREATE_TOPIC = 400;

    /** 检查 Topic 路由是否存在. */
    public static final int CHECK_ROUTE_EXIST = 401;

    /** 查询 Broker 自身配置. */
    public static final int GET_BROKER_CONFIG = 402;

    /** 重置 ConsumerOffset 偏移. */
    public static final int RESET_CONSUMER_OFFSET = 410;

    /** 终止 Consumer. */
    public static final int TERMINATE_CONSUMER = 411;

    /**
     * 是否为对 Nameserver 的请求 (走 Namesrv 端口).
     */
    public static boolean isNameServerRequest(int code) {
        return code >= 1 && code < 99;
    }

    /**
     * 是否为对 Broker 的请求 (走 Broker 端口).
     */
    public static boolean isBrokerRequest(int code) {
        return code >= 100 && code < 500;
    }
}
