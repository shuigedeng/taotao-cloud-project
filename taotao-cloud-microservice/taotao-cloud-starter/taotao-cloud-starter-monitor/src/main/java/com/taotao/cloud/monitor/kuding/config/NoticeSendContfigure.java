package com.taotao.cloud.monitor.kuding.config;


import com.taotao.cloud.monitor.kuding.message.INoticeSendComponent;
import com.taotao.cloud.monitor.kuding.pojos.notice.Notice;

public interface NoticeSendContfigure<T extends Notice> {

	public INoticeSendComponent<T> configure();
}
