package com.taotao.cloud.sa.just.biz.just.justauth.service;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.sa.just.biz.just.justauth.entity.JustAuthSocial;
import com.taotao.cloud.sa.just.biz.just.justauth.entity.JustAuthSocialUser;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @date 2022/6/18
 */
public interface IJustAuthService {

	/**
	 * 查询第三方用户绑定关系
	 */
	Long userBindId(@NotBlank @RequestParam("uuid") String uuid,
		@NotBlank @RequestParam("source") String source);

	/**
	 * 创建或更新第三方用户信息
	 */
	Long userCreateOrUpdate(@NotNull @RequestBody JustAuthSocialInfoDTO justAuthSocialInfoDTO);

	/**
	 * 查询绑定第三方用户信息
	 */
	Result<Object> userBindQuery(@NotNull @RequestParam("socialId") Long socialId);

	/**
	 * 查询第三方用户信息
	 *
	 * @param socialId
	 * @return
	 */
	JustAuthSocial querySocialInfo(@NotNull @RequestParam("socialId") Long socialId);

	/**
	 * 绑定第三方用户信息
	 */
	JustAuthSocialUser userBind(@NotNull @RequestParam("socialId") Long socialId,
		@NotNull @RequestParam("userId") Long userId);

	/**
	 * 解绑第三方用户信息"
	 */
	Result<JustAuthSocialUser> userUnbind(@NotNull @RequestParam("socialId") Long socialId,
		@NotNull @RequestParam("userId") Long userId);

}
