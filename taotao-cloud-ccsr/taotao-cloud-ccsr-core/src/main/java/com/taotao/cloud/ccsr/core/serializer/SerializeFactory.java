package com.taotao.cloud.ccsr.core.serializer;

import com.taotao.cloud.ccsr.spi.SpiExtensionFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SerializeFactory {

    public static final String HESSIAN_INDEX = "Hessian".toLowerCase();

    private static final Map<String, Serializer> SERIALIZER_MAP = new HashMap<>(4);

    public static String DEFAULT_SERIALIZER = HESSIAN_INDEX;

    static {
        List<Serializer> extensions = SpiExtensionFactory.getExtensions(Serializer.class);
        extensions.forEach(item -> SERIALIZER_MAP.put(item.name().toLowerCase(), item));
    }

    public static Serializer getDefault() {
        return SERIALIZER_MAP.get(DEFAULT_SERIALIZER);
    }

    public static Serializer getSerializer(String type) {
        return SERIALIZER_MAP.get(type.toLowerCase());
    }
}
