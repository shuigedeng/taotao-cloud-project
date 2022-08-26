package com.taotao.cloud.monitor.kuding.message;


import com.taotao.cloud.monitor.kuding.pojos.notice.Notice;

@FunctionalInterface
public interface INoticeSendComponent<T extends Notice> {

	public void send(T notice);

}
