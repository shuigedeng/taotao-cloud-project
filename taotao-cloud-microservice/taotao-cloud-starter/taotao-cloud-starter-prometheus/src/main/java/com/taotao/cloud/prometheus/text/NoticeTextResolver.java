package com.taotao.cloud.prometheus.text;


import com.taotao.cloud.prometheus.pojos.PromethuesNotice;

@FunctionalInterface
public interface NoticeTextResolver<T extends PromethuesNotice> {

	public String resolve(T object);
}
