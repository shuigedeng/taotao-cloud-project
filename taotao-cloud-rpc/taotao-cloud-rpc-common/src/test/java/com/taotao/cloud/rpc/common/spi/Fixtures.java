package com.taotao.cloud.rpc.common.spi;

/**
 * ExtensionLoader 测试夹具：全部为包级私有类型，与 {@link ExtensionLoader} 同包，
 * 因此 {@code Class.newInstance()} 可以正常实例化。
 * <p>
 * 每个接口对应 {@code src/test/resources/META-INF/ttc-rpc/<接口全名>} 一个资源文件。
 */
final class Fixtures {

    private Fixtures() {}
}
