package com.taotao.cloud.common.serializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;

/**
 * The Data Serializer Factory.
 */
public enum SerializerFactory {

	SF;

	Map<String, Serializer> serializerMap = new HashMap<>();

	SerializerFactory() {
		ServiceLoader<Serializer> serializers = ServiceLoader.load(Serializer.class);
		for (Serializer serializer : serializers) {
			if (Objects.nonNull(serializer)) {
				String name = serializer.name();
				if (serializerMap.containsKey(name)) {
					throw new RuntimeException("序列化方式已存在: " + name);
				}

				serializerMap.put(name, serializer);
			}
		}
	}

	/**
	 * The get compress @SPI value is {#name} extension.
	 *
	 * @param name
	 * @return
	 */
	public Serializer getExtension(String name) {
		return serializerMap.get(name);
	}

}
