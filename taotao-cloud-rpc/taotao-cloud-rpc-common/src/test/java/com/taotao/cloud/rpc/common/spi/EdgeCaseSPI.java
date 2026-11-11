package com.taotao.cloud.rpc.common.spi;

/** 资源文件里指向一个不实现本接口的类（java.lang.String）。 */
@SPI("alpha")
interface BadAssignSPI {
}

/** 资源文件里指向一个不存在的类。 */
@SPI("ghost")
interface MissingClassSPI {
}

/** 有 @SPI（且 value 为空串）但没有任何资源文件。 */
@SPI
interface NoResourceSPI {
}

/** 完全没有 @SPI 注解的接口。 */
interface PlainInterface {
}
