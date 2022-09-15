package com.taotao.cloud.stock.biz.interfaces.facade;

import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 身份验证应用服务实现类
 *
 * @author shuigedeng
 * @date 2021-05-10
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
        authenticationDTO.setPermissionCodes(permissionQueryService.getPermissionCodes(user.getUserId().getId()));
        return authenticationDTO;
    }

}
