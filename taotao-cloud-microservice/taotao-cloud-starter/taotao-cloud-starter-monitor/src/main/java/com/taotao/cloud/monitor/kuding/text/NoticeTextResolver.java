package com.taotao.cloud.monitor.kuding.text;


import com.taotao.cloud.monitor.kuding.pojos.notice.Notice;

@FunctionalInterface
public interface NoticeTextResolver<T extends Notice> {

	public String resolve(T object);
}
