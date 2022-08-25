package com.taotao.cloud.monitor.kuding.text;


import com.taotao.cloud.monitor.kuding.pojos.PromethuesNotice;

@FunctionalInterface
public interface NoticeTextResolver<T extends PromethuesNotice> {

	public String resolve(T object);
}
