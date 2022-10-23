package com.taotao.cloud.elasticsearch.esearchx;

import java.nio.charset.Charset;
import java.util.Base64;
import org.noear.snack.ONode;
import org.noear.snack.core.Options;

/**
 * 内部工具（外部别用它）
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-16 16:05:13
 */
public class PriUtils {

	private static final Options nodeOptions = Options.def();

	public static ONode newNode() {
		return new ONode(nodeOptions);
	}

	/**
	 * 是否为空
	 */
	public static boolean isEmpty(String str) {
		return (str == null || str.length() == 0);
	}

	/**
	 * 是否不为空
	 */
	public static boolean isNotEmpty(String str) {
		return !(str == null || str.length() == 0);
	}

	/**
	 * Base64 编码
	 */
	public static String b64Encode(String text) {
		return Base64.getEncoder().encodeToString(text.getBytes(Charset.forName("UTF-8")));
	}
}
