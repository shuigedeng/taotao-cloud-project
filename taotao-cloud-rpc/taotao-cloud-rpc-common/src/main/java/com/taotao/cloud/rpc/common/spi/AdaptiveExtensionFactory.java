package com.taotao.cloud.rpc.common.spi;

import com.taotao.cloud.rpc.common.spi.ExtensionFactory;
import com.taotao.cloud.rpc.common.spi.SpiExtensionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 自适应扩展工厂
 * <p>
 * 组合多个工厂，依次尝试获取扩展对象。
 * <p>
 * 当前内置：{@link SpiExtensionFactory}（Spring 容器查找）。
 */
public class AdaptiveExtensionFactory implements ExtensionFactory {

    private static final Logger log = LogManager.getLogger(AdaptiveExtensionFactory.class);

    private final List<ExtensionFactory> factories;

    public AdaptiveExtensionFactory() {
        this.factories = new ArrayList<>();
        this.factories.add(new SpiExtensionFactory());
    }

    @Override
    public <T> T getExtension(Class<?> type, String name) {
        for (ExtensionFactory factory : factories) {
            T ext = factory.getExtension(type, name);
            if (ext != null) {
                return ext;
            }
        }
        return null;
    }
}
