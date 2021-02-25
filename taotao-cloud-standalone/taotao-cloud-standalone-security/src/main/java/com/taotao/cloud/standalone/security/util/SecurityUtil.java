package com.taotao.cloud.standalone.security.util;

import com.alibaba.fastjson.JSON;
import com.taotao.cloud.standalone.common.exception.PreBaseException;
import com.taotao.cloud.standalone.common.utils.R;
import com.taotao.cloud.standalone.security.PreSecurityUser;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Classname SecurityUtil
 * @Description 安全服务工具类
 * @Author 李号东 lihaodongmail@163.com
 * @since 2019-05-08 10:12
 * @Version 1.0
 */
@UtilityClass
public class SecurityUtil {

    public void writeJavaScript(R r, HttpServletResponse response) throws IOException {
        response.setStatus(200);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.write(JSON.toJSONString(r));
        printWriter.flush();
    }

    /**
     * 获取Authentication
     */
    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * @Author 李号东
     * @Description 获取用户
     * @since 11:29 2019-05-10
     **/
    public PreSecurityUser getUser(){
        try {
            return (PreSecurityUser) getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new PreBaseException("登录状态过期", HttpStatus.UNAUTHORIZED.value());
        }
    }
}
