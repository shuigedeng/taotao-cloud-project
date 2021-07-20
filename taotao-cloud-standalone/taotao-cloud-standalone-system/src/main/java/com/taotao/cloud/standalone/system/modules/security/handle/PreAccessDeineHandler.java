package com.taotao.cloud.standalone.system.modules.security.handle;

import cn.hutool.http.HttpStatus;
import com.taotao.cloud.standalone.common.utils.R;
import com.taotao.cloud.standalone.security.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Classname PreAccessDeineHandler
 * @Description 用来解决匿名用户访问无权限资源时的异常
 * @Author shuigedeng
 * @since 2019/12/19 3:35 下午
 * @Version 1.0
 */
@Slf4j
public class PreAccessDeineHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        log.error("请求访问: " + request.getRequestURI() + " 接口， 没有访问权限");
        SecurityUtil.writeJavaScript(R.error(HttpStatus.HTTP_UNAUTHORIZED, "请求访问:" + request.getRequestURI() + "接口,没有访问权限"), response);
    }
}
