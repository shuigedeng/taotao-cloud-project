package com.taotao.cloud.third.client.support.forest.config;

import com.dtflys.forest.http.ForestResponse;
import com.dtflys.forest.logging.DefaultLogHandler;
import com.dtflys.forest.logging.RequestLogMessage;
import com.dtflys.forest.logging.ResponseLogMessage;
import com.dtflys.forest.utils.StringUtils;

/**
 * 我自定义的日志处理器
 */
public class MyLogHandler extends DefaultLogHandler {
  
    /**
     * 所有的请求最终会调用这个方法打印日志
     */
    @Override
    public void logContent(String content) {
        super.logContent("[哈哈，这是我自己的日志]: " + content);
    }

    /**
     * 该方法生成Forest请求的日志内容字符串
     * @param requestLogMessage 请求日志字符串
     * @return 日志内容字符串
     */
    @Override
    protected String requestLoggingContent(RequestLogMessage requestLogMessage) {
        StringBuilder builder = new StringBuilder();
        builder.append("请求: \n\t");
        // 插入重试信息
        builder.append(retryContent(requestLogMessage));
        // 插入代理信息
        builder.append(proxyContent(requestLogMessage));
        // 插入请求类型变更历史信息
        builder.append(requestTypeChangeHistory(requestLogMessage));
        // 插入请求行信息
        builder.append(requestLogMessage.getRequestLine());
        // 获取并插入所有请求头内容
        String headers = requestLoggingHeaders(requestLogMessage);
        if (StringUtils.isNotEmpty(headers)) {
            builder.append("\n\t请求头: \n");
            builder.append(headers);
        }
        // 获取并插入所有请求体内容
        String body = requestLoggingBody(requestLogMessage);
        if (StringUtils.isNotEmpty(body)) {
            builder.append("\n\t请求体: \n");
            builder.append(body);
        }
        return builder.toString();
    }

    /**
     * 该方法生成Forest请求响应结果的日志内容字符串
     * @param responseLogMessage 请求响应日志字符串
     * @return 日志内容字符串
     */
    @Override
    protected String responseLoggingContent(ResponseLogMessage responseLogMessage) {
        ForestResponse response = responseLogMessage.getResponse();
        if (response != null && response.getException() != null) {
            return "[网络错误]: " + response.getException().getMessage();
        }
        // 获取请求响应状态码
        int status = responseLogMessage.getStatus();
        // 获取请求响应时间
        long time = responseLogMessage.getTime();
        if (status >= 0) {
            return "请求响应: 状态码: " + status + ", 耗时: " + time + "ms";
        } else {
            return "[网络错误]: 未知的网络错误!";
        }
    }

}
