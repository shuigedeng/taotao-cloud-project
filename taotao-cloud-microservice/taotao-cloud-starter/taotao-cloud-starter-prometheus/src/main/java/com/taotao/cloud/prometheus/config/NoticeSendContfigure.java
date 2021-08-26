package com.taotao.cloud.prometheus.config;

import com.taotao.cloud.prometheus.message.INoticeSendComponent;
import com.taotao.cloud.prometheus.pojos.PromethuesNotice;

public interface NoticeSendContfigure<T extends PromethuesNotice> {

	public INoticeSendComponent<T> configure();
}
