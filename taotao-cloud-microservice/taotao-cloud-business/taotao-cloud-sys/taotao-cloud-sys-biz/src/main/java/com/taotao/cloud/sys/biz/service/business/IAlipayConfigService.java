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

package com.taotao.cloud.sys.biz.service.business;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.sys.biz.model.vo.alipay.TradeVO;
import com.taotao.cloud.sys.biz.model.entity.config.AlipayConfig;

/**
 * ali支付配置服务
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-25 14:24:04
 */
public interface IAlipayConfigService extends IService<AlipayConfig> {

    /**
     * 处理来自PC的交易请求
     *
     * @param alipay 支付宝配置
     * @param trade 交易详情
     * @return String
     * @throws Exception 异常
     */
    String toPayAsPc(AlipayConfig alipay, TradeVO trade) throws Exception;

    /**
     * 处理来自手机网页的交易请求
     *
     * @param alipay 支付宝配置
     * @param trade 交易详情
     * @return String
     * @throws Exception 异常
     */
    String toPayAsWeb(AlipayConfig alipay, TradeVO trade) throws Exception;

    /**
     * 查询配置
     *
     * @return 配置信息
     */
    AlipayConfig find();

    /**
     * 更新配置
     *
     * @param alipayConfig 支付宝配置
     * @return 是否完成更新
     */
    Boolean update(AlipayConfig alipayConfig);
}
