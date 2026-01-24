//package com.taotao.cloud.gateway.error;
//
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.boot.webmvc.error.DefaultErrorAttributes;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.ServletWebRequest;
//import org.springframework.web.context.request.WebRequest;
//
//import java.time.ZoneId;
//import java.util.Map;
//
//@Component
//public class GatewayErrorAttributes extends DefaultErrorAttributes {
//
//    @Override
//    public Map<String, Object> getErrorAttributes( WebRequest webRequest,
//                                                  boolean includeStackTrace) {
//        Map<String, Object> errorAttributes = super.getErrorAttributes(
//            webRequest, includeStackTrace);
//
//        // 获取原始异常
//        Throwable error = getError(webRequest);
//
//        // 添加网关特定错误信息
//        if (error instanceof GatewayException) {
//            GatewayException gatewayEx = (GatewayException) error;
//            errorAttributes.put("gatewayErrorCode", gatewayEx.getErrorCode());
//            errorAttributes.put("targetService", gatewayEx.getTargetService());
//            errorAttributes.put("suggestedAction", gatewayEx.getSuggestedAction());
//
//            // 移除敏感信息（生产环境）
//            if (!includeStackTrace) {
//                errorAttributes.remove("trace");
//            }
//        }
//
//        // 添加请求信息
//        ServletWebRequest servletWebRequest = (ServletWebRequest) webRequest;
//        HttpServletRequest request = servletWebRequest.getRequest();
//
//        errorAttributes.put("requestId", request.getHeader("X-Request-ID"));
//        errorAttributes.put("clientIp", getClientIp(request));
//        errorAttributes.put("requestPath", request.getRequestURI());
//        errorAttributes.put("requestMethod", request.getMethod());
//
//        // 格式化时间戳
//        Object timestamp = errorAttributes.get("timestamp");
//        if (timestamp instanceof Date) {
//            errorAttributes.put("timestamp",
//                Instant.ofEpochMilli(((Date) timestamp).getTime())
//                       .atZone(ZoneId.systemDefault())
//                       .toLocalDateTime());
//        }
//
//        return errorAttributes;
//    }
//}
