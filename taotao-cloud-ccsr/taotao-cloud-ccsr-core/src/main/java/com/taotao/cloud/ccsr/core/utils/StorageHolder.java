package com.taotao.cloud.ccsr.core.utils;

import com.taotao.cloud.ccsr.core.storage.MetadaStorage;
import com.taotao.cloud.ccsr.core.storage.Storage;
import com.taotao.cloud.ccsr.spi.SpiExtensionFactory;

public class StorageHolder {

    @SuppressWarnings("unchecked")
    public static <T extends Storage<?>> T getInstance(String key) {
        Storage<?> extension = SpiExtensionFactory.getExtension(key, Storage.class);
        return (T) extension;
    }

}
