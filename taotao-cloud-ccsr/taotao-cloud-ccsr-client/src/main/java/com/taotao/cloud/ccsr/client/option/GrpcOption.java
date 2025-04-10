package com.taotao.cloud.ccsr.client.option;


import com.taotao.cloud.ccsr.client.dto.ServerAddress;
import com.taotao.cloud.ccsr.client.utils.ServerAddressConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class GrpcOption extends RequestOption {

	private List<ServerAddress> serverAddresses;

	//private EventType eventType;

	@Override
	public String protocol() {
		return "grpc";
	}

	public void initServers(List<String> addresses) {
		this.serverAddresses = ServerAddressConverter.convert(addresses);
		if (CollectionUtils.isEmpty(serverAddresses)) {
			throw new IllegalArgumentException("Invalid params: the server address is empty");
		}
	}

}
