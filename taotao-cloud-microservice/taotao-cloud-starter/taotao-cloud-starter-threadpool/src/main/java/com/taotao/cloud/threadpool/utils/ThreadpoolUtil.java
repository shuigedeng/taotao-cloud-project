package com.taotao.cloud.threadpool.utils;

import com.dtp.core.support.ThreadPoolCreator;
import com.dtp.core.thread.DtpExecutor;

public class ThreadpoolUtil {

	public static DtpExecutor dtpExecutor() {
		return ThreadPoolCreator.createDynamicFast("commonExecutor");
	}
}
