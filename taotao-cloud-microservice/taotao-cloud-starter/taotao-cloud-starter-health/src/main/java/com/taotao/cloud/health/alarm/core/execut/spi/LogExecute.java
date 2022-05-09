package com.taotao.cloud.health.alarm.core.execut.spi;

import com.taotao.cloud.health.alarm.core.execut.api.IExecute;
import com.taotao.cloud.health.alarm.core.execut.gen.ExecuteNameGenerator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 有些报警,不需要立即上报,但是希望计数, 当大量出现时, 用于升级
 * <p/>
 */
public class LogExecute implements IExecute {
    public static final String NAME = ExecuteNameGenerator.genExecuteName(LogExecute.class);

    private static final Logger logger = LoggerFactory.getLogger("alarm");

    @Override
    public void sendMsg(List<String> users, String title, String msg) {
        logger.info("Do send msg by {} to user:{}, title: {}, msg: {}", getName(), users, title, msg);
    }
}
