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

package com.taotao.cloud.stock.biz.interfaces.facade;

import java.util.List;
import com.taotao.boot.common.utils.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 身份验证应用服务实现类
 *
 * @author shuigedeng
 * @since 2021-05-10
 */
@DubboService
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private CaptchaRepository captchaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PermissionQueryService permissionQueryService;

    @Override
    public boolean validateCaptcha(String uuid, String captchaCode) {
        if (StringUtils.isBlank(uuid) || StringUtils.isBlank(captchaCode)) {
            return false;
        }
        CaptchaValidateService captchaValidateService = new CaptchaValidateService(captchaRepository);
        return captchaValidateService.validate(new Uuid(uuid), new CaptchaCode(captchaCode));
    }

    @Override
    public AuthenticationDTO loginByUserName(String userName) {
        List<User> users = userRepository.find(new Mobile(userName));
        if (users == null || users.isEmpty()) {
            throw new RuntimeException("用户或密码不正确");
        }
        User user = users.get(0);
        AuthenticationDTO authenticationDTO = AuthenticationDTOAssembler.fromUser(user);
        authenticationDTO.setPermissionCodes(
                permissionQueryService.getPermissionCodes(user.getUserId().getId()));
        return authenticationDTO;
    }
}
