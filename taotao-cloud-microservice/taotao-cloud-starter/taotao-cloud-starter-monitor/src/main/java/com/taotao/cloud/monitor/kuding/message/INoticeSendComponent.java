package com.taotao.cloud.monitor.kuding.message;


import com.taotao.cloud.monitor.kuding.pojos.PromethuesNotice;

@FunctionalInterface
public interface INoticeSendComponent<T extends PromethuesNotice> {

	public void send(T notice);

}
