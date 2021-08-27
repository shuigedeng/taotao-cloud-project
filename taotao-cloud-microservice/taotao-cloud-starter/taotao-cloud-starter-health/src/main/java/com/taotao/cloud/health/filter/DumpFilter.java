package com.taotao.cloud.health.filter;

import com.yh.csx.bsf.core.util.ContextUtils;
import com.yh.csx.bsf.health.dump.DumpProvider;
import lombok.val;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import javax.servlet.*;

/**
 * @author: chejiangyi
 * @version: 2019-09-07 13:31
 **/
public class DumpFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        val request = (HttpServletRequest) servletRequest;
        val response = (HttpServletResponse) servletResponse;
        val conetextPath = org.springframework.util.StringUtils.trimTrailingCharacter(request.getContextPath(),'/');
        val uri = request.getRequestURI();
        if(uri.startsWith(conetextPath+"/bsf/health/dump/")){
            DumpProvider dumpProvider = ContextUtils.getBean(DumpProvider.class,false);
            if(uri.startsWith(conetextPath+"/bsf/health/dump/zip/")){
                dumpProvider.zip(request.getParameter("name"));
            }
            else if(uri.startsWith(conetextPath+"/bsf/health/dump/download/")){
                dumpProvider.download(request.getParameter("name"));
            }
            else if(uri.startsWith(conetextPath+"/bsf/health/dump/do/")){
                dumpProvider.dump();
            }
            else{
                dumpProvider.list();
            }
        }

    }

    @Override
    public void destroy() {

    }
}
