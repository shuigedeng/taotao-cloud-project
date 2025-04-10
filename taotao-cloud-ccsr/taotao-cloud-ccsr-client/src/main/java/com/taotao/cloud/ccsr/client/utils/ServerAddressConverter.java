package com.taotao.cloud.ccsr.client.utils;


import com.taotao.cloud.ccsr.client.dto.ServerAddress;

import java.util.ArrayList;
import java.util.List;

public class ServerAddressConverter {

	public static List<ServerAddress> convert(List<String> addressStrings) {
		List<ServerAddress> serverAddresses = new ArrayList<>();
		for (String addr : addressStrings) {
			// 使用 ":" 分割字符串，获取主机和端口
			String[] parts = addr.split(":");
			if (parts.length == 2) {
				String host = parts[0];
				int port = Integer.parseInt(parts[1]);
				// 默认设置 active 为 true
				serverAddresses.add(new ServerAddress(host, port, true));
			} else {
				throw new IllegalArgumentException("Invalid address format: " + addr);
			}
		}
		return serverAddresses;
	}

}
