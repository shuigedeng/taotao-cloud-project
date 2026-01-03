/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.sys.biz.service.business.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.sys.biz.model.vo.alipay.TradeVO;
import com.taotao.cloud.sys.biz.mapper.IAlipayConfigMapper;
import com.taotao.cloud.sys.biz.model.entity.config.AlipayConfig;
import com.taotao.cloud.sys.biz.service.business.IAlipayConfigService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

// 默认不使用缓存
// import org.springframework.cache.annotation.CacheConfig;
// import org.springframework.cache.annotation.CacheEvict;
// import org.springframework.cache.annotation.Cacheable;

/**
 * AlipayConfigServiceImpl
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
@Service
// @CacheConfig(cacheNames = "alipayConfig")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AlipayConfigServiceImpl extends ServiceImpl<IAlipayConfigMapper, AlipayConfig> implements
        IAlipayConfigService {

    @Override
    public String toPayAsPc( AlipayConfig alipay, TradeVO trade ) throws Exception {

        // if (alipay.getId() == null) {
        //    throw new BadRequestException("请先添加相应配置，再操作");
        // }
        //
        // AlipayClient alipayClient = new DefaultAlipayClient(alipay.getGatewayUrl(),
        // alipay.getAppId(), alipay.getPrivateKey(), alipay.getFormat(), alipay.getCharset(),
        // alipay.getPublicKey(), alipay.getSignType());
        //
        //// 创建API对应的request(电脑网页版)
        // AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        //
        //// 订单完成后返回的页面和异步通知地址
        // request.setReturnUrl(alipay.getReturnUrl());
        // request.setNotifyUrl(alipay.getNotifyUrl());
        //// 填充订单参数
        // request.setBizContent("{" +
        //        "    \"out_trade_no\":\"" + trade.getOutTradeNo() + "\"," +
        //        "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
        //        "    \"total_amount\":" + trade.getTotalAmount() + "," +
        //        "    \"subject\":\"" + trade.getSubject() + "\"," +
        //        "    \"body\":\"" + trade.getBody() + "\"," +
        //        "    \"extend_params\":{" +
        //        "    \"sys_service_provider_id\":\"" + alipay.getSysServiceProviderId() + "\"" +
        //        "    }" +
        //        "  }");//填充业务参数
        //// 调用SDK生成表单, 通过GET方式，口可以获取url
        // return alipayClient.pageExecute(request, "GET").getBody();
        return "";
    }

    @Override
    public String toPayAsWeb( AlipayConfig alipay, TradeVO trade ) throws Exception {
        // if (alipay.getId() == null) {
        //    throw new BadRequestException("请先添加相应配置，再操作");
        // }
        // AlipayClient alipayClient = new DefaultAlipayClient(alipay.getGatewayUrl(),
        // alipay.getAppId(), alipay.getPrivateKey(), alipay.getFormat(), alipay.getCharset(),
        // alipay.getPublicKey(), alipay.getSignType());
        //
        // double money = Double.parseDouble(trade.getTotalAmount());
        // double maxMoney = 5000;
        // if (money <= 0 || money >= maxMoney) {
        //    throw new BadRequestException("测试金额过大");
        // }
        //// 创建API对应的request(手机网页版)
        // AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
        // request.setReturnUrl(alipay.getReturnUrl());
        // request.setNotifyUrl(alipay.getNotifyUrl());
        // request.setBizContent("{" +
        //        "    \"out_trade_no\":\"" + trade.getOutTradeNo() + "\"," +
        //        "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
        //        "    \"total_amount\":" + trade.getTotalAmount() + "," +
        //        "    \"subject\":\"" + trade.getSubject() + "\"," +
        //        "    \"body\":\"" + trade.getBody() + "\"," +
        //        "    \"extend_params\":{" +
        //        "    \"sys_service_provider_id\":\"" + alipay.getSysServiceProviderId() + "\"" +
        //        "    }" +
        //        "  }");
        // return alipayClient.pageExecute(request, "GET").getBody();
        return "";
    }

    @Override
    //    @Cacheable(key = "'1'")
    public AlipayConfig find() {
        AlipayConfig alipayConfig = this.list().get(0);
        return alipayConfig;
    }

    @Override
    //    @CachePut(key = "'1'")
    @Transactional(rollbackFor = Exception.class)
    public Boolean update( AlipayConfig alipayConfig ) {
        return this.save(alipayConfig);
    }
}
