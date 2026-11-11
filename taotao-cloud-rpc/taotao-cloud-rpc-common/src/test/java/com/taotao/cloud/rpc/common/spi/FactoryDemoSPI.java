package com.taotao.cloud.rpc.common.spi;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 专供「ExtensionFactory 那一层到底有没有被加载器问过」用例的扩展点。
 * 只有该用例引用它，因此它的 loader 与实例缓存进入用例时都是冷的。
 */
@SPI("factory")
interface FactoryDemoSPI {
    String mark();
}

class FactoryImpl implements FactoryDemoSPI {
    public String mark() { return "factory"; }
}

/**
 * {@code SpiExtensionFactory} 只用反射的 {@code getBean(String)} 认容器（{@code invokeGetBean}），
 * 所以这里不需要真 Spring —— 它扮演的就是生产代码看到的那一面对。
 * 与 {@code SpiExtensionFactory} 同包，反射调用不会因为可见性被挡。
 */
final class FakeSpiContext {

    private final Map<String, Object> beans = new LinkedHashMap<>();

    FakeSpiContext put(String name, Object bean) {
        beans.put(name, bean);
        return this;
    }

    public Object getBean(String name) {
        return beans.get(name);
    }
}
