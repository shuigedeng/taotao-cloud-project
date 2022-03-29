package com.taotao.cloud.core.compress;

import com.taotao.cloud.core.compress.support.SPI;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;


/**
 * The Data Compress Factory.
 */
public enum CompressFactory {
	/**
	 * 压缩
	 */
	CF;
	/**
	 * 压缩map
	 */
	Map<String, Compress> compressMap = new HashMap<>();

	CompressFactory() {
		ServiceLoader<Compress> compresses = ServiceLoader.load(Compress.class);
		for (Compress compress : compresses) {
			SPI spi = compress.getClass().getAnnotation(SPI.class);
			if (spi != null) {
				String name = spi.value();
				if (compressMap.containsKey(name)) {
					throw new RuntimeException("The @SPI value(" + name
							+ ") repeat, for class(" + compress.getClass()
							+ ") and class(" + compressMap.get(name).getClass()
							+ ").");
				}

				compressMap.put(name, compress);
			}
		}
	}

	/**
	 * The get compress @SPI value is {#name} extension.
	 * 
	 * @param name
	 * @return
	 */
	public Compress getExtension(String name) {
		return compressMap.get(name);
	}

}
