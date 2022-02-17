package com.taotao.cloud.redis.delay.message;


public class RedissonHeaders {

	public static final String MESSAGE_ID = "message_id";
	public static final String DELIVERY_QUEUE_NAME = "delivery_queue_name";
	public static final String SEND_TIMESTAMP = "send_timestamp";
	public static final String RECEIVED_TIMESTAMP = "received_timestamp";
	public static final String CHARSET_NAME = "charset_name";
	public static final String EXPECTED_DELAY_MILLIS = "expected_delay_millis";
	public static final String REQUEUE_TIMES = "requeue_times";
	public static final String BATCH_CONVERTED_HEADERS = "batch_converted_headers";
}
