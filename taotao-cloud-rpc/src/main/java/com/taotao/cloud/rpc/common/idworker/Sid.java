package com.taotao.cloud.rpc.common.idworker;


import com.taotao.cloud.rpc.common.idworker.enums.ServerSelector;
import com.taotao.cloud.rpc.common.idworker.utils.TimeUtils;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Sid {

	private static IdWorker idWorker;

	static {
		configure();
	}

	public static synchronized void configure() {
		long workerId = WorkerIdServer.getWorkerId(ServerSelector.REDIS_SERVER.getCode());
		idWorker = new IdWorker(workerId) {
			@Override
			public long getEpoch() {
				return TimeUtils.midnightMillis();
			}
		};
	}

	/**
	 * 一天最大毫秒86400000，最大占用27比特 27+10+11=48位 最大值281474976710655(15字)，YK0XXHZ827(10字)
	 * 6位(YYMMDD)+15位，共21位
	 *
	 * @return 固定21位数字字符串
	 */

	public static String next() {
		long id = idWorker.nextId();
		String yyMMdd = new SimpleDateFormat("yyMMdd").format(new Date());
		return yyMMdd + String.format("%015d", id);
	}

}
