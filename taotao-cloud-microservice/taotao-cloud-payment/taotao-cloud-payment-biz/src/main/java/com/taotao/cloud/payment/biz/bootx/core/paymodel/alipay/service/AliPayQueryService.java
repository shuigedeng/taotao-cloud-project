package com.taotao.cloud.payment.biz.bootx.core.paymodel.alipay.service;

import com.taotao.cloud.payment.biz.bootx.core.paymodel.alipay.dao.AlipayConfigManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
* 支付宝查询服务
* @author xxm
* @date 2021/4/20
*/
@Slf4j
@Service
@RequiredArgsConstructor
public class AliPayQueryService {
    private final AlipayConfigManager alipayConfigManager;
    

}
