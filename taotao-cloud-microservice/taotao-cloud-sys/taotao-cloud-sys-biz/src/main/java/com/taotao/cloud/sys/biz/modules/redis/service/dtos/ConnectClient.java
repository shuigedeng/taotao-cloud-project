package com.taotao.cloud.sys.biz.modules.redis.service.dtos;

import lombok.Data;
import redis.clients.jedis.HostAndPort;

@Data
public class ConnectClient {
	private String id;
	private HostAndPort connect;
	private String age;
	private String idle;
	private String cmd;
}
