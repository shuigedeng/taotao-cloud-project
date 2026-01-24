package com.taotao.cloud.gateway.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.webmvc.autoconfigure.error.ErrorViewResolver;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Component
public class GatewayErrorViewResolver implements ErrorViewResolver {
    
    @Override
    public ModelAndView resolveErrorView(HttpServletRequest request, 
                                         HttpStatus status,
                                         Map<String, Object> model) {
        
        // 网关特定的错误视图解析
        if (isGatewayError(status, model)) {
            return resolveGatewayErrorView(request, status, model);
        }
        
        // 默认错误视图
        return new ModelAndView("error/" + status.value(), model);
    }
    
    private boolean isGatewayError(HttpStatus status, Map<String, Object> model) {
        // 判断是否为网关错误
        return status == HttpStatus.BAD_GATEWAY || 
               status == HttpStatus.GATEWAY_TIMEOUT ||
               status == HttpStatus.TOO_MANY_REQUESTS ||
               (model.containsKey("exception") && 
                model.get("exception").toString().contains("Gateway"));
    }
    
    private ModelAndView resolveGatewayErrorView( HttpServletRequest request,
                                                HttpStatus status,
                                                Map<String, Object> model) {
        Map<String, Object> gatewayModel = new HashMap<>(model);
        
        // 添加网关特定信息
        gatewayModel.put("gatewayInstance", System.getenv("HOSTNAME"));
        gatewayModel.put("gatewayVersion", "1.0.0");
        gatewayModel.put("requestId", request.getAttribute("X-Request-ID"));
        
        // 根据错误类型选择视图
        String viewName;
        if (status == HttpStatus.BAD_GATEWAY) {
            viewName = "gateway/502";
            gatewayModel.put("suggestion", 
                "The backend service may be temporarily unavailable");
        } else if (status == HttpStatus.GATEWAY_TIMEOUT) {
            viewName = "gateway/504";
            gatewayModel.put("suggestion", 
                "The request took too long to process");
        } else if (status == HttpStatus.TOO_MANY_REQUESTS) {
            viewName = "gateway/429";
            gatewayModel.put("retryAfter", "60");
        } else {
            viewName = "gateway/error";
        }
        
        return new ModelAndView(viewName, gatewayModel);
    }
}
