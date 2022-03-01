package com.taotao.cloud.sys.biz.tools.security.configs.access;

import com.taotao.cloud.sys.biz.tools.security.configs.UrlSecurityPermsLoad;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class CustomFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    @Autowired
    private UrlSecurityPermsLoad urlPermsLoad;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        String requestUrl = ((FilterInvocation) object).getRequestUrl();
        String matchRoles = urlPermsLoad.findMatchRoles(requestUrl);
        if (StringUtils.isBlank(matchRoles)){
            // 如果路径没有角色配置,则默认是可以被访问的, 只要登录了就行
            matchRoles = "authc";
        }
        final List<ConfigAttribute> list = SecurityConfig.createList(matchRoles);
        return list;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
