package com.taotao.cloud.quartz.listener;

import org.quartz.SchedulerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自定义SchedulerListener。定义并配置之后，系统可自动注册
 *
 * @author luas
 * @since 1.0
 */
public abstract class AbstractSchedulerListener implements SchedulerListener {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

}
