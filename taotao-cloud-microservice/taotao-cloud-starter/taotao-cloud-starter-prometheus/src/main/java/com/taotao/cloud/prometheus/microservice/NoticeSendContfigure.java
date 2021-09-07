package com.taotao.cloud.prometheus.microservice;


import com.taotao.cloud.prometheus.message.INoticeSendComponent;
import com.taotao.cloud.prometheus.model.PromethuesNotice;

public interface NoticeSendContfigure<T extends PromethuesNotice> {

	public INoticeSendComponent<T> configure();
}
