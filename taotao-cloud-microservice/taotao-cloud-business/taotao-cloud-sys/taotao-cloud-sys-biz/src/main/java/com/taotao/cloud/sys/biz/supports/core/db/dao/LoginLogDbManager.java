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

package com.taotao.cloud.sys.biz.supports.core.db.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Repository;

/**
 * 登录日志
 *
 * @author shuigedeng
 * @since 2021/8/12
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class LoginLogDbManager extends BaseManager<LoginLogDbMapper, LoginLogDb> {

    public IPage<LoginLogDb> page(LoginLogParam loginLogParam) {
        return lambdaQuery()
                .orderByDesc(LoginLogDb::getId)
                .like(
                        StrUtil.isNotBlank(loginLogParam.getAccount()),
                        LoginLogDb::getAccount,
                        loginLogParam.getAccount())
                .like(StrUtil.isNotBlank(loginLogParam.getClient()), LoginLogDb::getClient, loginLogParam.getClient())
                .like(
                        StrUtil.isNotBlank(loginLogParam.getLoginType()),
                        LoginLogDb::getLoginType,
                        loginLogParam.getLoginType())
                .page(MpUtils.buildMpPage(loginLogParam));
    }
}
