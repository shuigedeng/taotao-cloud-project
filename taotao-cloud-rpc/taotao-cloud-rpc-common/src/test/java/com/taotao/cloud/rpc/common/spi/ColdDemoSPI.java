package com.taotao.cloud.rpc.common.spi;

/**
 * 专供「冷加载器 + @Activate」用例的扩展点。
 * 只有该用例引用它，因此它的 loader 在进入用例时必然是未加载状态。
 */
@SPI("coldA")
interface ColdDemoSPI {
    String mark();
}

@Activate(group = {"consumer"}, order = 20)
class ColdA implements ColdDemoSPI {
    public String mark() { return "coldA"; }
}

@Activate(group = {"consumer", "provider"}, order = 10)
class ColdB implements ColdDemoSPI {
    public String mark() { return "coldB"; }
}
