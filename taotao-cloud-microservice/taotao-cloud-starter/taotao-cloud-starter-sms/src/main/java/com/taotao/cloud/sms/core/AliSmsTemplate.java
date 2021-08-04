package com.taotao.cloud.sms.core;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.sms.props.AliSmsProperties;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Random;
import org.springframework.lang.NonNull;

public class AliSmsTemplate implements SmsTemplate {

	private final AliSmsProperties aliSmsProperties;

	public AliSmsTemplate(AliSmsProperties aliSmsProperties) {
		this.aliSmsProperties = aliSmsProperties;
	}

	/**
	 * 获取默认客户端
	 *
	 * @return
	 */
	public IAcsClient getDefaultAcsClient() {
		DefaultProfile profile = DefaultProfile
			.getProfile(aliSmsProperties.getRegionId(), aliSmsProperties.getAccessKey(),
				aliSmsProperties.getSecretKey());
		DefaultProfile.addEndpoint(aliSmsProperties.getRegionId(),
			aliSmsProperties.getProduct(), aliSmsProperties.getDomain());
		//可自助调整超时时间
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");
		return new DefaultAcsClient(profile);
	}

	/**
	 * 封装公共的request
	 *
	 * @return
	 */
	private CommonRequest request() {
		CommonRequest request = new CommonRequest();
		request.setSysMethod(MethodType.POST);
		request.setSysDomain(aliSmsProperties.getDomain());
		request.setSysVersion(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		request.setSysAction("SendSms");
		request.putQueryParameter("RegionId", aliSmsProperties.getRegionId());
		return request;
	}

	@Override
	public boolean sendSms(@NonNull String phoneNumber, @NonNull String signName,
		@NonNull String templateCode, @NonNull String templateParam) {
		CommonRequest request = this.request();
		request.putQueryParameter("PhoneNumbers", phoneNumber);
		request.putQueryParameter("SignName", signName);
		request.putQueryParameter("TemplateCode", templateCode);
		request.putQueryParameter("TemplateParam", templateParam);
		try {
			CommonResponse response = this.getDefaultAcsClient().getCommonResponse(request);
			LogUtil.info(response.getData());
			return true;
		} catch (Exception e) {
			LogUtil.error("异常：{}", e);
		}
		return false;
	}

	@Override
	public String sendRandCode(int digits) {
		StringBuilder sBuilder = new StringBuilder();
		Random rd = new Random(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());
		for (int i = 0; i < digits; ++i) {
			sBuilder.append(rd.nextInt(9));
		}
		return sBuilder.toString();
	}
}
