package com.taotao.cloud.stock.biz.application.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 注册应用服务实现类
 *
 * @author shuigedeng
 * @date 2021-06-23
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
        if (!captchaValidateService.validate(new Uuid(registerTenantCommand.getUuid()), new CaptchaCode(registerTenantCommand.getCaptcha()))) {
            throw new RuntimeException("验证码不正确");
        }
        TenantRegisterService tenantRegisterService = new TenantRegisterService(tenantRepository, roleRepository, permissionRepository, userRepository);
        tenantRegisterService.registerTenant(new TenantName(registerTenantCommand.getTenantName()), new TenantCode(registerTenantCommand.getTenantCode()), new Mobile(registerTenantCommand.getMobile()),
                Password.create(registerTenantCommand.getPassword()), new UserName(registerTenantCommand.getUserName()));
    }
}
