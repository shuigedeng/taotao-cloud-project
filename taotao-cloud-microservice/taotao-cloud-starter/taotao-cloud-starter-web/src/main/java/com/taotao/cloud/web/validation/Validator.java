package com.taotao.cloud.web.validation;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.utils.context.ContextUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;
import javax.validation.ConstraintViolation;

/**
 * <b>参数校验器</b>
 * <p>全面简单的校验框架，更适合国内校验场景，支持多种校验方式，配合全局异常处理，只为更好的RESTful
 *
 * @since 2019年6月25日
 */
public class Validator {

	private Object param;

	// 提示
	private static final String NOT_NULL_HINT_MSG = "参数 {} 必须不为 null";
	private static final String NOT_EMPTY_HINT_MSG = "参数 {} 必须不为empty(null 或 \"\")";
	private static final String ASSERT_TRUE_HINT_MSG = "参数 {} 必须为 true";
	private static final String ASSERT_FALSE_HINT_MSG = "参数 {} 必须为 false";
	private static final String DIGITS_HINT_MSG = "参数 {} 必须是一个数字，其值必须在 {} - {} 之间（包含）";
	private static final String MAX_HINT_MSG = "参数 {} 不能超过最大值：{}";
	private static final String MIN_HINT_MSG = "参数 {} 不能低于最小值：{}";
	private static final String LENGTH_HINT_MSG = "参数 {} 长度必须在 {} - {} 之间（包含）";
	private static final String CHINESE_HINT_MSG = "参数 {} 中文校验不通过";
	private static final String ENGLISH_HINT_MSG = "参数 {} 英文校验不通过";
	private static final String BIRTHDAY_HINT_MSG = "参数 {} 生日校验不通过";
	private static final String CELLPHONE_HINT_MSG = "参数 {} 不是一个合法的手机号码";
	private static final String EMAIL_HINT_MSG = "参数 {} 不是一个合法的邮箱格式";
	private static final String ID_CARD_HINT_MSG = "参数 {} 不是一个合法的身份证号码";
	private static final String PLATE_NUMBER_HINT_MSG = "参数 {} 不是一个合法的中国车牌号码";
	private static final String UUID_HINT_MSG = "参数 {} 不是一个合法的UUID";
	private static final String URL_HINT_MSG = "参数 {} 不是一个合法的URL";
	private static final String IPV4_HINT_MSG = "参数 {} 不是一个合法的IPV4地址";
	private static final String IPV6_HINT_MSG = "参数 {} 不是一个合法的IPV6地址";
	private static final String MAC_ADDRESS_HINT_MSG = "参数 {} 不是一个合法的MAC地址";
	private static final String CAR_DRIVING_LICENCE_HINT_MSG = "参数 {} 不是一个合法的驾驶证（仅限：中国驾驶证档案编号）";
	private static final String CAR_VIN_HINT_MSG = "参数 {} 不是一个合法的车架号";
	private static final String CREDIT_CODE_HINT_MSG = "参数 {} 不是一个合法的统一社会信用代码";
	private static final String ZIP_CODE_HINT_MSG = "参数 {} 不是一个合法的邮政编码（中国）";
	private static final String REGEX_HINT_MSG = "参数 {} 不满足正则表达式：{}";

	/**
	 * 切换校验对象
	 *
	 * @param param 校验对象
	 * @return Validator
	 */
	public Validator param(Object param) {
		this.param = param;
		return this;
	}

	/**
	 * 获得参数校验器并设置校验对象
	 *
	 * @param param 校验对象
	 * @return Validator
	 */
	public static Validator getValidatorAndSetParam(Object param) {
		return ContextUtil.getBean(Validator.class, true).param(param);
	}

	/**
	 * 必须不为 {@code null}
	 *
	 * @param paramName 参数名
	 * @return Validator
	 */
	public Validator notNull(String paramName) {
		cn.hutool.core.lang.Validator.validateNotNull(param,
			StrUtil.format(NOT_NULL_HINT_MSG, paramName));
		return this;
	}

	/**
	 * 必须不为empty(null 或 "")
	 *
	 * @param paramName 参数名
	 * @return Validator
	 */
	public Validator notEmpty(String paramName) {
		cn.hutool.core.lang.Validator.validateNotEmpty(param,
			StrUtil.format(NOT_EMPTY_HINT_MSG, paramName));
		return this;
	}

	/**
	 * 必须为 true
	 *
	 * @param paramName 参数名
	 * @return Validator
	 */
	public Validator assertTrue(String paramName) {
		cn.hutool.core.lang.Validator.validateTrue((boolean) param,
			StrUtil.format(ASSERT_TRUE_HINT_MSG, paramName));
		return this;
	}

	/**
	 * 必须为 false
	 *
	 * @param paramName 参数名
	 * @return Validator
	 */
	public Validator assertFalse(String paramName) {
		cn.hutool.core.lang.Validator.validateFalse((boolean) param,
			StrUtil.format(ASSERT_FALSE_HINT_MSG, paramName));
		return this;
	}

	/**
	 * 必须是一个数字，其值必须在可接受的范围内（包含）
	 *
	 * @param min       最小值
	 * @param max       最大值
	 * @param paramName 参数名
	 * @return Validator
	 */
	public Validator digits(Number min, Number max, String paramName) {
		cn.hutool.core.lang.Validator.validateBetween((Number) param, min, max,
			StrUtil.format(DIGITS_HINT_MSG, paramName, min, max));
		return this;
	}

	/**
	 * 最大值校验
	 *
	 * @param max       最大值
	 * @param paramName 参数名
	 * @return Validator
	 */
	public Validator max(Number max, String paramName) {
		BigDecimal bigNum1 = NumberUtil.toBigDecimal((Number) param);
		BigDecimal bigNum2 = NumberUtil.toBigDecimal(max);

		if (!NumberUtil.isLessOrEqual(bigNum1, bigNum2)) {
			throw new ValidateException(StrUtil.format(MAX_HINT_MSG, paramName, max));
		}
		return this;
	}

	/**
	 * 最小值校验
	 *
	 * @param min       最小值
	 * @param paramName 参数名
	 * @return Validator
	 */
	public Validator min(Number min, String paramName) {
		BigDecimal bigNum1 = NumberUtil.toBigDecimal((Number) param);
		BigDecimal bigNum2 = NumberUtil.toBigDecimal(min);

		if (!NumberUtil.isGreaterOrEqual(bigNum1, bigNum2)) {
			throw new ValidateException(StrUtil.format(MIN_HINT_MSG, paramName, min));
		}
		return this;
	}

	/**
	 * 长度校验
	 *
	 * @param min       最小长度
	 * @param max       最大长度
	 * @param paramName 参数名
	 * @return Validator
	 */
	public Validator length(int min, int max, String paramName) {
		int length = ObjectUtil.length(param);
		if (!(length >= min && length <= max)) {
			throw new ValidateException(StrUtil.format(LENGTH_HINT_MSG, paramName, min, max));
		}

		return this;
	}

	/**
	 * 中文校验
	 *
	 * @param paramName 参数名
	 * @return Validator
	 */
	public Validator chinese(String paramName) {
		cn.hutool.core.lang.Validator.validateChinese((CharSequence) param,
			StrUtil.format(CHINESE_HINT_MSG, paramName));
		return this;
	}

	/**
	 * 英文校验
	 *
	 * @param paramName 参数名
	 * @return Validator
	 */
	public Validator english(String paramName) {
		cn.hutool.core.lang.Validator.validateWord((CharSequence) param,
			StrUtil.format(ENGLISH_HINT_MSG, paramName));
		return this;
	}

	/**
	 * 生日校验
	 *
	 * @param paramName 参数名
	 * @return Validator
	 */
	public Validator birthday(String paramName) {
		String date = null;
		if (param instanceof String) {
			date = (String) param;
		} else if (param instanceof Date) {
			date = DateUtil.formatDate((Date) param);
		} else if (param instanceof LocalDate || param instanceof LocalDateTime) {
			date = DateUtil.formatLocalDateTime((LocalDateTime) param);
		} else {
			throw new BusinessException(StrUtil.format("参数 {} 未知类型，不支持生日校验", paramName));
		}

		cn.hutool.core.lang.Validator.validateBirthday(date,
			StrUtil.format(BIRTHDAY_HINT_MSG, paramName));
		return this;
	}

	/**
	 * 手机号校验
	 *
	 * @param paramName 参数名
	 * @return Validator
	 */
	public Validator cellphone(String paramName) {
		cn.hutool.core.lang.Validator.validateMobile((CharSequence) param,
			StrUtil.format(CELLPHONE_HINT_MSG, paramName));
		return this;
	}

	/**
	 * 邮箱校验
	 *
	 * @param paramName 参数名
	 * @return Validator
	 */
	public Validator email(String paramName) {
		cn.hutool.core.lang.Validator.validateEmail((CharSequence) param,
			StrUtil.format(EMAIL_HINT_MSG, paramName));
		return this;
	}

	/**
	 * 身份证校验
	 *
	 * @param paramName 参数名
	 * @return Validator
	 */
	public Validator idCard(String paramName) {
		cn.hutool.core.lang.Validator.validateCitizenIdNumber((CharSequence) param,
			StrUtil.format(ID_CARD_HINT_MSG, paramName));
		return this;
	}

	/**
	 * 中国车牌号校验
	 *
	 * @param paramName 参数名
	 * @return Validator
	 */
	public Validator plateNumber(String paramName) {
		cn.hutool.core.lang.Validator.validatePlateNumber((CharSequence) param,
			StrUtil.format(PLATE_NUMBER_HINT_MSG, paramName));
		return this;
	}

	/**
	 * UUID校验
	 *
	 * @param paramName 参数名
	 * @return Validator
	 */
	public Validator uuid(String paramName) {
		cn.hutool.core.lang.Validator.validateUUID((CharSequence) param,
			StrUtil.format(UUID_HINT_MSG, paramName));
		return this;
	}

	/**
	 * URL校验
	 *
	 * @param paramName 参数名
	 * @return Validator
	 */
	public Validator url(String paramName) {
		cn.hutool.core.lang.Validator.validateUrl((CharSequence) param,
			StrUtil.format(URL_HINT_MSG, paramName));
		return this;
	}

	/**
	 * IPV4地址校验
	 *
	 * @param paramName 参数名
	 * @return Validator
	 */
	public Validator ipv4(String paramName) {
		cn.hutool.core.lang.Validator.validateIpv4((CharSequence) param,
			StrUtil.format(IPV4_HINT_MSG, paramName));
		return this;
	}

	/**
	 * IPV6地址校验
	 *
	 * @param paramName 参数名
	 * @return Validator
	 */
	public Validator ipv6(String paramName) {
		cn.hutool.core.lang.Validator.validateIpv6((CharSequence) param,
			StrUtil.format(IPV6_HINT_MSG, paramName));
		return this;
	}

	/**
	 * MAC地址校验
	 *
	 * @param paramName 参数名
	 * @return Validator
	 */
	public Validator macAddress(String paramName) {
		cn.hutool.core.lang.Validator.validateMac((CharSequence) param,
			StrUtil.format(MAC_ADDRESS_HINT_MSG, paramName));
		return this;
	}

	/**
	 * 验证是否为驾驶证 别名：驾驶证档案编号、行驶证编号
	 *
	 * @param paramName 参数名
	 * @return Validator
	 */
	public Validator carDrivingLicence(String paramName) {
		cn.hutool.core.lang.Validator.validateCarDrivingLicence((CharSequence) param,
			StrUtil.format(CAR_DRIVING_LICENCE_HINT_MSG, paramName));
		return this;
	}

	/**
	 * 验证是否为车架号；别名：行驶证编号 车辆识别代号 车辆识别码
	 *
	 * @param paramName 参数名
	 * @return Validator
	 */
	public Validator carVin(String paramName) {
		cn.hutool.core.lang.Validator.validateCarVin((CharSequence) param,
			StrUtil.format(CAR_VIN_HINT_MSG, paramName));
		return this;
	}

	/**
	 * 是否是有效的统一社会信用代码
	 * <pre>
	 * 第一部分：登记管理部门代码1位 (数字或大写英文字母)
	 * 第二部分：机构类别代码1位 (数字或大写英文字母)
	 * 第三部分：登记管理机关行政区划码6位 (数字)
	 * 第四部分：主体标识码（组织机构代码）9位 (数字或大写英文字母)
	 * 第五部分：校验码1位 (数字或大写英文字母)
	 * </pre>
	 *
	 * @param paramName 参数名
	 * @return Validator
	 */
	public Validator creditCode(String paramName) {
		if (cn.hutool.core.lang.Validator.isCreditCode((CharSequence) param) == false) {
			throw new ValidateException(CREDIT_CODE_HINT_MSG, paramName);
		}

		return this;
	}

	/**
	 * 验证是否为邮政编码（中国）
	 *
	 * @param paramName 参数名
	 * @return Validator
	 */
	public Validator zipCode(String paramName) {
		cn.hutool.core.lang.Validator.validateZipCode((CharSequence) param,
			StrUtil.format(ZIP_CODE_HINT_MSG, paramName));
		return this;
	}

	/**
	 * 正则校验
	 *
	 * @param regex     正则表达式
	 * @param paramName 参数名
	 * @return Validator
	 */
	public Validator regex(String regex, String paramName) {
		cn.hutool.core.lang.Validator.validateMatchRegex(regex, (CharSequence) param,
			StrUtil.format(REGEX_HINT_MSG, paramName, regex));
		return this;
	}

	/**
	 * POJO对象校验（通过注解）
	 *
	 * @param param  校验对象
	 * @param groups 用于验证的组或组列表(默认为Default)
	 * @return Validator
	 */
	public Validator valid(Object param, Class<?>... groups) {
		Set<ConstraintViolation<Object>> violations = ContextUtil.getBean(
			javax.validation.Validator.class, true).validate(param, groups);
		if (violations.size() > 0) {
			LogUtil.warn("{} violations.", violations.size());
			Console.log("校验对象：{}", param);
			JSONArray errorHints = new JSONArray();
			violations.forEach(violation -> {
				String errorkey = violation.getPropertyPath().toString();
				Object errorValue = violation.getInvalidValue();
				String errorHintMsg = violation.getMessage();
				JSONObject errorHint = new JSONObject(true);
				errorHint.put("errorkey", errorkey);
				errorHint.put("errorValue", errorValue);
				errorHint.put("errorHintMsg", errorHintMsg);
				errorHints.add(errorHint);
				LogUtil.error(errorHint.toString(SerializerFeature.WriteMapNullValue));
			});

			throw new ValidateException(errorHints.toString(SerializerFeature.WriteMapNullValue));
		}

		return this;
	}

}
