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

package com.taotao.cloud.auth.biz.demo.server.processor;

import cn.herodotus.engine.assistant.core.domain.AccessPrincipal;
import cn.herodotus.engine.oauth2.core.definition.domain.HerodotusUser;
import cn.herodotus.engine.oauth2.core.definition.service.EnhanceUserDetailsService;
import cn.herodotus.engine.oauth2.core.definition.strategy.StrategyUserDetailsService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Description: UserDetailsService核心类
 *
 * <p>之前一直使用Fegin进行UserDetailsService的远程调用。现在直接改为数据库访问。主要原因是： 1.
 * 根据目前的设计，Oauth的表与系统权限相关的表是在一个库中的。因此UAA和UPMS分开是为了以后提高性能考虑，逻辑上没有必要分成两个服务。 2. UserDetailsService 和
 * ClientDetailsService 是Oauth核心内容，调用频繁增加一道远程调用增加消耗而已。 3. UserDetailsService 和 ClientDetailsService
 * 是Oauth核心内容，只是UAA在使用。 4. UserDetailsService 和 ClientDetailsService 是Oauth核心内容，是各种验证权限之前必须调用的内容。
 * 一方面：使用feign的方式调用，只能采取作为白名单的方式，安全性无法保证。 另一方面：会产生调用的循环。 因此，最终考虑把这两个服务相关的代码，抽取至UPMS
 * API，采用UAA直接访问数据库的方式。
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

    //    @Override
    //    public HerodotusUser loadUserByUsername(String username) throws UsernameNotFoundException
    // {
    //        HerodotusUser HerodotusUser =
    // strategyUserDetailsService.findUserDetailsByUsername(username);
    //        log.debug("[Herodotus] |- UserDetailsService loaded user : [{}]", username);
    //        return HerodotusUser;
    //    }

    @Override
    public UserDetails loadUserBySocial(String source, AccessPrincipal accessPrincipal)
            throws UsernameNotFoundException {
        HerodotusUser HerodotusUser =
                strategyUserDetailsService.findUserDetailsBySocial(
                        StringUtils.toRootUpperCase(source), accessPrincipal);
        log.debug(
                "[Herodotus] |- UserDetailsService loaded social user : [{}]",
                HerodotusUser.getUsername());
        return HerodotusUser;
    }

    @Override
    public HerodotusUser loadHerodotusUserByUsername(String username)
            throws UsernameNotFoundException {
        HerodotusUser HerodotusUser =
                strategyUserDetailsService.findUserDetailsByUsername(username);
        log.debug("[Herodotus] |- UserDetailsService loaded user : [{}]", username);
        return HerodotusUser;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return loadHerodotusUserByUsername(username);
    }
}
