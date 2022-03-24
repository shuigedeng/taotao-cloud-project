package com.taotao.cloud.stock.biz.infrastructure.external;

/**
 * 生成Token外部接口
 *
 * @author shuigedeng
 * @date 2021-04-23
 **/
public interface TokenGeneratorExternalService {

    /**
     * 生成token
     *
     * @return
     */
    String generateValue();
}
