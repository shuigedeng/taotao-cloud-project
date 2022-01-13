package com.taotao.cloud.sys.biz.springboot.mysql.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hrhx.springboot.domain.IndentityInfoBean;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
 * 
 * @author duhongming
 *
 */
@Service
public class IdCardService {
	
	@Value("classpath:static/data/idcard.json")
    private Resource info;
	
	public String getIdCardByIdSix(String sixCode) throws IOException{
		Map<String, Object> map = JSON.parseObject(injectInfo(), new TypeReference<Map<String, Object>>(){});
		return map.get(sixCode).toString();
	}
	
	private String injectInfo() throws IOException{
        return IOUtils.toString(info.getInputStream());
    }
	
	private final static int[] DAY_ARR = new int[] { 20, 19, 21, 20, 21, 22, 23,
			23, 23, 24, 23, 22 };
	private final static String[] CONSTELLATION_ARR = new String[] { "摩羯座",
			"水瓶座", "双鱼座", "牧羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座",
			"天蝎座", "射手座", "摩羯座" };
	public static final String[] ZODIAC_ARR = { "猴", "鸡", "狗", "猪", "鼠", "牛",
			"虎", "兔", "龙", "蛇", "马", "羊" };

	/** 中国公民身份证号码最小长度。 */
	public static final int CHINA_ID_MIN_LENGTH = 15;

	/** 中国公民身份证号码最大长度。 */
	public static final int CHINA_ID_MAX_LENGTH = 18;

	/** 省、直辖市代码表 */
	public static final String[] CITY_CODE = { "11", "12", "13", "14", "15",
			"21", "22", "23", "31", "32", "33", "34", "35", "36", "37", "41",
			"42", "43", "44", "45", "46", "50", "51", "52", "53", "54", "61",
			"62", "63", "64", "65", "71", "81", "82", "91" };

	/** 每位加权因子 */
	public static final int[] POWER = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9,
			10, 5, 8, 4, 2 };

	/** 第18位校检码 */
	public static final String[] VERIFY_CODE = { "1", "0", "X", "9", "8", "7",
			"6", "5", "4", "3", "2" };
	/** 最低年限 */
	public static final int MIN = 1930;
	public static Map<String, String> cityCodes = new HashMap<String, String>();
	/** 台湾身份首字母对应数字 */
	public static Map<String, Integer> twFirstCode = new HashMap<String, Integer>();
	/** 香港身份首字母对应数字 */
	public static Map<String, Integer> hkFirstCode = new HashMap<String, Integer>();
	static {
		cityCodes.put("11", "北京");
		cityCodes.put("12", "天津");
		cityCodes.put("13", "河北");
		cityCodes.put("14", "山西");
		cityCodes.put("15", "内蒙古");
		cityCodes.put("21", "辽宁");
		cityCodes.put("22", "吉林");
		cityCodes.put("23", "黑龙江");
		cityCodes.put("31", "上海");
		cityCodes.put("32", "江苏");
		cityCodes.put("33", "浙江");
		cityCodes.put("34", "安徽");
		cityCodes.put("35", "福建");
		cityCodes.put("36", "江西");
		cityCodes.put("37", "山东");
		cityCodes.put("41", "河南");
		cityCodes.put("42", "湖北");
		cityCodes.put("43", "湖南");
		cityCodes.put("44", "广东");
		cityCodes.put("45", "广西");
		cityCodes.put("46", "海南");
		cityCodes.put("50", "重庆");
		cityCodes.put("51", "四川");
		cityCodes.put("52", "贵州");
		cityCodes.put("53", "云南");
		cityCodes.put("54", "西藏");
		cityCodes.put("61", "陕西");
		cityCodes.put("62", "甘肃");
		cityCodes.put("63", "青海");
		cityCodes.put("64", "宁夏");
		cityCodes.put("65", "新疆");
		cityCodes.put("71", "台湾");
		cityCodes.put("81", "香港");
		cityCodes.put("82", "澳门");
		cityCodes.put("91", "国外");
		twFirstCode.put("A", 10);
		twFirstCode.put("B", 11);
		twFirstCode.put("C", 12);
		twFirstCode.put("D", 13);
		twFirstCode.put("E", 14);
		twFirstCode.put("F", 15);
		twFirstCode.put("G", 16);
		twFirstCode.put("H", 17);
		twFirstCode.put("J", 18);
		twFirstCode.put("K", 19);
		twFirstCode.put("L", 20);
		twFirstCode.put("M", 21);
		twFirstCode.put("N", 22);
		twFirstCode.put("P", 23);
		twFirstCode.put("Q", 24);
		twFirstCode.put("R", 25);
		twFirstCode.put("S", 26);
		twFirstCode.put("T", 27);
		twFirstCode.put("U", 28);
		twFirstCode.put("V", 29);
		twFirstCode.put("X", 30);
		twFirstCode.put("Y", 31);
		twFirstCode.put("W", 32);
		twFirstCode.put("Z", 33);
		twFirstCode.put("I", 34);
		twFirstCode.put("O", 35);
		hkFirstCode.put("A", 1);
		hkFirstCode.put("B", 2);
		hkFirstCode.put("C", 3);
		hkFirstCode.put("R", 18);
		hkFirstCode.put("U", 21);
		hkFirstCode.put("Z", 26);
		hkFirstCode.put("X", 24);
		hkFirstCode.put("W", 23);
		hkFirstCode.put("O", 15);
		hkFirstCode.put("N", 14);
	}

	/**
	 * 将15位身份证号码转换为18位
	 * 
	 * @param idCard
	 *            15位身份编码
	 * @return 18位身份编码
	 * @throws ParseException 
	 */
	private String conver15CardTo18(String idCard) throws ParseException {
		String idCard18 = "";
		if (idCard.length() != CHINA_ID_MIN_LENGTH) {
			return null;
		}
		if (isNum(idCard)) {
			// 获取出生年月日
			String birthday = idCard.substring(6, 12);
			Date birthDate = new SimpleDateFormat("yyMMdd").parse(birthday);
			Calendar cal = Calendar.getInstance();
			if (birthDate != null){
				cal.setTime(birthDate);
			}
			// 获取出生年(完全表现形式,如：2010)
			String sYear = String.valueOf(cal.get(Calendar.YEAR));
			idCard18 = idCard.substring(0, 6) + sYear + idCard.substring(8);
			// 转换字符数组
			char[] cArr = idCard18.toCharArray();
			if (cArr != null) {
				int[] iCard = converCharToInt(cArr);
				int iSum17 = getPowerSum(iCard);
				// 获取校验位
				String sVal = getCheckCode18(iSum17);
				if (sVal.length() > 0) {
					idCard18 += sVal;
				} else {
					return null;
				}
			}
		} else {
			return null;
		}
		return idCard18;
	}

	/**
	 * 验证身份证是否合法
	 * @throws ParseException 
	 */
	private boolean validateCard(String idCard) throws ParseException {
		String card = idCard.trim();
		if (validateIdCard18(card)) {
			return true;
		}
		if (validateIdCard15(card)) {
			return true;
		}
		String[] cardval = validateIdCard10(card);
		if (cardval != null) {
			if ("true".equals(cardval[2])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 验证18位身份编码是否合法
	 * 
	 * @param idCard
	 *            身份编码
	 * @return 是否合法
	 */
	private boolean validateIdCard18(String idCard) {
		boolean bTrue = false;
		if (idCard.length() == CHINA_ID_MAX_LENGTH) {
			// 前17位
			String code17 = idCard.substring(0, 17);
			// 第18位
			String code18 = idCard.substring(17, CHINA_ID_MAX_LENGTH);
			if (isNum(code17)) {
				char[] cArr = code17.toCharArray();
				if (cArr != null) {
					int[] iCard = converCharToInt(cArr);
					int iSum17 = getPowerSum(iCard);
					// 获取校验位
					String val = getCheckCode18(iSum17);
					if (val.length() > 0) {
						if (val.equalsIgnoreCase(code18)) {
							bTrue = true;
						}
					}
				}
			}
		}
		return bTrue;
	}

	/**
	 * 验证15位身份编码是否合法
	 * 
	 * @param idCard
	 *            身份编码
	 * @return 是否合法
	 * @throws ParseException 
	 */
	private boolean validateIdCard15(String idCard) throws ParseException {
		if (idCard.length() != CHINA_ID_MIN_LENGTH) {
			return false;
		}
		if (isNum(idCard)) {
			String proCode = idCard.substring(0, 2);
			if (cityCodes.get(proCode) == null) {
				return false;
			}
			String birthCode = idCard.substring(6, 12);
			Date birthDate = new SimpleDateFormat("yy").parse(birthCode.substring(0, 2));
			Calendar cal = Calendar.getInstance();
			if (birthDate != null){
				cal.setTime(birthDate);
			}
			if (!valiDate(cal.get(Calendar.YEAR),
					Integer.valueOf(birthCode.substring(2, 4)),
					Integer.valueOf(birthCode.substring(4, 6)))) {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}

	/**
	 * 验证10位身份编码是否合法
	 * 
	 * @param idCard
	 *            身份编码
	 * @return 身份证信息数组
	 *         <p>
	 *         [0] - 台湾、澳门、香港 [1] - 性别(男M,女F,未知N) [2] - 是否合法(合法true,不合法false)
	 *         若不是身份证件号码则返回null
	 *         </p>
	 */
	private String[] validateIdCard10(String idCard) {
		String[] info = new String[3];
		String card = idCard.replaceAll("[\\(|\\)]", "");
		if (card.length() != 8 && card.length() != 9 && idCard.length() != 10) {
			return null;
		}
		// 台湾
		if (idCard.matches("^[a-zA-Z][0-9]{9}$")) { 
			info[0] = "台湾";
			String char2 = idCard.substring(1, 2);
			if (char2.equals("1")) {
				info[1] = "M";
			} else if (char2.equals("2")) {
				info[1] = "F";
			} else {
				info[1] = "N";
				info[2] = "false";
				return info;
			}
			info[2] = validateTWCard(idCard) ? "true" : "false";
		// 澳门
		} else if (idCard.matches("^[1|5|7][0-9]{6}\\(?[0-9A-Z]\\)?$")) { 
			info[0] = "澳门";
			info[1] = "N";
			// TODO
		// 香港
		} else if (idCard.matches("^[A-Z]{1,2}[0-9]{6}\\(?[0-9A]\\)?$")) { 
			info[0] = "香港";
			info[1] = "N";
			info[2] = validateHKCard(idCard) ? "true" : "false";
		} else {
			return null;
		}
		return info;
	}

	/**
	 * 验证台湾身份证号码
	 * 
	 * @param idCard
	 *            身份证号码
	 * @return 验证码是否符合
	 */
	private boolean validateTWCard(String idCard) {
		String start = idCard.substring(0, 1);
		String mid = idCard.substring(1, 9);
		String end = idCard.substring(9, 10);
		Integer iStart = twFirstCode.get(start);
		Integer sum = iStart / 10 + (iStart % 10) * 9;
		char[] chars = mid.toCharArray();
		Integer iflag = 8;
		for (char c : chars) {
			sum = sum + Integer.valueOf(c + "") * iflag;
			iflag--;
		}
		return (sum % 10 == 0 ? 0 : (10 - sum % 10)) == Integer.valueOf(end) ? true
				: false;
	}

	/**
	 * 验证香港身份证号码(存在Bug，部份特殊身份证无法检查)
	 * <p>
	 * 身份证前2位为英文字符，如果只出现一个英文字符则表示第一位是空格，对应数字58 前2位英文字符A-Z分别对应数字10-35
	 * 最后一位校验码为0-9的数字加上字符"A"，"A"代表10
	 * </p>
	 * <p>
	 * 将身份证号码全部转换为数字，分别对应乘9-1相加的总和，整除11则证件号码有效
	 * </p>
	 * 
	 * @param idCard
	 *            身份证号码
	 * @return 验证码是否符合
	 */
	private boolean validateHKCard(String idCard) {
		String card = idCard.replaceAll("[\\(|\\)]", "");
		Integer sum = 0;
		if (card.length() == 9) {
			sum = (Integer.valueOf(card.substring(0, 1).toUpperCase()
					.toCharArray()[0]) - 55)
					* 9
					+ (Integer.valueOf(card.substring(1, 2).toUpperCase()
							.toCharArray()[0]) - 55) * 8;
			card = card.substring(1, 9);
		} else {
			sum = 522 + (Integer.valueOf(card.substring(0, 1).toUpperCase()
					.toCharArray()[0]) - 55) * 8;
		}
		String mid = card.substring(1, 7);
		String end = card.substring(7, 8);
		char[] chars = mid.toCharArray();
		Integer iflag = 7;
		for (char c : chars) {
			sum = sum + Integer.valueOf(c + "") * iflag;
			iflag--;
		}
		if (end.toUpperCase().equals("A")) {
			sum = sum + 10;
		} else {
			sum = sum + Integer.valueOf(end);
		}
		return (sum % 11 == 0) ? true : false;
	}

	/**
	 * 将字符数组转换成数字数组
	 * 
	 * @param ca
	 *            字符数组
	 * @return 数字数组
	 */
	private int[] converCharToInt(char[] ca) {
		int len = ca.length;
		int[] iArr = new int[len];
		for (int i = 0; i < len; i++) {
			iArr[i] = Integer.parseInt(String.valueOf(ca[i]));
		}
		return iArr;
	}

	/**
	 * 将身份证的每位和对应位的加权因子相乘之后，再得到和值
	 * 
	 * @param iArr
	 * @return 身份证编码。
	 */
	private int getPowerSum(int[] iArr) {
		int iSum = 0;
		if (POWER.length == iArr.length) {
			for (int i = 0; i < iArr.length; i++) {
				for (int j = 0; j < POWER.length; j++) {
					if (i == j) {
						iSum = iSum + iArr[i] * POWER[j];
					}
				}
			}
		}
		return iSum;
	}

	/**
	 * 将power和值与11取模获得余数进行校验码判断
	 * 
	 * @param iSum
	 * @return 校验位
	 */
	private String getCheckCode18(int iSum) {
		String sCode = "";
		switch (iSum % 11) {
		case 10:
			sCode = "2";
			break;
		case 9:
			sCode = "3";
			break;
		case 8:
			sCode = "4";
			break;
		case 7:
			sCode = "5";
			break;
		case 6:
			sCode = "6";
			break;
		case 5:
			sCode = "7";
			break;
		case 4:
			sCode = "8";
			break;
		case 3:
			sCode = "9";
			break;
		case 2:
			sCode = "x";
			break;
		case 1:
			sCode = "0";
			break;
		case 0:
			sCode = "1";
			break;
		default:
		}
		return sCode;
	}

	/**
	 * 根据身份编号获取年龄
	 * 
	 * @param idCard
	 *            身份编号
	 * @return 年龄
	 * @throws ParseException 
	 */
	private int getAgeByIdCard(String idCard) throws ParseException {
		int iAge = 0;
		if (idCard.length() == CHINA_ID_MIN_LENGTH) {
			idCard = conver15CardTo18(idCard);
		}
		String year = idCard.substring(6, 10);
		Calendar cal = Calendar.getInstance();
		int iCurrYear = cal.get(Calendar.YEAR);
		iAge = iCurrYear - Integer.valueOf(year);
		return iAge;
	}

	/**
	 * 根据身份编号获取生日
	 * 
	 * @param idCard
	 *            身份编号
	 * @return 生日(yyyyMMdd)
	 * @throws ParseException 
	 */
	private String getBirthByIdCard(String idCard) throws ParseException {
		Integer len = idCard.length();
		if (len < CHINA_ID_MIN_LENGTH) {
			return null;
		} else if (len == CHINA_ID_MIN_LENGTH) {
			idCard = conver15CardTo18(idCard);
		}
		return idCard.substring(6, 14);
	}

	/**
	 * 根据身份编号获取生日年
	 * 
	 * @param idCard
	 *            身份编号
	 * @return 生日(yyyy)
	 * @throws ParseException 
	 */
	private Short getYearByIdCard(String idCard) throws ParseException {
		Integer len = idCard.length();
		if (len < CHINA_ID_MIN_LENGTH) {
			return null;
		} else if (len == CHINA_ID_MIN_LENGTH) {
			idCard = conver15CardTo18(idCard);
		}
		return Short.valueOf(idCard.substring(6, 10));
	}

	/**
	 * 根据身份编号获取生日月
	 * 
	 * @param idCard
	 *            身份编号
	 * @return 生日(MM)
	 * @throws ParseException 
	 */
	private Short getMonthByIdCard(String idCard) throws ParseException {
		Integer len = idCard.length();
		if (len < CHINA_ID_MIN_LENGTH) {
			return null;
		} else if (len == CHINA_ID_MIN_LENGTH) {
			idCard = conver15CardTo18(idCard);
		}
		return Short.valueOf(idCard.substring(10, 12));
	}

	/**
	 * 根据身份编号获取生日天
	 * 
	 * @param idCard
	 *            身份编号
	 * @return 生日(dd)
	 * @throws ParseException 
	 */
	private Short getDateByIdCard(String idCard) throws ParseException {
		Integer len = idCard.length();
		if (len < CHINA_ID_MIN_LENGTH) {
			return null;
		} else if (len == CHINA_ID_MIN_LENGTH) {
			idCard = conver15CardTo18(idCard);
		}
		return Short.valueOf(idCard.substring(12, 14));
	}

	/**
	 * 根据身份编号获取性别
	 * 
	 * @param idCard
	 *            身份编号
	 * @return 性别(M-男，F-女，N-未知)
	 * @throws ParseException 
	 */
	private String getGenderByIdCard(String idCard) throws ParseException {
		String sGender = "N";
		if (idCard.length() == CHINA_ID_MIN_LENGTH) {
			idCard = conver15CardTo18(idCard);
		}
		String sCardNum = idCard.substring(16, 17);
		if (Integer.parseInt(sCardNum) % 2 != 0) {
			sGender = "男";
		} else {
			sGender = "女";
		}
		return sGender;
	}

	/**
	 * 根据身份编号获取户籍省份
	 * 
	 * @param idCard
	 *            身份编码
	 * @return 省级编码。
	 */
	private String getProvinceByIdCard(String idCard) {
		int len = idCard.length();
		String sProvince = null;
		String sProvinNum = "";
		if (len == CHINA_ID_MIN_LENGTH || len == CHINA_ID_MAX_LENGTH) {
			sProvinNum = idCard.substring(0, 2);
		}
		sProvince = cityCodes.get(sProvinNum) + "省";
		return sProvince;
	}

	private String getCityByIdCard(String idCard) throws IOException {
		int len = idCard.length();
		String sCity = null;
		String sCityNum = "";
		if (len == CHINA_ID_MIN_LENGTH || len == CHINA_ID_MAX_LENGTH) {
			sCityNum = idCard.substring(0, 6);
		}
		sCity = getIdCardByIdSix(sCityNum);
		return sCity;
	}

	/**
	 * 数字验证
	 * 
	 * @param val
	 * @return 提取的数字。
	 */
	private boolean isNum(String val) {
		return val == null || "".equals(val) ? false : val.matches("^[0-9]*$");
	}

	/**
	 * 验证小于当前日期 是否有效
	 * 
	 * @param iYear
	 *            待验证日期(年)
	 * @param iMonth
	 *            待验证日期(月 1-12)
	 * @param iDate
	 *            待验证日期(日)
	 * @return 是否有效
	 */
	private boolean valiDate(int iYear, int iMonth, int iDate) {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int datePerMonth;
		if (iYear < MIN || iYear >= year) {
			return false;
		}
		if (iMonth < 1 || iMonth > 12) {
			return false;
		}
		switch (iMonth) {
		case 4:
		case 6:
		case 9:
		case 11:
			datePerMonth = 30;
			break;
		case 2:
			boolean dm = ((iYear % 4 == 0 && iYear % 100 != 0) || (iYear % 400 == 0))
					&& (iYear > MIN && iYear < year);
			datePerMonth = dm ? 29 : 28;
			break;
		default:
			datePerMonth = 31;
		}
		return (iDate >= 1) && (iDate <= datePerMonth);
	}

	private String getConstellation(int month, int day) {
		return day < DAY_ARR[month - 1] ? CONSTELLATION_ARR[month - 1]
				: CONSTELLATION_ARR[month];
	}

	private String date2Zodica(Calendar time) {
		return ZODIAC_ARR[time.get(Calendar.YEAR) % 12];
	}
	
	public IndentityInfoBean getIndentityInfoBean(String idCard) throws ParseException, IOException{
		IndentityInfoBean indentityInfo = new IndentityInfoBean();
		if (validateCard(idCard)) {
			short year = getYearByIdCard(idCard);
			short month = getMonthByIdCard(idCard);
			short day = getDateByIdCard(idCard);
			indentityInfo.setVerify(validateCard(idCard));
			indentityInfo.setBirthDate(getBirthByIdCard(idCard));
			indentityInfo.setAge(getAgeByIdCard(idCard));
			indentityInfo.setProvince(getProvinceByIdCard(idCard));
			indentityInfo.setAddressDetail(getCityByIdCard(idCard));
			indentityInfo.setBirthYear(year);
			indentityInfo.setBirthMonth(month);
			indentityInfo.setBirthDay(day);
			indentityInfo.setSex(getGenderByIdCard(idCard));
			indentityInfo.setConstellation(getConstellation(month, day));
			Calendar c = Calendar.getInstance();
			c.set(year, month, day);
			indentityInfo.setZodiac(date2Zodica(c));	
		} else {
			indentityInfo.setVerify(false);
		}
		return indentityInfo;
	}
}
