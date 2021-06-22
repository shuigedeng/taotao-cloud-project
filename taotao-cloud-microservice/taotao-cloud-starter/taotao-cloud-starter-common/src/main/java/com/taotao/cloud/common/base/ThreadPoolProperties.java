package com.taotao.cloud.common.base;

import com.taotao.cloud.common.utils.PropertyUtil;

/**
 * @author: chejiangyi
 * @version: 2019-07-23 21:54
 **/
public class ThreadPoolProperties {

	public static String Prefix = "bsf.threadpool.";

	public static int getThreadPoolMaxSize() {
		return PropertyUtil.getPropertyCache("bsf.threadpool.max", 500);
	}

	public static int getThreadPoolMinSize() {
		return PropertyUtil.getPropertyCache("bsf.threadpool.min", 0);
	}
}
