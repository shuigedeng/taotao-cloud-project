//package com.taotao.cloud.gateway.error;
//
//import jakarta.servlet.RequestDispatcher;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.boot.webmvc.error.ErrorAttributes;
//import org.springframework.boot.webmvc.error.ErrorController;
//import org.springframework.context.annotation.Bean;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.servlet.ModelAndView;
//
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
//
//@Controller
//@RequestMapping("${server.error.path:${error.path:/error}}")
//public class GatewayErrorController implements ErrorController {
//
//    private final ErrorAttributes errorAttributes;
//
//    public GatewayErrorController(ErrorAttributes errorAttributes) {
//        this.errorAttributes = errorAttributes;
//    }
//
//    // 处理 HTML 错误页面
//    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
//    public ModelAndView errorHtml( HttpServletRequest request,
//                                  HttpServletResponse response) {
//        HttpStatus status = getStatus(request);
//        Map<String, Object> model = getErrorAttributes(request,
//            MediaType.TEXT_HTML.includes(MediaType.parseMediaType(
//                request.getHeader("Accept"))));
//
//        // 自定义网关错误页面
//        if (status == HttpStatus.BAD_GATEWAY) {
//            model.put("gatewayError", true);
//            model.put("targetService", extractTargetService(request));
//        }
//
//        return new ModelAndView("gateway/error", model, status);
//    }
//	@Bean
//	public RouterFunction<ServerResponse> gatewayRouterFunctionsAddReqHeader() {
//		return route("add_request_header_route")
//			.GET("/red/{segment}", http())
//			.before(uri("https://example.org"))
//			.before(addRequestHeader("X-Request-red", "blue-{segment}"));
//	}
//    // 处理 JSON 错误响应
//    @RequestMapping
//    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
//        Map<String, Object> body = getErrorAttributes(request,
//            getTraceParameter(request));
//        HttpStatus status = getStatus(request);
//
//        // 网关特有错误处理
//        if (status == HttpStatus.TOO_MANY_REQUESTS) {
//            body.put("retryAfter", "60");
//            body.put("errorType", "RATE_LIMIT_EXCEEDED");
//        }
//
//        if (status == HttpStatus.BAD_GATEWAY) {
//            body.put("errorType", "SERVICE_UNAVAILABLE");
//            body.put("suggestedAction", "retry_after_interval");
//        }
//
//        return new ResponseEntity<>(body, status);
//    }
//
//    // 网关特定的错误端点
//    @GetMapping("/gateway/routes")
//    public ResponseEntity<?> getRoutesError(HttpServletRequest request) {
//        Map<String, Object> error = new HashMap<>();
//        error.put("message", "Route configuration error");
//        error.put("timestamp", LocalDateTime.now());
//        error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
//
//        // 返回路由配置错误信息
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//            .body(error);
//    }
//
//    private HttpStatus getStatus(HttpServletRequest request) {
//        Integer statusCode = (Integer) request.getAttribute(
//            RequestDispatcher.ERROR_STATUS_CODE);
//        if (statusCode == null) {
//            return HttpStatus.INTERNAL_SERVER_ERROR;
//        }
//        return HttpStatus.valueOf(statusCode);
//    }
//}
