/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
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
 * 4.分发源码时候，请注明软件出处
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.management.processor;

import com.taotao.cloud.security.springsecurity.core.definition.domain.AccessPrincipal;
import com.taotao.cloud.security.springsecurity.core.definition.domain.HerodotusUser;
import com.taotao.cloud.security.springsecurity.core.definition.service.EnhanceUserDetailsService;
import com.taotao.cloud.security.springsecurity.core.definition.strategy.StrategyUserDetailsService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * <p>Description: UserDetailsService核心类 </p>
 * <p>
 * 之前一直使用Fegin进行UserDetailsService的远程调用。现在直接改为数据库访问。主要原因是：
 * 1. 根据目前的设计，Oauth的表与系统权限相关的表是在一个库中的。因此UAA和UPMS分开是为了以后提高性能考虑，逻辑上没有必要分成两个服务。
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


//    @Override
//    public HerodotusUser loadUserByUsername(String username) throws UsernameNotFoundException {
//        HerodotusUser HerodotusUser = strategyUserDetailsService.findUserDetailsByUsername(username);
//       log.info("[Herodotus] |- UserDetailsService loaded user : [{}]", username);
//        return HerodotusUser;
//    }

    @Override
    public UserDetails loadUserBySocial(String source, AccessPrincipal accessPrincipal) throws UsernameNotFoundException {
        HerodotusUser HerodotusUser = strategyUserDetailsService.findUserDetailsBySocial(StringUtils.toRootUpperCase(source), accessPrincipal);
       log.info("[Herodotus] |- UserDetailsService loaded social user : [{}]", HerodotusUser.getUsername());
        return HerodotusUser;
    }

    @Override
    public HerodotusUser loadHerodotusUserByUsername(String username) throws UsernameNotFoundException {
        HerodotusUser HerodotusUser = strategyUserDetailsService.findUserDetailsByUsername(username);
       log.info("[Herodotus] |- UserDetailsService loaded user : [{}]", username);
        return HerodotusUser;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return loadHerodotusUserByUsername(username);
    }
}
