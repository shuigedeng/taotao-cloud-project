package com.taotao.cloud.health.warn;

import com.taotao.cloud.health.model.Message;

public abstract class AbstractWarn {

	public abstract void notify(Message message);

}
