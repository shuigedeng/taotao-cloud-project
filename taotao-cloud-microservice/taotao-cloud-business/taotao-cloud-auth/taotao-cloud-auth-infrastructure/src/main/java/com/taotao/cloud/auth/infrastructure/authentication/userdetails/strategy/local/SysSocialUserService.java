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

package com.taotao.cloud.auth.infrastructure.authentication.userdetails.strategy.local;

import com.taotao.cloud.auth.infrastructure.authentication.userdetails.strategy.user.SysSocialUser;
import org.springframework.stereotype.Service;

/**
 * <p>社会化登录用户服务 </p>
 *
 *
 * @since : 2021/5/16 16:29
 */
@Service
public class SysSocialUserService {

    //    private final SysSocialUserRepository sysSocialUserRepository;

    //    public SysSocialUserService(SysSocialUserRepository sysSocialUserRepository) {
    //        this.sysSocialUserRepository = sysSocialUserRepository;
    //    }

    public SysSocialUser findByUuidAndSource(String uuid, String source) {
        //        return sysSocialUserRepository.findSysSocialUserByUuidAndSource(uuid, source);
        return null;
    }

    public void saveAndFlush(SysSocialUser sysSocialUser) {}
}
