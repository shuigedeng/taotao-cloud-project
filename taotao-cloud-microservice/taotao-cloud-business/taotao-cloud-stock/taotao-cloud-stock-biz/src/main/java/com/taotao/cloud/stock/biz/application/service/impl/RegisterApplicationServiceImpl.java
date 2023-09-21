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

package com.taotao.cloud.stock.biz.application.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 注册应用服务实现类
 *
 * @author shuigedeng
 * @since 2021-06-23
 */
@Service
public class RegisterApplicationServiceImpl implements RegisterApplicationService {

    @Autowired
    private CaptchaRepository captchaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public void registerTenant(RegisterTenantCommand registerTenantCommand) {
        CaptchaValidateService captchaValidateService = new CaptchaValidateService(captchaRepository);
        if (!captchaValidateService.validate(
                new Uuid(registerTenantCommand.getUuid()), new CaptchaCode(registerTenantCommand.getCaptcha()))) {
            throw new RuntimeException("验证码不正确");
        }
        TenantRegisterService tenantRegisterService =
                new TenantRegisterService(tenantRepository, roleRepository, permissionRepository, userRepository);
        tenantRegisterService.registerTenant(
                new TenantName(registerTenantCommand.getTenantName()),
                new TenantCode(registerTenantCommand.getTenantCode()),
                new Mobile(registerTenantCommand.getMobile()),
                Password.create(registerTenantCommand.getPassword()),
                new UserName(registerTenantCommand.getUserName()));
    }
}
