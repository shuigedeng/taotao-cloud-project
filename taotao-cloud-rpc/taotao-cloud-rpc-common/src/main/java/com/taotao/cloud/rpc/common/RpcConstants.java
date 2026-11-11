package com.taotao.cloud.rpc.common;

/**
 * 框架公共常量
 */
public final class RpcConstants {

    private RpcConstants() {}

    /** 框架名 */
    public static final String FRAMEWORK_NAME = "ttc-rpc";
    /** 框架版本 */
    public static final String FRAMEWORK_VERSION = "1.0.0";

    // ====================== Key 常量 ======================

    public static final String KEY_INTERFACE = "interface";
    public static final String KEY_VERSION = "version";
    public static final String KEY_GROUP = "group";
    public static final String KEY_PROTOCOL = "protocol";
    public static final String KEY_SERIALIZATION = "serialization";
    public static final String KEY_TIMEOUT = "timeout";
    public static final String KEY_RETRIES = "retries";
    public static final String KEY_LOADBALANCE = "loadbalance";
    public static final String KEY_CLUSTER = "cluster";
    public static final String KEY_WEIGHT = "weight";
    public static final String KEY_ASYNC = "async";
    public static final String KEY_MOCK = "mock";
    public static final String KEY_GENERIC = "generic";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_TRACE_ID = "trace-id";
    public static final String KEY_APP_NAME = "application";
    public static final String KEY_ORGANIZATION = "organization";
    public static final String KEY_SIDE = "side";
    public static final String KEY_CATEGORY = "category";

    // ====================== side 值 ======================

    public static final String SIDE_PROVIDER = "provider";
    public static final String SIDE_CONSUMER = "consumer";

    // ====================== 分类值 ======================

    public static final String CATEGORY_PROVIDERS = "providers";
    public static final String CATEGORY_CONSUMERS = "consumers";
    public static final String CATEGORY_ROUTERS = "routers";
    public static final String CATEGORY_CONFIGURATORS = "configurators";

    // ====================== 头/附件键 ======================

    public static final String HEADER_TRACE_ID = "x-ttcrpc-trace-id";
    public static final String HEADER_SPAN_ID = "x-ttcrpc-span-id";
    public static final String HEADER_PARENT_SPAN_ID = "x-ttcrpc-parent-span-id";
    public static final String HEADER_TOKEN = "x-ttcrpc-token";
    public static final String HEADER_APP_NAME = "x-ttcrpc-app";
    public static final String HEADER_VERSION = "x-ttcrpc-version";
    public static final String HEADER_GROUP = "x-ttcrpc-group";
}
