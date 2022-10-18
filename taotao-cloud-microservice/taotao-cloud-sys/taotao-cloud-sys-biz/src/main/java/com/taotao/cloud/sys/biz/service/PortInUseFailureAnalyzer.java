package com.taotao.cloud.sys.biz.service;

import com.taotao.cloud.common.utils.log.LogUtils;
import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;
import org.springframework.stereotype.Component;

@Component
public class PortInUseFailureAnalyzer extends AbstractFailureAnalyzer {


	@Override
	protected FailureAnalysis analyze(Throwable rootFailure, Throwable cause) {
		LogUtils.error(rootFailure, "rootFailure===========", rootFailure.getMessage());
		LogUtils.error(cause, "caus============", cause.getMessage());

		return new FailureAnalysis("端口号：" + cause.getMessage() + "被占用", "PortInUseException",
			rootFailure);
	}

}
