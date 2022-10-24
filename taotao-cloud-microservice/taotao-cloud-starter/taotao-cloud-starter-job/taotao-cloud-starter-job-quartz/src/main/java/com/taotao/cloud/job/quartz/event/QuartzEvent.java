package com.taotao.cloud.job.quartz.event;

import org.springframework.context.ApplicationEvent;

public class QuartzEvent extends ApplicationEvent {
	public QuartzEvent(Object source) {
		super(source);
	}
}
