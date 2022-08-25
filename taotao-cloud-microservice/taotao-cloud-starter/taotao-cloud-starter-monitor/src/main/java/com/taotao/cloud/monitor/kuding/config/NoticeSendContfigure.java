package com.taotao.cloud.monitor.kuding.config;


import com.taotao.cloud.monitor.kuding.message.INoticeSendComponent;
import com.taotao.cloud.monitor.kuding.pojos.PromethuesNotice;

public interface NoticeSendContfigure<T extends PromethuesNotice> {

	public INoticeSendComponent<T> configure();
}
