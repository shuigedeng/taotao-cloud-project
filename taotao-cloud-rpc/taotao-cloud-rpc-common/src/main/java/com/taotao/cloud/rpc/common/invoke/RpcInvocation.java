package com.taotao.cloud.rpc.common.invoke;

import com.taotao.cloud.rpc.common.URL;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * RPC 调用信息实现
 */
public class RpcInvocation implements Invocation, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 服务接口名
     */
    private String serviceInterface;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 参数类型
     */
    private Class<?>[] parameterTypes;

    /**
     * 参数值
     */
    private Object[] arguments;

    /**
     * 附加信息
     */
    private Map<String, String> attachments = new HashMap<>();

    /**
     * 调用者 URL
     */
    private URL invokerUrl;

    public RpcInvocation() {
    }

    public RpcInvocation(String serviceInterface, String methodName, Class<?>[] parameterTypes, Object[] arguments) {
        this.serviceInterface = serviceInterface;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.arguments = arguments;
    }

    public RpcInvocation(String serviceInterface, String methodName, Class<?>[] parameterTypes,
                         Object[] arguments, Map<String, String> attachments) {
        this(serviceInterface, methodName, parameterTypes, arguments);
        if (attachments != null) {
            this.attachments.putAll(attachments);
        }
    }

    @Override
    public String getServiceInterface() {
        return serviceInterface;
    }

    public void setServiceInterface(String serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    @Override
    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    @Override
    public Map<String, String> getAttachments() {
        return attachments;
    }

    public void setAttachments(Map<String, String> attachments) {
        this.attachments = attachments;
    }

    @Override
    public String getAttachment(String key) {
        return attachments.get(key);
    }

    @Override
    public String getAttachment(String key, String defaultValue) {
        return attachments.getOrDefault(key, defaultValue);
    }

    @Override
    public void setAttachment(String key, String value) {
        attachments.put(key, value);
    }

    @Override
    public URL getInvokerUrl() {
        return invokerUrl;
    }

    public void setInvokerUrl(URL invokerUrl) {
        this.invokerUrl = invokerUrl;
    }

    @Override
    public String getVersion() {
        return getAttachment("version", "1.0.0");
    }

    @Override
    public String getGroup() {
        return getAttachment("group", "");
    }
}
