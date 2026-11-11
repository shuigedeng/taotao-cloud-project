package com.taotao.cloud.rpc.common.invoke;

import com.taotao.cloud.rpc.common.URL;

import java.io.Serializable;
import java.util.Map;

/**
 * 调用信息封装
 * 参考 Dubbo 的 Invocation 设计
 */
public interface Invocation extends Serializable {

    /**
     * 获取服务接口名
     */
    String getServiceInterface();

    /**
     * 获取方法名
     */
    String getMethodName();

    /**
     * 获取参数类型
     */
    Class<?>[] getParameterTypes();

    /**
     * 获取参数
     */
    Object[] getArguments();

    /**
     * 获取附加信息
     */
    Map<String, String> getAttachments();

    /**
     * 获取单个附加信息
     */
    String getAttachment(String key);

    /**
     * 获取单个附加信息，带默认值
     */
    String getAttachment(String key, String defaultValue);

    /**
     * 设置附加信息
     */
    void setAttachment(String key, String value);

    /**
     * 获取调用者的 URL
     */
    URL getInvokerUrl();

    /**
     * 获取服务版本
     */
    default String getVersion() {
        return getAttachment("version", "1.0.0");
    }

    /**
     * 获取服务分组
     */
    default String getGroup() {
        return getAttachment("group", "");
    }
}
