package com.taotao.cloud.sys.biz.event.transactional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class DemoListener {

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, classes = DemoEvent.class)
	public void onEvent(DemoEvent demoEvent) {
		log.info("收到事件，事件源是：{}" , demoEvent.getSource());
		// todo 事务提交后的业务处理
	}
}
