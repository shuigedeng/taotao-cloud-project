package com.taotao.cloud.ccsr.api.event;

import com.taotao.cloud.ccsr.api.grpc.auto.Metadata;

/**
 * @author Spring Cat
 */
public record MetadataChangeEvent(Metadata metadata, EventType type) implements Event {
}
