package com.taotao.cloud.auth.infrastructure.authentication.federation;

/**
 * <p>
 * 三方登录账户信息表 服务类
 * </p>
 *
 */
//public interface IOauth2ThirdAccountService extends IService<Oauth2ThirdAccount> {
public interface IOauth2ThirdAccountService  {

    /**
     * 检查是否存在该用户信息，不存在则保存，暂时不做关联基础用户信息，由前端引导完善/关联基础用户信息
     *
     * @param thirdAccount 用户信息
     */
    void checkAndSaveUser(Oauth2ThirdAccount thirdAccount);

}
