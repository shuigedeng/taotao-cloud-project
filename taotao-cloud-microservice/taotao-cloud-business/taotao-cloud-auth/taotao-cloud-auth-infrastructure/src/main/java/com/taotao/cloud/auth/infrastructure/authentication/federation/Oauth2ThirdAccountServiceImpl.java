package com.taotao.cloud.auth.infrastructure.authentication.federation;

import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 三方登录账户信息表 服务实现类
 * </p>
 */
//@Service
@RequiredArgsConstructor
//public class Oauth2ThirdAccountServiceImpl extends ServiceImpl<Oauth2ThirdAccountMapper, Oauth2ThirdAccount> implements IOauth2ThirdAccountService {
public class Oauth2ThirdAccountServiceImpl implements IOauth2ThirdAccountService {

	//private final IOauth2BasicUserService basicUserService;

	@Override
	public void checkAndSaveUser(Oauth2ThirdAccount thirdAccount) {
		// 构建三方唯一id和三方登录方式的查询条件
//		Oauth2ThirdAccount oauth2ThirdAccount = this.lambdaQuery().eq(Oauth2ThirdAccount::getType, thirdAccount.getType())
//			.eq(Oauth2ThirdAccount::getUniqueId, thirdAccount.getUniqueId()).one();
//		if (oauth2ThirdAccount == null) {
//			// 生成用户信息
//			Integer userId = basicUserService.saveByThirdAccount(thirdAccount);
//			thirdAccount.setUserId(userId);
//			// 不存在保存用户信息
//			this.save(thirdAccount);
//		} else {
//			// 校验是否需要生成基础用户信息
//			if (ObjectUtils.isEmpty(oauth2ThirdAccount.getUserId())) {
//				// 生成用户信息
//				Integer userId = basicUserService.saveByThirdAccount(thirdAccount);
//				oauth2ThirdAccount.setUserId(userId);
//			}
//			// 存在更新用户的认证信息
//			oauth2ThirdAccount.setCredentials(thirdAccount.getCredentials());
//			oauth2ThirdAccount.setCredentialsExpiresAt(thirdAccount.getCredentialsExpiresAt());
//			// 设置空， 让MybatisPlus自动填充
//			oauth2ThirdAccount.setUpdateTime(null);
//			this.updateById(oauth2ThirdAccount);
//		}
	}

}
