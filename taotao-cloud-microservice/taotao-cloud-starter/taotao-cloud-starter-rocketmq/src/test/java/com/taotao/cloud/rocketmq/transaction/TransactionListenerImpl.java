package com.taotao.cloud.rocketmq.transaction;

import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.messaging.Message;

import java.util.HashMap;
import java.util.Map;

@RocketMQTransactionListener(txProducerGroup = "transaction-producer-group")
@Slf4j
public class TransactionListenerImpl implements RocketMQLocalTransactionListener {

	private static Map<String, RocketMQLocalTransactionState> STATE_MAP = new HashMap<>();

	/**
	 * 执行业务逻辑
	 */
	@Override
	public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
		String transId = (String) message.getHeaders().get(RocketMQHeaders.TRANSACTION_ID);
		try {
			System.out.println("用户A账户减500元.");
			System.out.println("用户B账户加500元.");
			STATE_MAP.put(transId, RocketMQLocalTransactionState.COMMIT);
			return RocketMQLocalTransactionState.COMMIT;
		} catch (Exception e) {
			e.printStackTrace();
		}

		STATE_MAP.put(transId, RocketMQLocalTransactionState.ROLLBACK);
		return RocketMQLocalTransactionState.UNKNOWN;

	}

	/**
	 * 回查
	 */
	@Override
	public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
		String transId = (String) message.getHeaders().get(RocketMQHeaders.TRANSACTION_ID);
		log.info("回查消息 -> transId ={} , state = {}", transId, STATE_MAP.get(transId));
		return STATE_MAP.get(transId);
	}
}
