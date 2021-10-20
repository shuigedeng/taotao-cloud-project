package com.taotao.cloud.common.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

public class CommonUtil {

	/**
	 * 正则表达式：验证用户名
	 */
	public static final String REGEX_USERNAME = "^[a-zA-Z]\\w{5,20}$";

	/**
	 * 正则表达式：验证密码
	 */
	public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,20}$";

	/**
	 * 正则表达式：验证手机号
	 */
	// public static final String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
	// 粗验证
	public static final String REGEX_MOBILE = "^1\\d{10}$";
	/**
	 * 正则表达式：验证邮箱
	 */
	public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

	/**
	 * 正则表达式：验证汉字
	 */
	public static final String REGEX_CHINESE = "^[\u4e00-\u9fa5],*$";

	/**
	 * 正则表达式：验证身份证
	 */
	public static final String REGEX_ID_CARD = "(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}$)";

	/**
	 * 正则表达式：验证URL
	 */
	public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";

	/**
	 * 正则表达式：验证IP地址
	 */
	public static final String REGEX_IP_ADDR = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";

	/**
	 * 正则表达式：6-20位字母+数字组合
	 */
	public static final String REGEX_LOGIN_PASSWORD = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$";

	/**
	 * 正则表达式：验证日期
	 */
	public static final String REGEX_FORMAT_DATE = "^((\\d{2}(([02468][048])|([13579][26]))[\\-]((((0?[13578])|(1[02]))[\\-]{1}((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-]{1}((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-]{1}((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-]((((0?[13578])|(1[02]))[\\-]{1}((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-]((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-]((0?[1-9])|(1[0-9])|(2[0-8]))))))";

	/**
	 * 验证金额两位小数
	 */
	public static final String REGEX_FORMAT_MONEY = "^(0|([1-9][0-9]*)|(([0]\\.\\d{1,2}|[1-9][0-9]*\\.\\d{1,2})))$";

	/**
	 * 正则表达式：验证时间格式
	 */
	public static final String REGEX_FORMAT_TIME = "0[0-9]:[0-5][0-9]|1[0-9]:[0-5][0-9]|2[0-3]:[0-5][0-9]";
	/**
	 * 判断是否有特殊字符
	 */
	private static final Pattern REGEX = Pattern.compile(
		"[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t|[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]");


	/**
	 * 判断是否为数字
	 *
	 * @param str
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * 字符串转换为带%号包括的查询字段
	 *
	 * @param str
	 */
	public static String toLikeStr(String str) {
		StringBuilder strs = new StringBuilder("%");
		strs.append(str);
		strs.append("%");
		return strs.toString();
	}

	/**
	 * 字符串转换为左边带%号包括的查询字段
	 *
	 * @param str
	 */
	public static String toLikeStrLt(String str) {
		StringBuilder strs = new StringBuilder("%");
		strs.append(str);
		return strs.toString();
	}

	/**
	 * 字符串转换为右边带%号包括的查询字段
	 *
	 * @param str
	 */
	public static String toLikeStrRt(String str) {
		StringBuilder strs = new StringBuilder();
		strs.append(str);
		strs.append("%");
		return strs.toString();
	}

	/**
	 * 替换标记以正常显示
	 *
	 * @param input
	 */
	public static String replaceTag(String input) {
		if (!hasSpecialChars(input)) {
			return input;
		}
		StringBuilder filtered = new StringBuilder(input.length());
		char c;
		for (int i = 0; i <= input.length() - 1; i++) {
			c = input.charAt(i);
			switch (c) {
				case '<':
					filtered.append("&lt;");
					break;
				case '>':
					filtered.append("&gt;");
					break;
				case '"':
					filtered.append("&quot;");
					break;
				case '&':
					filtered.append("&amp;");
					break;
				default:
					filtered.append(c);
			}

		}
		return (filtered.toString());
	}

	/**
	 * 判断标记是否存在
	 *
	 * @param input
	 */
	public static boolean hasSpecialChars(String input) {
		boolean flag = false;
		if ((input != null) && (input.length() > 0)) {
			char c;
			for (int i = 0; i < input.length(); i++) {
				c = input.charAt(i);
				switch (c) {
					case '>':
						flag = true;
						break;
					case '<':
						flag = true;
						break;
					case '"':
						flag = true;
						break;
					case '&':
						flag = true;
						break;
				}
				if (flag) {
					break;
				}
			}
		}
		return flag;
	}

	/**
	 * 生成UUID
	 */
	public static String createUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replaceAll("-", "");
	}

	/**
	 * 判断字符串是否为空
	 *
	 * @param obj
	 */
	public static boolean isBlank(String obj) {
		if (obj == null) {
			return true;
		}
		if (obj.isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * 判断字符串是否为null
	 *
	 * @param obj
	 */
	public static boolean isNull(Object obj) {
		if (obj == null) {
			return true;
		}
		return false;
	}

	/**
	 * 判断对象是否为空
	 *
	 * @param objs
	 */
	public static boolean isEmpty(Object... objs) {
		if (objs == null) {
			return true;
		} else {
			Object[] var1 = objs;
			int var2 = objs.length;

			for (int var3 = 0; var3 < var2; ++var3) {
				Object obj = var1[var3];
				if (obj != null && !"".equals(obj)) {
					return false;
				}
			}

			return true;
		}
	}

	/**
	 * 判断对象是否不为空
	 *
	 * @param objs
	 */
	public static boolean isNotEmpty(Object... objs) {
		if (objs == null) {
			return false;
		} else {
			Object[] var1 = objs;
			int var2 = objs.length;

			for (int var3 = 0; var3 < var2; ++var3) {
				Object obj = var1[var3];
				if (obj == null || "".equals(obj)) {
					return false;
				}
			}

			return true;
		}
	}


	/**
	 * 判断字符串长度
	 *
	 * @param lens
	 * @param str
	 */
	public static boolean isLengthRight(Integer[] lens, String str) {
		if (str == null || lens.length != 2) {
			return true;
		} else {
			if (str.length() > lens[1] || str.length() < lens[0]) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * 校验用户名
	 *
	 * @param username 校验通过返回true，否则返回false
	 */
	public static boolean isUsername(String username) {
		return Pattern.matches(REGEX_USERNAME, username);
	}

	/**
	 * 校验密码
	 *
	 * @param password 校验通过返回true，否则返回false
	 */
	public static boolean isPassword(String password) {
		return Pattern.matches(REGEX_PASSWORD, password);
	}

	/**
	 * 校验手机号
	 *
	 * @param mobile 校验通过返回true，否则返回false
	 */
	public static boolean isMobile(String mobile) {
		return Pattern.matches(REGEX_MOBILE, mobile);
	}

	/**
	 * 校验邮箱
	 *
	 * @param email 校验通过返回true，否则返回false
	 */
	public static boolean isEmail(String email) {
		return Pattern.matches(REGEX_EMAIL, email);
	}

	/**
	 * 校验汉字
	 *
	 * @param chinese 校验通过返回true，否则返回false
	 */
	public static boolean isChinese(String chinese) {
		return Pattern.matches(REGEX_CHINESE, chinese);
	}

	/**
	 * 校验身份证
	 *
	 * @param idCard 校验通过返回true，否则返回false
	 */
	public static boolean isIDCard(String idCard) {
		return Pattern.matches(REGEX_ID_CARD, idCard);
	}

	/**
	 * 校验URL
	 *
	 * @param url 校验通过返回true，否则返回false
	 */
	public static boolean isUrl(String url) {
		return Pattern.matches(REGEX_URL, url);
	}

	/**
	 * 校验IP地址
	 *
	 * @param ipAddr
	 */
	public static boolean isIPAddr(String ipAddr) {
		return Pattern.matches(REGEX_IP_ADDR, ipAddr);
	}


	/**
	 * 校验IP地址
	 *
	 * @param time
	 */
	public static boolean isTime(String time) {
		return Pattern.matches(REGEX_FORMAT_TIME, time);
	}

	public static boolean isPasswordOk(String password) {
		return Pattern.matches(REGEX_LOGIN_PASSWORD, password);
	}

	/**
	 * 生成短信验证码
	 */
	public static String getRoundNum() {
		/**
		 * 产生随机的六位数

		 */
		Random rad = new Random();
		String result = rad.nextInt(1000000) + "";
		if (result.length() != 6) {
			return getRoundNum();
		}
		return result;
	}

	/**
	 * 判断当前日期是星期几<br>
	 * <br>
	 *
	 * @param pTime 修要判断的时间<br> dayForWeek 判断结果<br>
	 * @Exception 发生异常<br>
	 */
	public static int dayForWeek(Date pTime) throws Exception {
		Calendar c = Calendar.getInstance();
		c.setTime(pTime);
		int dayForWeek = 0;
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			dayForWeek = 7;
		} else {
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		}
		return dayForWeek;
	}


	/**
	 * 身份证号码校验
	 */
	public static boolean validatorIdCardNo(String id) {
		if (StringUtils.isBlank(id)) {
			return false;
		}
		// 定义判别用户身份证号的正则表达式（15位或者18位，最后一位可以为字母）
		//String regularExpression = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|" +
		//        "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";
		//身份证号校验18位，15位不考虑
		String regularExpression = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)";
		//假设18位身份证号码:41000119910101123X  410001 19910101 123X
		//               620509195012242788
		//^开头
		//[1-9] 第一位1-9中的一个      4
		//\\d{5} 五位数字           10001（前六位省市县地区）
		//(18|19|20)                19（现阶段可能取值范围18xx-20xx年）
		//\\d{2}                    91（年份）
		//((0[1-9])|(10|11|12))     01（月份）
		//(([0-2][1-9])|10|20|30|31)01（日期）
		//\\d{3} 三位数字            123（第十七位奇数代表男，偶数代表女）
		//[0-9Xx] 0123456789Xx其中的一个 X（第十八位为校验值）
		//$结尾

		//假设15位身份证号码:410001910101123  410001 910101 123
		//^开头
		//[1-9] 第一位1-9中的一个      4
		//\\d{5} 五位数字           10001（前六位省市县地区）
		//\\d{2}                    91（年份）
		//((0[1-9])|(10|11|12))     01（月份）
		//(([0-2][1-9])|10|20|30|31)01（日期）
		//\\d{3} 三位数字            123（第十五位奇数代表男，偶数代表女），15位身份证不含X
		//$结尾

		boolean matches = id.matches(regularExpression);
		// update by zhulei on 2019-01-25 for MeiTuan test
		//判断第18位校验值
		if (matches) {

			if (id.length() == 18) {
				try {
					char[] charArray = id.toCharArray();
					//前十七位加权因子
					int[] idCardWi = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
					//这是除以11后，可能产生的11位余数对应的验证码
					String[] idCardY = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
					int sum = 0;
					for (int i = 0; i < idCardWi.length; i++) {
						int current = Integer.parseInt(String.valueOf(charArray[i]));
						int count = current * idCardWi[i];
						sum += count;
					}
					char idCardLast = charArray[17];
					int idCardMod = sum % 11;
					if (idCardY[idCardMod].toUpperCase()
						.equals(String.valueOf(idCardLast).toUpperCase())) {
						return true;
					} else {
						//System.out.println("身份证最后一位:" + String.valueOf(idCardLast).toUpperCase() + "错误,正确的应该是:" + idCardY[idCardMod].toUpperCase());
						return false;
					}

				} catch (Exception e) {
					e.printStackTrace();
					// System.out.println("异常:" + id);
					return false;
				}
			}

		}

		return matches;
	}


	/**
	 * 判断日期格式和范围
	 */
	public static boolean isFormatDate(String date) {

		String rexp = REGEX_FORMAT_DATE;

		Pattern pat = Pattern.compile(rexp);

		Matcher mat = pat.matcher(date);

		boolean dateType = mat.matches();

		return dateType;
	}

	/**
	 * 根据日期生成星期几
	 *
	 * @param datetime
	 */
	public static int dateToWeek(String datetime) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		int[] weekDays = {7, 1, 2, 3, 4, 5, 6};
		Calendar cal = Calendar.getInstance(); // 获得一个日历
		Date datet = null;
		try {
			datet = f.parse(datetime);
			cal.setTime(datet);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
		if (w < 0) {
			w = 0;
		}
		return weekDays[w];
	}

	/**
	 * 验证是否是两位小数的金额
	 */
	public static boolean isMoney(String str) {
		Pattern pattern = Pattern.compile(REGEX_FORMAT_MONEY);
		Matcher isMoney = pattern.matcher(str);
		if (!isMoney.matches()) {
			return false;
		}
		return true;
	}


	/**
	 * 身份证加密
	 *
	 * @param idcard
	 */
	public static String getEncryptIdCard(String idcard) {

		if (idcard.length() == 18) {
			idcard = idcard.replaceAll("(\\d{10})\\d{4}(\\S{4})", "$1****$2");
		}

		return idcard;

	}

	/**
	 * 身份证加密
	 *
	 * @param phone
	 */
	public static String getEncryptPhone(String phone) {

		if (phone.length() == 11) {
			phone = phone.replaceAll("(\\d{3})\\d{4}(\\S{4})", "$1****$2");
		}

		return phone;

	}

	/**
	 * @param obj 传入的小数
	 * @desc 1.0~1之间的BigDecimal小数，格式化后失去前面的0,则前面直接加上0。 2.传入的参数等于0，则直接返回字符串"0.00"
	 * 3.大于1的小数，直接格式化返回字符串
	 */
	public static String formatToNumber(BigDecimal obj) {
		DecimalFormat df = new DecimalFormat("#.00");
		if (obj.compareTo(BigDecimal.ZERO) == 0) {
			return "0.00";
		} else if (obj.compareTo(BigDecimal.ZERO) > 0 && obj.compareTo(new BigDecimal(1)) < 0) {
			return "0" + df.format(obj).toString();
		} else {
			return df.format(obj).toString();
		}
	}


	/**
	 * 将double格式化为指定小数位的String，不足小数位用0补全
	 *
	 * @param v     需要格式化的数字
	 * @param scale 小数点后保留几位
	 */
	public static String roundByScale(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
				"The   scale   must   be   a   positive   integer   or   zero");
		}
		if (scale == 0) {
			return new DecimalFormat("0").format(v);
		}
		String formatStr = "0.";
		for (int i = 0; i < scale; i++) {
			formatStr = formatStr + "0";
		}
		return new DecimalFormat(formatStr).format(v);
	}

	/**
	 * 拼接sql where in 需要的字符串数据
	 *
	 * @param str
	 * @param regex java.lang.String
	 * @author liubolin
	 * @date 2020/9/18 11:19
	 **/
	public static String getSqlWhereIn(String str, String regex) {
		String[] arr = str.split(regex);
		StringBuffer stringBuffer = new StringBuffer();
		int length = arr.length;
		for (int i = 0; i < length; i++) {
			if (i == length - 1) {
				stringBuffer.append("'").append(arr[i]).append("'");
			} else {
				stringBuffer.append("'").append(arr[i]).append("',");
			}
		}
		return stringBuffer.toString();
	}

	/**
	 * 判断是否有特殊字符
	 */
	public static boolean canshu(String name) {
		Matcher m = REGEX.matcher(name);
		return m.find();
	}
}
