package com.taotao.cloud.im.biz.platform.common.web.interceptor;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONUtil;
import com.platform.common.constant.HeadConstant;
import com.platform.common.web.domain.AjaxResult;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 设备拦截器
 */
@Component
public class DeviceInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String device = ServletUtil.getHeader(request, HeadConstant.DEVICE, CharsetUtil.UTF_8);
        if (StringUtils.isEmpty(device)) {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSONUtil.toJsonStr(AjaxResult.fail("请求不正确")));
            return false;
        }
        return true;
    }

}
