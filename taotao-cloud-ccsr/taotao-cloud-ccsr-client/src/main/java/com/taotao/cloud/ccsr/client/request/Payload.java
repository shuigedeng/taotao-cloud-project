package com.taotao.cloud.ccsr.client.request;

import com.taotao.cloud.ccsr.client.listener.ConfigData;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class Payload {

	private String namespace;

	private String group;

	private String tag;

	private String dataId;

	private ConfigData configData;

	private MetadataType type;

	private Map<String, String> ext;

	private Long gmtCreate;

	private Long gmtModified;

	private EventType eventType;

}
