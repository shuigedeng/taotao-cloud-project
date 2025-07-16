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

package com.taotao.cloud.rpc.registry.apiregistry.base;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ApiRegistryHealthFilter implements Filter {
    @Override
    public void doFilter(
            ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String contextPath =
                org.springframework.util.StringUtils.trimTrailingCharacter(
                        request.getContextPath(), '/');
        String uri = request.getRequestURI();
        /*下线apiRegistry,一般在k8s CICD中使用*/
        if (uri.startsWith(contextPath + "/ttc/eureka/offline/")
                || uri.startsWith(contextPath + "/ttc/apiRegistry/offline/")) {
            //			BaseRegistry registry = ContextUtils.getBean(BaseRegistry.class,false);
            //            if(registry!=null){
            //                registry.close();
            //                write(response,"已下线");
            //				TtcSpringApplicationRunListener listener = ContextUtils.getBean(
            //					TtcSpringApplicationRunListener.class,false);
            //                if(listener!=null) {
            //                    listener.change(StatusEnum.STOPPING, () -> {
            //                        LogUtils.info( "apiRegistry 设置当前应用程序为退出中...");
            //                    });
            //                }
            //                LogUtils.info(ApiRegistryProperties.Project,"apiRegistry 服务被强制下线!");
            //            }
        }
        /*apiRegistry服务注册列表*/
        else if (uri.startsWith(contextPath + "/ttc/apiRegistry/")) {
            //			BaseRegistry registry = ContextUtils.getBean(BaseRegistry.class,false);
            //            if(registry!=null) {
            //				String report = registry.getReport();
            //                write(response,report.replaceAll("\r","").replace("\n","<br/>"));
            //            }
        } else {
            chain.doFilter(servletRequest, servletResponse);
        }
    }

    private void write(HttpServletResponse response, String text) throws IOException {
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().append(text);
        response.getWriter().flush();
        response.getWriter().close();
    }

    @Override
    public void destroy() {}
}
