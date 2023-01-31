package com.taotao.cloud.auth.biz.oneClick;

import com.aliyun.dypnsapi20170525.models.GetMobileResponse;
import com.aliyun.dypnsapi20170525.models.VerifyMobileResponse;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 开放API接口 服务类
 * <p>
 * app实现阿里云手机号一键登录最佳实践
 * <p>
 * https://juejin.cn/post/7081945118202134542
 * </p>
 *
 * @author: jacklin
 * @since: 2022/03/28 9:38
 **/
public interface IOpenAPIService {

	/**
	 * 调用GetMobile完成一键登录取号
	 *
	 * @param accessToken APP端SDK获取的登录token，必填
	 * @param outId       外部流水号，非必填
	 * @author: jacklin
	 * @since: 2021/4/17 9:41
	 **/
	GetMobileResponse getMobile(String accessToken, String outId, HttpServletRequest request);

	/**
	 * 调用verifyMobile完成本机号码校验认证
	 *
	 * @param accessCode  APP端SDK获取的登录token，必填
	 * @param phoneNumber 手机号，必填
	 * @param outId       外部流水号，非必填
	 * @author: jacklin
	 * @since: 2021/4/17 11:18
	 **/
	VerifyMobileResponse verifyMobile(String accessCode, String phoneNumber, String outId,
		HttpServletRequest request);

	/**
	 * 根据键名查询参数配置信息
	 *
	 * @param configKey 参数键名
	 * @return configValue  参数键值
	 * @author: jacklin
	 * @since: 2022/1/15 15:43
	 **/
	String selectConfigValueByKey(String configKey, HttpServletRequest request);

}
