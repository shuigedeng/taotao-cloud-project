package com.taotao.cloud.standalone.system.modules.data.tenant;

import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.standalone.common.constant.PreConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Classname PreTenantContextFilter
 * @Description 多租户上下文过滤器 -设置加载顺序最高获取租户
 * @Author shuigedeng
 * @since 2019-08-10 19:52
 * @Version 1.0
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PreTenantContextHolderFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 后面考虑存到redis
        String tenantId = request.getHeader(PreConstant.PRE_TENANT_KEY);
        //在没有提供tenantId的情况下返回默认的
        if (StrUtil.isNotBlank(tenantId)) {
            PreTenantContextHolder.setCurrentTenantId(Long.valueOf((tenantId)));
        } else {
            PreTenantContextHolder.setCurrentTenantId(1L);
        }
        filterChain.doFilter(request, response);
    }
}
