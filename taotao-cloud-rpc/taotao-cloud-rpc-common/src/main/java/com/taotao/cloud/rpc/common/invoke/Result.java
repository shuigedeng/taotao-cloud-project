package com.taotao.cloud.rpc.common.invoke;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 调用结果封装
 * 参考 Dubbo 的 Result 设计
 */
public interface Result extends Serializable {

    /**
     * 创建成功结果
     */
    static Result success(Object value) {
        return new RpcResult(value);
    }

    /**
     * 创建异常结果
     */
    static Result error(Throwable exception) {
        return new RpcResult(exception);
    }

    /**
     * 获取返回值
     */
    Object getValue();

    /**
     * 获取异常
     */
    Throwable getException();

    /**
     * 是否有异常
     */
    boolean hasException();

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
     * 递归获取结果（用于异步转同步）
     */
    Result getRecursionResult();

    // ==================== 静态工厂方法 ====================

    /**
     * 获取异步 Future
     */
    CompletableFuture<Result> getResultFuture();

    /**
     * 是否异步
     */
    boolean isAsync();

    /**
     * 默认实现类
     */
    class RpcResult implements Result {
        private static final long serialVersionUID = 1L;

        private Object value;
        private Throwable exception;
        private Map<String, String> attachments = new HashMap<>();

        public RpcResult() {
        }

        public RpcResult(Object value) {
            this.value = value;
        }

        public RpcResult(Throwable exception) {
            this.exception = exception;
        }

        @Override
        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        @Override
        public Throwable getException() {
            return exception;
        }

        public void setException(Throwable exception) {
            this.exception = exception;
        }

        @Override
        public boolean hasException() {
            return exception != null;
        }

        @Override
        public Map<String, String> getAttachments() {
            return attachments;
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
        public Result getRecursionResult() {
            return this;
        }

        @Override
        public CompletableFuture<Result> getResultFuture() {
            return CompletableFuture.completedFuture(this);
        }

        @Override
        public boolean isAsync() {
            return false;
        }
    }
}
