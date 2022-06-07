package com.taotao.cloud.dubbo.checker;

import com.taotao.cloud.common.utils.log.LogUtil;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.status.Status;
import org.apache.dubbo.common.status.StatusChecker;

@Activate
public class CustomStatusChecker implements StatusChecker {
	@Override
	public Status check() {
		LogUtil.info("CustomStatusChecker activate ---------------------------------------");
		return new Status(Status.Level.OK, "");
	}
}
