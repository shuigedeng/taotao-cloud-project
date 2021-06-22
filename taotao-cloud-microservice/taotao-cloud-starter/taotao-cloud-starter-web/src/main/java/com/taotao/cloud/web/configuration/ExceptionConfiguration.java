package com.taotao.cloud.web.configuration;

import com.taotao.cloud.web.exception.DefaultExceptionAdvice;
import javax.servlet.Servlet;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
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
@RestControllerAdvice(annotations = {RestController.class, Controller.class})
public class ExceptionConfiguration extends DefaultExceptionAdvice {

}
