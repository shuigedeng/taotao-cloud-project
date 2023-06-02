/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * <http://www.apache.org/licenses/LICENSE-2.0>
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 <https://gitee.com/herodotus/dante-engine>
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 <https://gitee.com/herodotus/dante-engine>
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.dante.core.utils;

import cn.herodotus.engine.oauth2.core.definition.domain.HerodotusUser;
import org.dromara.hutoolcore.bean.BeanUtil;
import org.dromara.hutoolcore.bean.copier.CopyOptions;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * @author gengwei.zheng
 * @date 2018-3-8
 **/
public class SecurityUtils {

    private static final Logger log = LoggerFactory.getLogger(SecurityUtils.class);

    private static final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public static final String PREFIX_ROLE = "ROLE_";
    public static final String PREFIX_SCOPE = "SCOPE_";

    /**
     * 密码加密
     *
     * @param password 明文密码
     * @return 已加密密码
     */
    public static String encrypt(String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * 密码验证
     *
     * @param rawPassword     原始密码
     * @param encodedPassword 加密后的密码
     * @return 密码是否匹配
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public static SecurityContext getSecurityContext() {
        return SecurityContextHolder.getContext();
    }

    public static Authentication getAuthentication() {
        return getSecurityContext().getAuthentication();
    }

    public static boolean isAuthenticated() {
        return ObjectUtils.isNotEmpty(getAuthentication()) && getAuthentication().isAuthenticated();
    }

    public static Object getDetails() {
        return getAuthentication().getDetails();
    }

    /**
     * 当用户角色发生变化，或者用户角色对应的权限发生变化，那么就从数据库中重新查询用户相关信息
     *
     * @param newHerodotusUser 从数据库中重新查询并生成的用户信息
     */
    public static void reloadAuthority(HerodotusUser newHerodotusUser) {
        // 重新new一个token，因为Authentication中的权限是不可变的.
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                newHerodotusUser, newHerodotusUser.getPassword(),
                newHerodotusUser.getAuthorities());
        token.setDetails(getDetails());
        getSecurityContext().setAuthentication(token);
    }

    /**
     * 获取认证用户信息
     * <p>
     * 该方法仅能获取有限用户信息。从实用角度建议使用本系统提供的其它获取用户方式。
     *
     * @return 自定义 UserDetails {@link HerodotusUser}
     */
    public static HerodotusUser getPrincipal() {
        if (isAuthenticated()) {
            Authentication authentication = getAuthentication();
            if (authentication.getPrincipal() instanceof OAuth2IntrospectionAuthenticatedPrincipal introspectionPrincipal) {
                return new HerodotusUser(null, introspectionPrincipal.getUsername(), null, introspectionPrincipal.getAuthorities());
            }
            if (authentication.getPrincipal() instanceof HerodotusUser) {
                return (HerodotusUser) authentication.getPrincipal();
            }
            if (authentication.getPrincipal() instanceof Map) {
                Map<String, Object> principal = (Map<String, Object>) authentication.getPrincipal();
                return BeanUtil.mapToBean(principal, HerodotusUser.class, true, new CopyOptions());
            }
        }

        return null;
    }

    public static String getUsername() {
        HerodotusUser user = getPrincipal();
        if (user != null) {
            return user.getUsername();
        }
        return null;
    }

    public static HerodotusUser getPrincipals() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal != null) {
            if (principal instanceof HerodotusUser) {
                return (HerodotusUser) principal;
            } else if (principal instanceof LinkedHashMap) {
                // TODO: zhangyu 2019/7/15 感觉还可以升级一把，不吐linkedhashmap 直接就是oauth2user
                // 2019/7/20 试验过将OAuth2UserAuthenticationConverter map<string,?>中的?强制转换成oauth2user，试验失败，问题不是很急，可以先放着
                /**
                 * https://blog.csdn.net/m0_37834471/article/details/81814233
                 * cn/itcraftsman/luban/auth/oauth2/OAuth2UserAuthenticationConverter.java
                 */
//                HerodotusUser user = new HerodotusUser();
//                BeanUtil.fillBeanWithMap((LinkedHashMap) principal, user, true);
                return null;
            } else if (principal instanceof String && principal.equals("anonymousUser")) {
                return null;
            } else {
                throw new IllegalStateException("获取用户数据失败");
            }
        }
        return null;
    }

    public static String getUserId() {
        HerodotusUser herodotusUser = getPrincipal();
        if (ObjectUtils.isNotEmpty(herodotusUser)) {
            return herodotusUser.getUserId();
        }

        return null;
    }


    public static String[] whitelistToAntMatchers(List<String> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            String[] array = new String[list.size()];
            log.debug("[Herodotus] |- Fetch The REST White List.");
            return list.toArray(array);
        }

        log.warn("[Herodotus] |- Can not Fetch The REST White List Configurations.");
        return new String[]{};
    }

    public static String wellFormRolePrefix(String content) {
        return wellFormPrefix(content, PREFIX_ROLE);
    }

    public static String wellFormPrefix(String content, String prefix) {
        if (StringUtils.startsWith(content, prefix)) {
            return content;
        } else {
            return prefix + content;
        }
    }

}
