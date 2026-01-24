//package com.taotao.cloud.gateway.error;
//
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.core.Ordered;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//@RestControllerAdvice
//@Order(Ordered.HIGHEST_PRECEDENCE)
//public class GatewayGlobalExceptionHandler {
//
//    // 处理所有未捕获异常
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<GatewayErrorResponse> handleAllExceptions(
//            Exception ex,
//            HttpServletRequest request) {
//
//        GatewayErrorResponse error = new GatewayErrorResponse();
//        error.setTimestamp(LocalDateTime.now());
//        error.setPath(request.getRequestURI());
//        error.setRequestId(getOrGenerateRequestId(request));
//
//        // 分类处理
//        if (ex instanceof GatewayTimeoutException) {
//            error.setStatus(HttpStatus.GATEWAY_TIMEOUT.value());
//            error.setError("Gateway Timeout");
//            error.setMessage("The request timed out while waiting for backend service");
//            error.setErrorCode("GATEWAY_TIMEOUT_001");
//
//            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT)
//                .header("X-Gateway-Error-Code", "TIMEOUT")
//                .body(error);
//
//        } else if (ex instanceof RateLimitExceededException) {
//            error.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
//            error.setError("Too Many Requests");
//            error.setMessage("Rate limit exceeded");
//            error.setErrorCode("RATE_LIMIT_001");
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.add("Retry-After", "60");
//            headers.add("X-RateLimit-Limit", "100");
//            headers.add("X-RateLimit-Remaining", "0");
//
//            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
//                .headers(headers)
//                .body(error);
//
//        } else {
//            // 默认错误处理
//            error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            error.setError("Internal Server Error");
//            error.setMessage("An unexpected error occurred");
//            error.setErrorCode("INTERNAL_001");
//
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(error);
//        }
//    }
//
//    // 处理网关转发异常
//    @ExceptionHandler(GatewayForwardException.class)
//    public ResponseEntity<GatewayErrorResponse> handleForwardException(
//            GatewayForwardException ex,
//            HttpServletRequest request) {
//
//        GatewayErrorResponse error = new GatewayErrorResponse();
//        error.setTimestamp(LocalDateTime.now());
//        error.setPath(request.getRequestURI());
//        error.setStatus(HttpStatus.BAD_GATEWAY.value());
//        error.setError("Bad Gateway");
//        error.setMessage(String.format(
//            "Failed to forward request to %s: %s",
//            ex.getTargetService(), ex.getMessage()));
//        error.setErrorCode("FORWARD_FAILED_001");
//        error.setTargetService(ex.getTargetService());
//
//        // 添加重试建议
//        if (ex.isRetryable()) {
//            error.setSuggestedAction("retry_after_30s");
//        }
//
//        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
//            .header("X-Target-Service", ex.getTargetService())
//            .header("X-Retryable", String.valueOf(ex.isRetryable()))
//            .body(error);
//    }
//
//    // 处理404路由不存在
//    @ExceptionHandler(NoRouteFoundException.class)
//    public ResponseEntity<GatewayErrorResponse> handleNoRoute(
//            NoRouteFoundException ex,
//            HttpServletRequest request) {
//
//        GatewayErrorResponse error = new GatewayErrorResponse();
//        error.setTimestamp(LocalDateTime.now());
//        error.setPath(request.getRequestURI());
//        error.setStatus(HttpStatus.NOT_FOUND.value());
//        error.setError("Route Not Found");
//        error.setMessage("No route configured for the requested path");
//        error.setErrorCode("ROUTE_NOT_FOUND_001");
//
//        // 提供可用的路由信息
//        error.setAvailableRoutes(getAvailableRoutes());
//
//        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//            .body(error);
//    }
//}
