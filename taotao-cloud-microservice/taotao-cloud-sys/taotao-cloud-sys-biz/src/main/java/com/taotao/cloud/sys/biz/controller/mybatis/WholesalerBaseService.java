package com.taotao.cloud.sys.biz.controller.mybatis;

public interface WholesalerBaseService {
	public void fixReuseSendMsgNew();
	public void scheduleGenerate();

	public void cursorTest();

	void transactionTemplateTest();

	void transactionalTest();
}
