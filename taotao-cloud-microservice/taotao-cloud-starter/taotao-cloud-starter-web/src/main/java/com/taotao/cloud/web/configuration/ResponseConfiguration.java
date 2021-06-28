package com.taotao.cloud.web.configuration;

import com.taotao.cloud.web.exception.AbstractGlobalResponseBodyAdvice;
import javax.servlet.Servlet;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * 全局统一返回值 包装器
 *
 * @author zuihou
 * @date 2020/12/30 2:48 下午
 */
@ConditionalOnClass({Servlet.class, DispatcherServlet.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
//@RestControllerAdvice(basePackages = {"com.taotao.cloud.*.biz.controller"}, annotations = {
//	RestController.class, Controller.class})
@RestControllerAdvice(basePackages = {"com.taotao.cloud.*.biz.controller"})
public class ResponseConfiguration extends AbstractGlobalResponseBodyAdvice {

}
