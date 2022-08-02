package com.taotao.cloud.sys.biz.modules.quartz.service;

import com.taotao.cloud.sys.biz.modules.classloader.ClassloaderService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
public class ClassLoaderAop {

    @Pointcut("@annotation(com.sanri.tools.modules.quartz.service.InvokeClassLoader)")
    public void pointcut(){}

    @Autowired
    private ClassloaderService classloaderService;

    @Before("pointcut()")
    public void beforeInvoke(JoinPoint joinPoint){
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        String classloaderName = request.getParameter("classloaderName");
        ClassLoader classloader = classloaderService.getClassloader(classloaderName);
        CascadingClassLoadHelperExtend.ClassLoaderHelperCustom.classLoaderThreadLocal.set(classloader);
    }
}
