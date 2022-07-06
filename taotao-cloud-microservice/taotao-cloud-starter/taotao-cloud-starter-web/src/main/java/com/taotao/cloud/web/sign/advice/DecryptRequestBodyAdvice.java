package com.taotao.cloud.web.sign.advice;


import com.alibaba.fastjson.JSON;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.web.sign.annotation.DecryptBody;
import com.taotao.cloud.web.sign.bean.DecryptAnnotationInfoBean;
import com.taotao.cloud.web.sign.bean.DecryptHttpInputMessage;
import com.taotao.cloud.web.sign.enums.DecryptBodyMethod;
import com.taotao.cloud.web.sign.exception.DecryptDtguaiException;
import com.taotao.cloud.web.sign.properties.EncryptBodyProperties;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

/**
 * 请求数据解密处理 本类只对控制器参数中含有<strong>{@link org.springframework.web.bind.annotation.RequestBody}</strong>
 * 以及package为<strong><code>.decrypt</code></strong>下的注解有效
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-06 14:46:56
 */
@Order(1)
@RestControllerAdvice(basePackages = {"com.taotao.cloud.*.biz.api.controller"})
public class DecryptRequestBodyAdvice implements RequestBodyAdvice {

	private final EncryptBodyProperties config;

	public DecryptRequestBodyAdvice(EncryptBodyProperties config) {
		this.config = config;
	}

	@Override
	public boolean supports(MethodParameter methodParameter, @NotNull Type targetType,
							@NotNull Class<? extends HttpMessageConverter<?>> converterType) {

		Annotation[] annotations = methodParameter.getDeclaringClass().getAnnotations();

		if (Arrays.stream(annotations).anyMatch(DecryptBody.class::isInstance)) {
			return true;
		}

		return Optional.of(methodParameter)
			.map(MethodParameter::getMethod)
			.map(x -> x.isAnnotationPresent(DecryptBody.class))
			.orElseThrow(() -> new DecryptDtguaiException("解密拦截器返回异常"));

	}

	@Override
	public Object handleEmptyBody(Object body, @NotNull HttpInputMessage inputMessage,
								  @NotNull MethodParameter parameter, @NotNull Type targetType,
								  @NotNull Class<? extends HttpMessageConverter<?>> converterType) {
		return body;
	}

	@NotNull
	@Override
	public HttpInputMessage beforeBodyRead(@NotNull HttpInputMessage inputMessage, @NotNull MethodParameter parameter,
										   @NotNull Type targetType, @NotNull Class<? extends HttpMessageConverter<?>> converterType) {

		if (Optional.of(inputMessage).map(x -> {
			try {
				return x.getBody();
			} catch (IOException e) {
				LogUtil.error("数据解密初始化异常,时间:{}", new Date());
				return null;
			}
		}).isEmpty()) {
			return inputMessage;
		}

		LinkedHashMap<String, Object> req;
		String body;
		String sign;
		try {
			body = IOUtils.toString((inputMessage.getBody()), config.getEncoding());
			req = JSON.<LinkedHashMap<String, Object>>parseObject(body, LinkedHashMap.class);

			body = (String) req.get("dataSecret");

			sign = (String) req.get("sign");

		} catch (Exception e) {
			LogUtil.error("无法获取请求正文数据，请检查发送数据体或请求方法是否符合规范", e);
			throw new DecryptDtguaiException("无法获取请求正文数据，请检查发送数据体或请求方法是否符合规范");
		}
		if (!StringUtils.hasText(body)) {
			LogUtil.error("请求参数dataSecret为null或为空字符串，因此解密失败body:{}", body);
			throw new DecryptDtguaiException("请求正文为NULL或为空字符串，因此解密失败");
		}

		String decryptBody = null;
		DecryptAnnotationInfoBean methodAnnotation = this.getMethodAnnotation(parameter);
		if (methodAnnotation != null) {
			decryptBody = switchDecrypt(body, methodAnnotation);
		} else {
			DecryptAnnotationInfoBean classAnnotation = this.getClassAnnotation(
				parameter.getDeclaringClass());
			if (classAnnotation != null) {
				decryptBody = switchDecrypt(body, classAnnotation);
			}
		}

		decryptBody = Optional.ofNullable(decryptBody).orElseThrow(() -> {
				LogUtil.error("decryptBody是null" + "当前类:{}", this.getClass().getName());
				return new DecryptDtguaiException("解密错误，请检查选择的源数据的加密方式是否正确");
			}
		);

		try {
			req = JSON.<LinkedHashMap<String, Object>>parseObject(decryptBody, LinkedHashMap.class);
			req.put("sign", sign);
			LogUtil.info("解密数据补充timestamp和sign的map:{}", JSON.toJSONString(req));
			InputStream inputStream = IOUtils.toInputStream(JSON.toJSONString(req),
				config.getEncoding());
			return new DecryptHttpInputMessage(inputStream, inputMessage.getHeaders());
		} catch (Exception e) {
			LogUtil.error("字符串转换成流格式异常，请检查编码等格式是否正确,decryptBody:{}", decryptBody);
			throw new DecryptDtguaiException("字符串转换成流格式异常，请检查编码等格式是否正确,decryptBody:" + decryptBody);
		}
	}

	@Override
	public Object afterBodyRead(Object body, HttpInputMessage inputMessage,
		MethodParameter parameter, Type targetType,
		Class<? extends HttpMessageConverter<?>> converterType) {
		return body;
	}

	/**
	 * 获取方法控制器上的加密注解信息
	 *
	 * @param methodParameter 控制器方法
	 * @return 加密注解信息
	 */
	private DecryptAnnotationInfoBean getMethodAnnotation(MethodParameter methodParameter) {

		boolean annotation = Optional.ofNullable(methodParameter)
			.map(MethodParameter::getMethod)
			.map(x -> x.isAnnotationPresent(DecryptBody.class))
			.orElse(false);

		if (annotation) {

			DecryptBody decryptBody = methodParameter.getMethodAnnotation(DecryptBody.class);

			return DecryptAnnotationInfoBean.builder()
				.decryptBodyMethod(Optional.ofNullable(decryptBody)
					.map(DecryptBody::value)
					.orElse(null))
				.key(Optional.ofNullable(decryptBody)
					.map(DecryptBody::otherKey)
					.orElse(null))
				.timeOut(Optional.ofNullable(decryptBody)
					.map(DecryptBody::timeOut)
					.orElse(0L))
				.build();
		}

		return null;
	}

	/**
	 * 获取类控制器上的加密注解信息
	 *
	 * @param clazz 控制器类
	 * @return 加密注解信息
	 */
	private DecryptAnnotationInfoBean getClassAnnotation(Class<?> clazz) {

		Annotation[] annotations = clazz.getDeclaredAnnotations();

		return Optional.of(annotations)
			.map(x -> {
					for (Annotation annotation : x) {
						if (annotation instanceof DecryptBody) {
							DecryptBody decryptBody = (DecryptBody) annotation;
							return DecryptAnnotationInfoBean.builder()
								.decryptBodyMethod(decryptBody.value())
								.key(decryptBody.otherKey())
								.build();
						}
					}
					return null;
				}
			)
			.orElse(null);
	}


	/**
	 * 选择加密方式并进行解密
	 *
	 * @param formatStringBody 目标解密字符串
	 * @param infoBean         加密信息
	 * @return 解密结果
	 */
	private String switchDecrypt(String formatStringBody, DecryptAnnotationInfoBean infoBean) {

		DecryptBodyMethod method = Optional.ofNullable(infoBean.getDecryptBodyMethod())
			.orElseThrow(() -> new DecryptDtguaiException("解密方式未定义"));

		String decodeData = method.getSecurity()
			.decrypt(formatStringBody, infoBean.getKey(), config);

		//验证数据是否过期timestamp
		verifyTime(decodeData, infoBean.getTimeOut());

		return decodeData;
	}

	/**
	 * 判断数据是否超时 不配置 不判断超时时间
	 *
	 * @param decodeData 解密后的数据
	 * @param timeOut    过期时间
	 */
	private void verifyTime(String decodeData, long timeOut) {
		if (0 != timeOut) {

			long timestamp = Optional.ofNullable(decodeData)
				.map(x -> JSON.parseObject(x, Map.class))
				.map(x -> x.get("timestamp"))
				.map(x -> Long.parseLong(x.toString()))
				.orElseThrow(() -> new DecryptDtguaiException("数据加密timestamp不能为空"));

			//当前时间
			long nowTime = System.currentTimeMillis();

			//判断数据加密时间 时间戳+过期时间如果小于当前时间踢飞
			if (timestamp + timeOut < nowTime) {
				LogUtil.error("时间戳:{},时间戳+过期时间:{},当前时间:{},时间差:{},数据为:{}"
					, timestamp
					, timestamp + timeOut
					, nowTime
					, timestamp + timeOut - nowTime
					, decodeData);
				throw new DecryptDtguaiException("解密数据过期,提交失败");
			}
		}
	}
}
