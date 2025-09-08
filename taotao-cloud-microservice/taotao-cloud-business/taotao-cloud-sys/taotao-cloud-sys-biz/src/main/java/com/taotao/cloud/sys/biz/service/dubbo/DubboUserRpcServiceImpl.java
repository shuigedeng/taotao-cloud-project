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

package com.taotao.cloud.sys.biz.service.dubbo;

import com.taotao.cloud.sys.api.dubbo.UserRpcService;
import com.taotao.cloud.sys.biz.mapper.IUserMapper;
import com.taotao.cloud.sys.biz.model.entity.system.User;
import com.taotao.cloud.sys.biz.repository.cls.UserRepository;
import com.taotao.cloud.sys.biz.repository.inf.IUserRepository;
import com.taotao.boot.webagg.service.impl.BaseSuperServiceImpl;
import lombok.*;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

/**
 * UserServiceImpl
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 20:50:41
 */
@Service
@AllArgsConstructor
@DubboService(interfaceClass = UserRpcService.class, validation = "true")
public class DubboUserRpcServiceImpl
        implements UserRpcService {


    private static final String DEFAULT_PASSWORD = "123456";
    private static final String DEFAULT_USERNAME = "admin";

//    private final IUserRelationService userRoleService;
}
