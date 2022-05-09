package com.taotao.cloud.health.alarm.core.execut.spi;

import com.taotao.cloud.health.alarm.core.execut.api.IExecute;
import com.taotao.cloud.health.alarm.core.execut.gen.ExecuteNameGenerator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 空报警执行器, 什么都不干
 * <p>
 */
public class NoneExecute implements IExecute {

	public static final String NAME = ExecuteNameGenerator.genExecuteName(NoneExecute.class);

	private static final Logger logger = LoggerFactory.getLogger("alarm");

	@Override
	public void sendMsg(List<String> users, String title, String msg) {
		if (logger.isDebugEnabled()) {
			logger.debug("{} mock! users: {}, title: {}, msg: {}", this.getName(), users, title,
				msg);
		}
	}
}
