/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2020-2030 ZHENGGENGWEI<码匠君>. All rights reserved.
 *
 * - Author: ZHENGGENGWEI<码匠君>
 * - Contact: herodotus@aliyun.com
 * - Blog and source code availability: https://gitee.com/herodotus/herodotus-cloud
 */

package com.taotao.cloud.auth.biz.herodotus.server.authorization.processor;

import com.taotao.cloud.auth.biz.herodotus.core.definition.domain.HerodotusUser;
import com.taotao.cloud.auth.biz.herodotus.core.definition.service.AccessPrincipal;
import com.taotao.cloud.auth.biz.herodotus.core.definition.service.EnhanceUserDetailsService;
import com.taotao.cloud.auth.biz.herodotus.core.definition.strategy.StrategyUserDetailsService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * <p>Description: UserDetailsService核心类 </p>
 * <p>
 * 之前一直使用Fegin进行UserDetailsService的远程调用。现在直接改为数据库访问。主要原因是：
 * 1. 根据目前的设计，Oauth的表与系统权限相关的表是在一个库中的。因此UAA和UPMS分开是为了以后提高新能考虑，逻辑上没有必要分成两个服务。
 * 2. UserDetailsService 和 ClientDetailsService 是Oauth核心内容，调用频繁增加一道远程调用增加消耗而已。
 * 3. UserDetailsService 和 ClientDetailsService 是Oauth核心内容，只是UAA在使用。
 * 4. UserDetailsService 和 ClientDetailsService 是Oauth核心内容，是各种验证权限之前必须调用的内容。
 * 一方面：使用feign的方式调用，只能采取作为白名单的方式，安全性无法保证。
 * 另一方面：会产生调用的循环。
 * 因此，最终考虑把这两个服务相关的代码，抽取至UPMS API，采用UAA直接访问数据库的方式。
 *
 * @author : gengwei.zheng
 * @date : 2019/11/25 11:02
 */
public class HerodotusUserDetailsService implements EnhanceUserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(HerodotusUserDetailsService.class);

    private final StrategyUserDetailsService strategyUserDetailsService;

    public HerodotusUserDetailsService(StrategyUserDetailsService strategyUserDetailsService) {
        this.strategyUserDetailsService = strategyUserDetailsService;
    }

    @Override
    public HerodotusUser loadUserByUsername(String username) throws UsernameNotFoundException {
        HerodotusUser HerodotusUser = strategyUserDetailsService.findUserDetailsByUsername(username);
        log.debug("[Herodotus] |- UserDetailsService loaded user : [{}]", username);
        return HerodotusUser;
    }

    @Override
    public UserDetails loadUserBySocial(String source, AccessPrincipal accessPrincipal) throws UsernameNotFoundException {
        HerodotusUser HerodotusUser = strategyUserDetailsService.findUserDetailsBySocial(StringUtils.toRootUpperCase(source), accessPrincipal);
        log.debug("[Herodotus] |- UserDetailsService loaded social user : [{}]", HerodotusUser.getUsername());
        return HerodotusUser;
    }
}
