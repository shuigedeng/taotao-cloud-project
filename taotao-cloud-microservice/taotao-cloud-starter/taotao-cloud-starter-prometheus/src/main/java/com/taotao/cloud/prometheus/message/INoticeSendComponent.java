package com.taotao.cloud.prometheus.message;


import com.taotao.cloud.prometheus.model.PromethuesNotice;

@FunctionalInterface
public interface INoticeSendComponent<T extends PromethuesNotice> {

	public void send(T notice);

}
