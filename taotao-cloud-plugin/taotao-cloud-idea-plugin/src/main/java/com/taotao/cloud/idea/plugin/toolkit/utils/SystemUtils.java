package com.taotao.cloud.idea.plugin.toolkit.utils;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;

/**
 * SystemUtils
 */
public class SystemUtils {

	private SystemUtils() {
	}

	public static void copyToClipboard(String content) {
		Toolkit.getDefaultToolkit()
			.getSystemClipboard()
			.setContents(new StringSelection(content), null);
	}
}
