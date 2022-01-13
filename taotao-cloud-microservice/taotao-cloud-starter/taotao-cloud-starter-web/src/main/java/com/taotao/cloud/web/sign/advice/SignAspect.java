package com.taotao.cloud.web.sign.advice;

import com.alibaba.fastjson.JSON;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.web.sign.exception.SignDtguaiException;
import com.taotao.cloud.web.sign.properties.SignProperties;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import org.apache.commons.codec.digest.DigestUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;


/**
 * 系统日志，切面处理类
 *
 * @since 2021年3月12日13:41:04
 */
@Aspect
@Order(1)
public class SignAspect {

	private final SignProperties signProperties;

	public SignAspect(SignProperties signProperties) {
		this.signProperties = signProperties;
	}

	@Pointcut("@annotation(com.dtguai.encrypt.annotation.Sign)")
	public void signPointCut() {
		//自定义切入点
	}

	public static final String TOKEN_HEADER = "token";
	public static final String SIGN_HEADER = "sign";
	public static final String DATA_SECRET_HEADER = "dataSecret";

	@Around("signPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {

		//请求的参数
		Object[] args = point.getArgs();

		TreeMap<String, Object> reqm = Optional.ofNullable(args[0])
			.map(x -> JSON.toJSONStringWithDateFormat(x, "yyyy-MM-dd HH:mm:ss"))
			.map(x -> JSON.<TreeMap<String, Object>>parseObject(x, TreeMap.class))
			.orElseThrow(() -> new SignDtguaiException("sing注解中加密数据为空"));

		String timestamp = Optional.ofNullable(reqm.get("timestamp"))
			.map(Object::toString)
			.orElseThrow(() -> new SignDtguaiException("数字证书timestamp不能为空"));

		LogUtil.info("sign的TreeMap默认key升序排序timestamp:{} ---- json:{}", timestamp,
			JSON.toJSONString(reqm));

		Optional.of(reqm)
			.ifPresent(this::validSign);
		//执行方法
		return point.proceed();
	}

	/**
	 * 验证数字证书
	 *
	 * @param reqm 数据map
	 */
	private void validSign(Map<String, Object> reqm) {
		String md5Sign;
		String sign;
		StringBuilder paramBuilder = new StringBuilder();
		try {
			reqm = Optional.ofNullable(reqm)
				.orElseThrow(() -> new SignDtguaiException(SIGN_HEADER + "的map不能为空"));
			sign = Optional.ofNullable(reqm)
				.map(x -> x.get(SIGN_HEADER))
				.map(Object::toString)
				.orElseThrow(() -> new SignDtguaiException(SIGN_HEADER + "不能为空"));

			// 校验 Sign
			reqm.forEach((k, v) -> {
				List<String> ignore = signProperties.getIgnore();
				if (v != null && !ignore.contains(k)) {
					paramBuilder.append(k).append("=").append(v).append("&");
				}
			});
			String dataSing = paramBuilder.append("signKey=").append(signProperties.getKey())
				.toString();
			LogUtil.info("sing之前的拼装数据:{}", dataSing);
			md5Sign = DigestUtils.md5Hex(dataSing);
		} catch (Exception e) {
			LogUtil.error("sign数据签名校验出错{}", reqm, e);
			throw new SignDtguaiException(SIGN_HEADER + "数据签名校验出错");
		}
		if (!md5Sign.equals(sign)) {
			LogUtil.error("验证失败:{}  传入的sign:{}  当前生成的md5Sign:{}", paramBuilder, sign, md5Sign);
			throw new SignDtguaiException("数字证书校验失败");
		}

	}


}
