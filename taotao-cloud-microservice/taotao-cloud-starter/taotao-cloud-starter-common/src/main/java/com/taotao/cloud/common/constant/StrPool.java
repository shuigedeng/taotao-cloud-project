/*
 * Copyright (c) 2011-2020, baomidou (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.taotao.cloud.common.constant;

/**
 * StrPool
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:39:05
 */
public class StrPool {

	public static final String AMPERSAND = "&";
	public static final String AND = "and";
	public static final String AT = "@";
	public static final String ASTERISK = "*";
	public static final String STAR = ASTERISK;
	public static final String BACK_SLASH = "\\";
	public static final String COLON = ":";
	public static final String COMMA = ",";
	public static final String DASH = "-";
	public static final String DOLLAR = "$";
	public static final String DOT = ".";
	public static final String DOTDOT = "..";
	public static final String DOT_CLASS = ".class";
	public static final String DOT_JAVA = ".java";
	public static final String DOT_XML = ".xml";
	public static final String EMPTY = "";
	public static final String EQUALS = "=";
	public static final String FALSE = "false";
	public static final String SLASH = "/";
	public static final String HASH = "#";
	public static final String HAT = "^";
	public static final String LEFT_BRACE = "{";
	public static final String LEFT_BRACKET = "(";
	public static final String LEFT_CHEV = "<";
	public static final String DOT_NEWLINE = ",\n";
	public static final String NEWLINE = "\n";
	public static final String N = "n";
	public static final String NO = "no";
	public static final String NULL = "null";
	public static final String OFF = "off";
	public static final String ON = "on";
	public static final String PERCENT = "%";
	public static final String PIPE = "|";
	public static final String PLUS = "+";
	public static final String QUESTION_MARK = "?";
	public static final String EXCLAMATION_MARK = "!";
	public static final String QUOTE = "\"";
	public static final String RETURN = "\r";
	public static final String TAB = "\t";
	public static final String RIGHT_BRACE = "}";
	public static final String RIGHT_BRACKET = ")";
	public static final String RIGHT_CHEV = ">";
	public static final String SEMICOLON = ";";
	public static final String SINGLE_QUOTE = "'";
	public static final String BACKTICK = "`";
	public static final String SPACE = " ";
	public static final String TILDA = "~";
	public static final String LEFT_SQ_BRACKET = "[";
	public static final String RIGHT_SQ_BRACKET = "]";
	public static final String TRUE = "true";
	public static final String UNDERSCORE = "_";
	public static final String UTF_8 = "UTF-8";
	public static final String US_ASCII = "US-ASCII";
	public static final String ISO_8859_1 = "ISO-8859-1";
	public static final String Y = "y";
	public static final String YES = "yes";
	public static final String ONE = "1";
	public static final String ZERO = "0";
	public static final String DOLLAR_LEFT_BRACE = "${";
	public static final String HASH_LEFT_BRACE = "#{";

	public static final String CRLF = "\r\n";
	public static final String HTML_NBSP = "&nbsp;";
	public static final String HTML_AMP = "&amp";
	public static final String HTML_QUOTE = "&quot;";
	public static final String HTML_LT = "&lt;";
	public static final String HTML_GT = "&gt;";

	// ---------------------------------------------------------------- array

	public static final String[] EMPTY_ARRAY = new String[0];

	public static final byte[] BYTES_NEW_LINE = StrPool.NEWLINE.getBytes();

	public static final String BRACE = "{}";
	public static final String GBK = "GBK";
	public static final String STRING_TYPE_NAME = "java.lang.String";
	public static final String LONG_TYPE_NAME = "java.lang.Long";
	public static final String INTEGER_TYPE_NAME = "java.lang.Integer";
	public static final String SHORT_TYPE_NAME = "java.lang.Short";
	public static final String DOUBLE_TYPE_NAME = "java.lang.Double";
	public static final String FLOAT_TYPE_NAME = "java.lang.Float";
	public static final String BOOLEAN_TYPE_NAME = "java.lang.Boolean";
	public static final String SET_TYPE_NAME = "java.lang.Set";
	public static final String LIST_TYPE_NAME = "java.lang.List";
	public static final String COLLECTION_TYPE_NAME = "java.lang.Collection";
	public static final String DATE_TYPE_NAME = "java.util.Date";
	public static final String LOCAL_DATE_TIME_TYPE_NAME = "java.time.LocalDateTime";
	public static final String LOCAL_DATE_TYPE_NAME = "java.time.LocalDate";
	public static final String LOCAL_TIME_TYPE_NAME = "java.time.LocalTime";
	/**
	 * 编码
	 */
	public static final String UTF8 = "UTF-8";
	/**
	 * JSON 资源
	 */
	public static final String CONTENT_TYPE = "application/json; charset=utf-8";


	public static final String TEST_TOKEN = "Bearer test";
	public static final String TEST = "test";
	public static final String PROD = "prod";

	/**
	 * 默认的根节点path
	 */
	public static final String DEF_ROOT_PATH = COMMA;
	/**
	 * 默认的父id
	 */
	public static final Long DEF_PARENT_ID = 0L;

	public static final String UNKNOW = "unknown";
}
