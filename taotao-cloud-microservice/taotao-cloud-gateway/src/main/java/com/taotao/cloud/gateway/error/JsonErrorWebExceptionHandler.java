/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.gateway.error;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.log.LogUtils;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.WebProperties.Resources;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.cloud.gateway.support.TimeoutException;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;

/**
 * 自定义异常处理
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/4/29 22:12
 */
public class JsonErrorWebExceptionHandler extends DefaultErrorWebExceptionHandler {

    public JsonErrorWebExceptionHandler(
            ErrorAttributes errorAttributes,
            Resources resources,
            ErrorProperties errorProperties,
            ApplicationContext applicationContext) {
        super(errorAttributes, resources, errorProperties, applicationContext);
    }

    // @Override
    // protected Map<String, Object> getErrorAttributes(ServerRequest request,
    //	boolean includeStackTrace) {
    //	Throwable error = super.getError(request);
    //	LogUtils.error(error.getMessage(), error);
    //	return responseError(error.getMessage());
    // }

    @Override
    protected Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Throwable error = super.getError(request);

        LogUtils.error("请求发生异常，请求URI：{}，请求方法：{}，异常信息：{}", request.path(), request.methodName(), error.getMessage());
        LogUtils.error(error.getMessage(), error);

        String errorMessage;
        if (error instanceof NotFoundException) {
            String serverId = StringUtils.substringAfterLast(error.getMessage(), "Unable to find instance for ");
            serverId = StringUtils.replace(serverId, "\"", StringUtils.EMPTY);
            errorMessage = String.format("无法找到%s服务", serverId);
        } else if (StringUtils.containsIgnoreCase(error.getMessage(), "connection refused")) {
            errorMessage = "目标服务拒绝连接";
        } else if (error instanceof TimeoutException) {
            errorMessage = "访问服务超时";
        } else if (error instanceof ResponseStatusException
                && StringUtils.containsIgnoreCase(error.getMessage(), HttpStatus.NOT_FOUND.toString())) {
            errorMessage = "未找到该资源";
        } else {
            errorMessage = "网关异常";
        }

        return responseError(errorMessage);
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    @Override
    protected int getHttpStatus(Map<String, Object> errorAttributes) {
        return HttpStatus.OK.value();
    }

    /**
     * 构建返回的JSON数据格式
     *
     * @param errorMessage 异常信息
     */
    public static Map<String, Object> responseError(String errorMessage) {
        Result<Object> result = Result.fail(errorMessage);
        Map<String, Object> res = new HashMap<>();
        res.put("errorMsg", result.getErrorMsg());
        res.put("code", result.getCode());
        res.put("success", result.isSuccess());
        res.put("requestId", result.getRequestId());
        LocalDateTime timestamp = result.getTimestamp();
        timestamp = timestamp == null ? LocalDateTime.now() : timestamp;
        res.put("timestamp", timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        // Map<String, Object> map = BeanUtil.beanToMap(result, false, false);
        // LocalDateTime timestamp = (LocalDateTime) map
        //	.getOrDefault("timestamp", LocalDateTime.now());
        // map.put("timestamp", timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return res;
    }
}
