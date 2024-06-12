package com.taotao.cloud.rpc.common.common.rpc.domain;

import com.taotao.cloud.rpc.common.common.constant.enums.CallTypeEnum;

import java.util.List;

/**
 * 序列化相关处理
 * （1）调用创建时间-createTime
 * （2）调用方式 callType
 * （3）超时时间 timeOut
 *
 * 额外信息：
 * （1）上下文信息
 *
 * @author shuigedeng
 * @since 2024.06
 */
public interface RpcRequest extends BaseRpc {

    /**
     * 创建时间
     * @return 创建时间
     * @since 2024.06
     */
    long createTime();

    /**
     * 服务唯一标识
     * @return 服务唯一标识
     * @since 2024.06
     */
    String serviceId();

    /**
     * 方法名称
     * @return 方法名称
     * @since 2024.06
     */
    String methodName();

    /**
     * 方法类型名称列表
     * @return 名称列表
     * @since 2024.06
     */
    List<String> paramTypeNames();

    // 调用参数信息列表

    /**
     * 调用参数值
     * @return 参数值数组
     * @since 2024.06
     */
    Object[] paramValues();

    /**
     * 返回值类型
     * @return 返回值类型
     * @since 0.1.0
     */
    Class returnType();

    /**
     * 超时时间
     * @return 超时时间
     * @since 0.1.3
     */
    long timeout();

    /**
     * 调用方式
     * @return 调用方式
     * @since 0.1.3
     */
    CallTypeEnum callType();

}
