package com.taotao.cloud.java.javaee.s1.c5_springmvc.p2.java.interceptor;

import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyFileUploadInterceptor implements HandlerInterceptor {

    private Long maxFileuploadSize;

    public Long getMaxFileuploadSize() {
        return maxFileuploadSize;
    }

    public void setMaxFileuploadSize(Long maxFileuploadSize) {
        this.maxFileuploadSize = maxFileuploadSize;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断上传文件大小  1048576
        ServletRequestContext servletRequestContext = new ServletRequestContext(request);
        // 文件大小 byte
        long l = servletRequestContext.contentLength();
        if(l>maxFileuploadSize){
            throw new MaxUploadSizeExceededException(maxFileuploadSize);
        }
        return true;
    }
}
