package com.taotao.cloud.member.biz.connect.util;

import com.taotao.cloud.cache.redis.repository.RedisRepository;
import com.taotao.cloud.common.enums.CachePrefix;
import com.taotao.cloud.common.enums.ClientTypeEnum;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.member.biz.connect.config.ApiProperties;
import com.taotao.cloud.member.biz.connect.config.AuthConfig;
import com.taotao.cloud.member.biz.connect.config.ConnectAuthEnum;
import com.taotao.cloud.member.biz.connect.config.DomainProperties;
import com.taotao.cloud.member.biz.connect.entity.dto.AuthCallback;
import com.taotao.cloud.member.biz.connect.entity.dto.AuthResponse;
import com.taotao.cloud.member.biz.connect.entity.dto.ConnectAuthUser;
import com.taotao.cloud.member.biz.connect.exception.AuthException;
import com.taotao.cloud.member.biz.connect.request.AuthRequest;
import com.taotao.cloud.member.biz.connect.request.BaseAuthQQRequest;
import com.taotao.cloud.member.biz.connect.request.BaseAuthWeChatPCRequest;
import com.taotao.cloud.member.biz.connect.request.BaseAuthWeChatRequest;
import com.taotao.cloud.member.biz.connect.service.ConnectService;
import com.taotao.cloud.member.biz.connect.token.Token;
import com.taotao.cloud.sys.api.enums.SettingCategoryEnum;
import com.taotao.cloud.sys.api.feign.IFeignSettingApi;
import com.taotao.cloud.sys.api.model.vo.setting.QQConnectSettingItemVO;
import com.taotao.cloud.sys.api.model.vo.setting.QQConnectSettingVO;
import com.taotao.cloud.sys.api.model.vo.setting.WechatConnectSettingItemVO;
import com.taotao.cloud.sys.api.model.vo.setting.WechatConnectSettingVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 联合登陆工具类
 */
@Component
public class ConnectUtil {

	@Autowired
	private RedisRepository redisRepository;
	@Autowired
	private ConnectService connectService;
	@Autowired
	private IFeignSettingApi settingService;
	@Autowired
	private ApiProperties apiProperties;
	@Autowired
	private DomainProperties domainProperties;


	static String prefix = "/buyer/passport/connect/connect/callback/";

	/**
	 * 回调地址获取
	 *
	 * @param connectAuthEnum 用户枚举
	 * @return 回调地址
	 */
	String getRedirectUri(ConnectAuthEnum connectAuthEnum) {
		return apiProperties.getBuyer() + prefix + connectAuthEnum.getName();
	}

	/**
	 * 登录回调
	 *
	 * @param type
	 * @param callback
	 * @param httpServletResponse
	 * @param httpServletRequest
	 * @throws IOException
	 */
	public void callback(String type, AuthCallback callback, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
		AuthRequest authRequest = this.getAuthRequest(type);
		AuthResponse<ConnectAuthUser> response = authRequest.login(callback);
		Result<Token> result;
		//联合登陆处理，如果响应正常，则录入响应结果到redis
		if (response.ok()) {
			ConnectAuthUser authUser = response.getData();
			Token token;
			try {
				token = connectService.unionLoginCallback(type, authUser, callback.getState());
				result = Result.success(token);
			} catch (Exception e) {
				throw new BusinessException(ResultEnum.ERROR.getCode(), e.getMessage());
			}
		}
		//否则录入响应结果，等待前端获取信息
		else {
			throw new BusinessException(ResultEnum.ERROR.getCode(), response.getMsg());
		}
		//缓存写入登录结果，300秒有效
		redisRepository.setExpire(CachePrefix.CONNECT_RESULT.getPrefix() + callback.getCode(), result, 300L);

		//跳转地址
		String url = this.check(httpServletRequest.getHeader("user-agent")) ?
			domainProperties.getWap() + "/pages/passport/login?state=" + callback.getCode() :
			domainProperties.getPc() + "/login?state=" + callback.getCode();

		try {
			httpServletResponse.sendRedirect(url);
		} catch (Exception e) {
			LogUtils.error("登录回调错误", e);
		}
	}

	/**
	 * 获取响应结果
	 *
	 * @param state
	 * @return
	 */
	public Result<Object> getResult(String state) {
		Object object = redisRepository.get(CachePrefix.CONNECT_RESULT.getPrefix() + state);
		if (object == null) {
			return null;
		} else {
			redisRepository.del(CachePrefix.CONNECT_RESULT.getPrefix() + state);
			return (Result<Object>) object;
		}
	}

	/**
	 * 联合登录
	 *
	 * @param type 枚举
	 * @return
	 */
	public AuthRequest getAuthRequest(String type) {
		ConnectAuthEnum authInterface = ConnectAuthEnum.valueOf(type);
		if (authInterface == null) {
			throw new BusinessException(ResultEnum.CONNECT_NOT_EXIST);
		}
		AuthRequest authRequest = null;
		switch (authInterface) {
			case WECHAT: {
				//寻找配置
				WechatConnectSettingVO wechatConnectSetting = settingService.getWechatConnectSetting(SettingCategoryEnum.WECHAT_CONNECT.name()).data();

				for (WechatConnectSettingItemVO wechatConnectSettingItem : wechatConnectSetting.getWechatConnectSettingItemVOS()) {
					if (wechatConnectSettingItem.getClientType().equals(ClientTypeEnum.H5.name())) {
						authRequest = new BaseAuthWeChatRequest(AuthConfig.builder()
							.clientId(wechatConnectSettingItem.getAppId())
							.clientSecret(wechatConnectSettingItem.getAppSecret())
							.redirectUri(getRedirectUri(authInterface))
							.build(), redisRepository);
					}
				}
				break;
			}
			case WECHAT_PC: {
				//寻找配置
				WechatConnectSettingVO wechatConnectSetting = settingService.getWechatConnectSetting(SettingCategoryEnum.WECHAT_CONNECT.name()).data();
				for (WechatConnectSettingItemVO wechatConnectSettingItem : wechatConnectSetting.getWechatConnectSettingItemVOS()) {
					if (wechatConnectSettingItem.getClientType().equals(ClientTypeEnum.PC.name())) {
						authRequest = new BaseAuthWeChatPCRequest(AuthConfig.builder()
							.clientId(wechatConnectSettingItem.getAppId())
							.clientSecret(wechatConnectSettingItem.getAppSecret())
							.redirectUri(getRedirectUri(authInterface))
							.build(), redisRepository);
					}
				}

				break;
			}
			case QQ:
				//寻找配置
				QQConnectSettingVO qqConnectSetting = settingService.getQQConnectSetting(SettingCategoryEnum.QQ_CONNECT.name()).data();
				for (QQConnectSettingItemVO qqConnectSettingItem : qqConnectSetting.getQqConnectSettingItemList()) {
					if (qqConnectSettingItem.getClientType().equals(ClientTypeEnum.PC.name())) {
						authRequest = new BaseAuthQQRequest(AuthConfig.builder()
							.clientId(qqConnectSettingItem.getAppId())
							.clientSecret(qqConnectSettingItem.getAppKey())
							.redirectUri(getRedirectUri(authInterface))
							//这里qq获取unionid 需要配置为true，详情可以查阅属性说明，内部有帮助文档
							.unionId(true)
							.build(), redisRepository);
					}
				}
				break;
			default:
				break;
		}
		if (null == authRequest) {
			throw new AuthException("暂不支持第三方登陆");
		}
		return authRequest;
	}

	/**
	 * \b 是单词边界(连着的两个(字母字符 与 非字母字符) 之间的逻辑上的间隔),
	 * 字符串在编译时会被转码一次,所以是 "\\b"
	 * \B 是单词内部逻辑间隔(连着的两个字母字符之间的逻辑上的间隔)
	 */
	static String phoneReg = "\\b(ip(hone|od)|android|opera m(ob|in)i"
		+ "|windows (phone|ce)|blackberry"
		+ "|s(ymbian|eries60|amsung)|p(laybook|alm|rofile/midp"
		+ "|laystation portable)|nokia|fennec|htc[-_]"
		+ "|mobile|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";
	static String tableReg = "\\b(ipad|tablet|(Nexus 7)|up.browser"
		+ "|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";

	/**
	 * 移动设备正则匹配：手机端、平板
	 */
	public static final Pattern phonePat = Pattern.compile(phoneReg, Pattern.CASE_INSENSITIVE);
	public static final Pattern tablePat = Pattern.compile(tableReg, Pattern.CASE_INSENSITIVE);

	/**
	 * 检测是否是移动设备访问
	 *
	 * @param userAgent 浏览器标识
	 * @return true:移动设备接入，false:pc端接入
	 */
	private boolean check(String userAgent) {
		if (null == userAgent) {
			userAgent = "";
		}
		//匹配
		Matcher matcherPhone = phonePat.matcher(userAgent);
		Matcher matcherTable = tablePat.matcher(userAgent);
		if (matcherPhone.find() || matcherTable.find()) {
			return true;
		} else {
			return false;
		}
	}
}

