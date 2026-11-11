package com.taotao.cloud.rpc.common.spi;

/** 正常扩展点：三个实现，默认 alpha。 */
@SPI("alpha")
interface DemoSPI {
    String mark();
}

class DemoAlpha implements DemoSPI {
    public String mark() { return "alpha"; }
}

class DemoBeta implements DemoSPI {
    public String mark() { return "beta"; }
}

class DemoGamma implements DemoSPI {
    public String mark() { return "gamma"; }
}
