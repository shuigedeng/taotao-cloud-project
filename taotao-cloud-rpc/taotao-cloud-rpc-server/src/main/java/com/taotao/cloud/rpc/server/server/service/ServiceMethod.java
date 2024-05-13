package com.taotao.cloud.rpc.server.server.service;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 服务方法类
 *
 * @author shuigedeng
 * @since 0.0.6
 */
public interface ServiceMethod {

    /**
     * 方法名称
     * @return 名称
     * @since 0.0.6
     */
    String name();

    /**
     * 方法类型列表
     * @return 方法类型列表
     * @since 0.0.6
     */
    Class[] paramTypes();

    /**
     * 方法类型名称列表
     * @return 方法名称列表
     * @since 0.0.6
     */
    List<String> paramTypeNames();

    /**
     * 方法信息
     * @return 方法信息
     * @since 0.0.6
     */
    Method method();

}
