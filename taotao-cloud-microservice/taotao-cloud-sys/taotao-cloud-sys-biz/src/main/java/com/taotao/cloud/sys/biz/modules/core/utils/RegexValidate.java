package com.taotao.cloud.sys.biz.modules.core.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexValidate {
	public static final  Pattern ALPHABETIC = Pattern.compile("[a-zA-Z]+");
	// 首字母汉字
	public static final Pattern CHINESE = Pattern.compile("^[\u4e00-\u9fa5]");
	// 英文字母开头,数字结尾
	public static final Pattern FIRST_EN_END_CN = Pattern.compile("^[A-Za-z]+[0-9]+$");
	// 所有字母是英文
	public static final Pattern ALL_EN = Pattern.compile("^[A-Za-z]+$");
	// 数字规则
	public static final Pattern NUMBER = Pattern.compile("[0-9]+");
	// 用于定义分区字母匹配
	public static final Pattern PARTITION = Pattern.compile("^[a-z0-9]");

	// 用于首字符是字母或数字
	public static final Pattern FIRST_IS_WORD_OR_NUM = Pattern.compile("^[A-Za-z0-9]");

	// 用于首字符是字母或数字
	public static final Pattern END_IS_WORD_OR_NUM = Pattern.compile("[A-Za-z0-9]$");

	// 定义首字母为大写字母
	public static final Pattern BIG_WORD = Pattern.compile("[A-Z]");
	// 英文开头可以包含空格
	public static final Pattern EN_NAME = Pattern.compile("^[A-Za-z\\s]+$");

	// 验证是否含有^%&',);=?$\"等字符："[^%&',);=?$\x22]+"
	public static final Pattern SPEC_CHAR =Pattern.compile("[`~!#$%^&*()+=|{}':);',//[//]<>/?~！#￥%……&*（）——+|{}【】‘；：”“’。，、？]+");

	// 验证文件路径是否为绝对路径
	public static final Pattern ABSOLUTE_FILE_PATH  = Pattern.compile("^\\w:");

	// 验证文件名是否有后缀名
	public static final Pattern FILE_SUFFIX = Pattern.compile("\\.\\w+$");
	
	public static final  Pattern BEGIN_NUM = Pattern.compile("^[\\d][\\d\\D]*");
	public static final  Pattern EXPLORER = Pattern.compile("[\\d\\D]*");
	
	//日期验证
	public static final  Pattern DATE = Pattern.compile("((^((1[8-9]\\d{2})|([2-9]\\d{3}))([-\\/\\._])(10|12|0?[13578])([-\\/\\._])(3[01]|[12][0-9]|0?[1-9])$)|(^((1[8-9]\\d{2})|([2-9]\\d{3}))([-\\/\\._])(11|0?[469])([-\\/\\._])(30|[12][0-9]|0?[1-9])$)|(^((1[8-9]\\d{2})|([2-9]\\d{3}))([-\\/\\._])(0?2)([-\\/\\._])(2[0-8]|1[0-9]|0?[1-9])$)|(^([2468][048]00)([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([3579][26]00)([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([1][89][0][48])([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([2-9][0-9][0][48])([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([1][89][2468][048])([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([2-9][0-9][2468][048])([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([1][89][13579][26])([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([2-9][0-9][13579][26])([-\\/\\._])(0?2)([-\\/\\._])(29)$))");
	
	/***以下是字符串正则,用于组合正则***/
//	public final static String IDENTIFIER = "([a-zA-Z_$][a-zA-Z0-9_$]*)";
	public static final  String IDENTIFIER = "[a-zA-Z_$][\\w$]*";		//单独使用时,前面需加限制 ^
	public static final  String PART = "[\\n\\s]";
	public static final  String PART_NAME = "("+IDENTIFIER+"\\."+PART+"*)*"+IDENTIFIER;
	
	public static final  Pattern HTML = Pattern.compile("<[^>]*>",Pattern.CASE_INSENSITIVE);
	
	/**
	 * 
	 * 作者:sanri
	 * 时间:2016-9-26下午1:34:26
	 * 功能:正则验证
	 */
	public static boolean validate(String value, Pattern pattern) {
		if (value == null) {
            return false;
        }
//		if (pattern.matcher(value).matches())	//修改 bug 匹配不上 at 2016/10/14
		if(pattern.matcher(value).find()) {
            return true;
        }
		return false;
	}
	/**
	 * 
	 * 作者:sanri
	 * 时间:2016-9-26下午1:34:40
	 * 功能:正则提取字符串
	 */
	public static List<String> match(String value, Pattern pattern) {
		if (value == null) {
            return null;
        }

		List<String> matchs = new ArrayList<>();
		Matcher matcher = pattern.matcher(value);
		while (matcher.find()){ //修改 bug at 2016/10/14
			int count = matcher.groupCount();
			if(count == 0){
				//零组表示整个模式。它不包括在此计数中。 但又匹配到了,所以返回整个模式匹配串 修改 bug at 2016/10/14 
				matchs.add(matcher.group());
			}else{
				for (int i = 0; i < count; i++) {
					String group = matcher.group(i + 1);
					matchs.add(group);
				}
			}
		}

		return matchs;
	}
	
	/**
	 * 统计 字符串中 包含正则表达示匹配的元素个数
	 * @param content 文本源
	 * @param regex 正则表达式
	 * @return 个数
	 */
	public static long countRegex(String content,Pattern pattern){
		if(StringUtils.isBlank(content)){
			return 0;
		}
		Matcher matcher = pattern.matcher(content);
		long count = 0;
		while(matcher.find()){
			count ++;
		}
		return count;
	} 
	
	/**
	 * 
	 * 功能:<br>
	 * 创建时间:2016-9-24下午3:58:40<br>
	 * 作者：sanri<br>
	 * see StringUtil.isBlank()
	 */
	@Deprecated
	public static boolean isEmpty(String str) {
		return str == null || ("".equals(str.trim())); 
	}
	public static boolean isEmpty(Collection<?> collection){
		return collection==null || collection.size() == 0;
	}
	public static boolean isEmpty(Map<?,?> map){
		return map == null || map.isEmpty();
	}
	public static boolean isEmpty(Object [] array){
		return array==null || array.length == 0;
	}
	/**
	 * 判断 文件是否为空
	 * @return
	 */
	public static boolean isEmpty(File file){
		return file == null || !file.exists();
	}

	/**
	 * 提取字符串的前面的数字  ; 类似 js 的  parseInt 功能
	 * @param source
	 * @return
	 */
	public static Integer parseInt(String source){
		if(StringUtils.isBlank(source)){
		    return null;
        }

        //第一个字符必须是数字
        char c = source.charAt(0);
        boolean digit = Character.isDigit(c);
        if(!digit){
            return null;
        }

        Matcher matcher = NUMBER.matcher(source);
		if(matcher.find()){
            String group0 = matcher.group(0);
            return NumberUtils.toInt(group0);
        }
        return null;
	}
}
