package com.taotao.cloud.sys.biz.event.sys;

import org.springframework.context.ApplicationEvent;

public class SysEvent extends ApplicationEvent {
	public SysEvent(Object source) {
		super(source);
	}
}
