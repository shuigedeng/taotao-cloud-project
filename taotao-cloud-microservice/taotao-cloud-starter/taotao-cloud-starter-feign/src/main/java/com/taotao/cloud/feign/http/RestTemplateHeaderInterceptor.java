package com.taotao.cloud.feign.http;

import cn.hutool.core.util.ObjectUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static com.taotao.cloud.feign.configuration.FeignInterceptorConfiguration.HEADER_NAME_LIST;


/**
 * 通过 RestTemplate 调用时，传递请求头和线程变量
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:24:51
 */
public class RestTemplateHeaderInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] bytes,
                                        ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders httpHeaders = request.getHeaders();

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            HEADER_NAME_LIST.forEach(headerName -> {
                //if (ObjectUtil.isNotEmpty(ContextUtil.get(headerName))) {
                //    httpHeaders.add(headerName, ContextUtil.get(headerName));
                //}
            });
            return execution.execute(request, bytes);
        }

        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        if (httpServletRequest == null) {
            LogUtil.warn("path={}, 在FeignClient API接口未配置FeignConfiguration类， 故而无法在远程调用时获取请求头中的参数!", request.getURI());
            return execution.execute(request, bytes);
        }

        HEADER_NAME_LIST.forEach(headerName -> {
            if (ObjectUtil.isNotEmpty(httpServletRequest.getHeader(headerName))) {
                httpHeaders.add(headerName, httpServletRequest.getHeader(headerName));
            }
        });

        return execution.execute(request, bytes);
    }
}
