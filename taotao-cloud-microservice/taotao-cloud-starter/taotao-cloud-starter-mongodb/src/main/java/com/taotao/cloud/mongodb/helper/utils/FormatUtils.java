package com.taotao.cloud.mongodb.helper.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * FormatUtils
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-27 21:53:43
 */
public class FormatUtils {

	public static Pattern regex = Pattern.compile("\\$\\{([^}]*)\\}");

	/**
	 * bson
	 *
	 * @param json json
	 * @return {@link String }
	 * @since 2022-05-27 21:53:43
	 */
	public static String bson(String json) {
		json = transString(json);

		String blank = "    ";
		String indent = "";// 缩进
		StringBuilder sb = new StringBuilder();

		for (char c : json.toCharArray()) {
			switch (c) {
				case '{' -> {
					indent += blank;
					sb.append("{\n").append(indent);
				}
				case '}' -> {
					indent = indent.substring(0, indent.length() - blank.length());
					sb.append("\n").append(indent).append("}");
				}
				case '[' -> {
					indent += blank;
					sb.append("[\n").append(indent);
				}
				case ']' -> {
					indent = indent.substring(0, indent.length() - blank.length());
					sb.append("\n").append(indent).append("]");
				}
				case ',' -> sb.append(",\n").append(indent);
				default -> sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * 转换$oid为ObjectId()
	 *
	 * @param str
	 * @return {@link String }
	 * @since 2022-05-27 21:53:43
	 */
	private static String transString(String str) {
		str = str.replace(", ", ",").replace("{\"$oid\":", "${");

		List<String> temp = getContentInfo(str);
		for (String tp : temp) {
			str = str.replace("${" + tp + "}", "ObjectId(" + tp.trim() + ")");
		}

		return str;
	}

	/**
	 * 获取表达式中${}中的值
	 * 
	 * @param content
	 */
	private static List<String> getContentInfo(String content) {

		Matcher matcher = regex.matcher(content);
		List<String> list = new ArrayList<String>();
		while (matcher.find()) {
			list.add(matcher.group(1));
		}

		return list;

	}
}
