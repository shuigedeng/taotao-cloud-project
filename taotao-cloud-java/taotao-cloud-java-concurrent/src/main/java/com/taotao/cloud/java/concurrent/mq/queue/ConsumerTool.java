package com.taotao.cloud.java.mq.queue;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.beans.ExceptionListener;

public class ConsumerTool implements MessageListener, ExceptionListener {
	private String user = ActiveMQConnection.DEFAULT_USER;
	private String password = ActiveMQConnection.DEFAULT_PASSWORD;
	private String url = ActiveMQConnection.DEFAULT_BROKER_URL;
	private String subject = "myqueue";
	private Queue destination = null;
	private Connection connection = null;
	private Session session = null;
	private MessageConsumer consumer = null;
	private ActiveMQConnectionFactory connectionFactory = null;
	public static Boolean isconnection = false;

	// 初始化
	private void initialize() throws JMSException {
		connectionFactory = new ActiveMQConnectionFactory(
			user, password, url);
		connection = connectionFactory.createConnection();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		destination = session.createQueue(subject);
		consumer = session.createConsumer(destination);
	}

	// 消费消息
	public void consumeMessage() throws JMSException {
		initialize();
		connection.start();

		consumer.setMessageListener(this);
		connection.setExceptionListener((javax.jms.ExceptionListener) this);
		System.out.println("Consumer:->Begin listening...");
		isconnection = true;
		// 开始监听
		Message message = consumer.receive();
		System.out.println(message.getJMSMessageID());
	}

	// 关闭连接
	public void close() throws JMSException {
		System.out.println("Consumer:->Closing connection");
		if (consumer != null) {
			consumer.close();
		}
		if (session != null) {
			session.close();
		}
		if (connection != null) {
			connection.close();
		}
	}

	// 消息处理函数
	@Override
	public void onMessage(Message message) {
		try {
			if (message instanceof TextMessage) {
				TextMessage txtMsg = (TextMessage) message;
				String msg = txtMsg.getText();
				System.out.println("Consumer:->Received: " + msg);
			} else {
				System.out.println("Consumer:->Received: " + message);
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void exceptionThrown(Exception e) {

	}
}
