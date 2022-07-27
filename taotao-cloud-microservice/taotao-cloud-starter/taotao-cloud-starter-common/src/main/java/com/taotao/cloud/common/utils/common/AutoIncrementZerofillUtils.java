package com.taotao.cloud.common.utils.common;

import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.common.exception.BaseException;
import com.taotao.cloud.common.utils.lang.StringUtil;

/**
 * 自动递增填充零
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-07-27 20:23:33
 */
public class AutoIncrementZerofillUtils {
	
	/**
	 * 获得初始化值，自动填充零
	 * @param length 初始化长度
	 * @return 如：0001
	 */
	public static String getInitValue(int length) {
		return StrUtil.padPre("1", length, '0');
	}
	
	/**
	 * 字符串尾部值自动递增
	 * @param str 尾部值是 {@linkplain Integer} 类型
	 * @return 自动递增后的值
	 */
	public static String autoIncrement(String str) {
		int maxIndex = str.length() - 1;
		int autoIncrementValue = Integer.parseInt(CharUtil.toString(str.charAt(maxIndex))) + 1;
		if (autoIncrementValue == 10) {
			int cycleIndex = 0;
			for (int i = maxIndex - 1; i >= 0; i--) {
				int autoIncrementValueI = Integer.parseInt(CharUtil.toString(str.charAt(i))) + 1;
				cycleIndex++;
				if (autoIncrementValueI != 10) {
					String pad = StrUtil.padPre("0", cycleIndex, '0');
					String replaceValue = Integer.toString(autoIncrementValueI) + pad;
					return StringUtil.replace(str, replaceValue, i, i + 1 + replaceValue.length());
				}
			}
			
			throw new BaseException("无法自动递增，此参数已是最大值：" + str);
		}
		
		return str.substring(0, maxIndex) + autoIncrementValue;
	}
	
	/**
	 * 字符串尾部值自动递减
	 * @param str 尾部值是 {@linkplain Integer} 类型
	 * @return 自动递减后的值
	 */
	public static String autoDecr(String str) {
		int maxIndex = str.length() - 1;
		int autoDecrValue = Integer.parseInt(CharUtil.toString(str.charAt(maxIndex))) - 1;
		if (autoDecrValue == -1) {
			int cycleIndex = 0;
			for (int i = maxIndex - 1; i >= 0; i--) {
				int autoDecrValueI = Integer.parseInt(CharUtil.toString(str.charAt(i))) - 1;
				cycleIndex++;
				if (autoDecrValueI != -1) {
					String pad = StrUtil.padPre("9", cycleIndex, '9');
					String replaceValue = Integer.toString(autoDecrValueI) + pad;
					return StringUtil.replace(str, replaceValue, i, i + 1 + replaceValue.length());
				}
			}
			
			throw new BaseException("无法自动递减，此参数已是最小值：" + str);
		}
		
		return str.substring(0, maxIndex) + autoDecrValue;
	}
	
}
