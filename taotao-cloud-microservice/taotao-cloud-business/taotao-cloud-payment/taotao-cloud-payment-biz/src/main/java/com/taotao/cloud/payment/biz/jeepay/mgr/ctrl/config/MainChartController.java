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

package com.taotao.cloud.payment.biz.jeepay.mgr.ctrl.config;

import com.alibaba.fastjson.JSONObject;
import com.taotao.cloud.payment.biz.jeepay.core.model.ApiRes;
import com.taotao.cloud.payment.biz.jeepay.mch.ctrl.CommonCtrl;
import com.taotao.cloud.payment.biz.jeepay.service.impl.PayOrderService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页统计类
 *
 * @author pangxiaoyu
 * @site https://www.jeequan.com
 * @date 2021-06-07 07:15
 */
@Slf4j
@RestController
@RequestMapping("api/mainChart")
public class MainChartController extends CommonCtrl {

    @Autowired
    private PayOrderService payOrderService;

    /**
     * @author: pangxiaoyu
     * @date: 2021/6/7 16:18
     * @describe: 周交易总金额
     */
    @PreAuthorize("hasAuthority('ENT_C_MAIN_PAY_AMOUNT_WEEK')")
    @RequestMapping(value = "/payAmountWeek", method = RequestMethod.GET)
    public ApiRes payAmountWeek() {
        return ApiRes.ok(payOrderService.mainPageWeekCount(null));
    }

    /**
     * @author: pangxiaoyu
     * @date: 2021/6/7 16:18
     * @describe: 商户总数量、服务商总数量、总交易金额、总交易笔数
     */
    @PreAuthorize("hasAuthority('ENT_C_MAIN_NUMBER_COUNT')")
    @RequestMapping(value = "/numCount", method = RequestMethod.GET)
    public ApiRes numCount() {
        JSONObject json = payOrderService.mainPageNumCount(null);
        // 返回数据
        return ApiRes.ok(json);
    }

    /**
     * @author: pangxiaoyu
     * @date: 2021/6/7 16:18
     * @describe: 交易统计
     */
    @PreAuthorize("hasAuthority('ENT_C_MAIN_PAY_COUNT')")
    @RequestMapping(value = "/payCount", method = RequestMethod.GET)
    public ApiRes payCount() {
        // 获取传入参数
        JSONObject paramJSON = getReqParamJSON();
        String createdStart = paramJSON.getString("createdStart");
        String createdEnd = paramJSON.getString("createdEnd");
        List<Map> mapList = payOrderService.mainPagePayCount(null, createdStart, createdEnd);
        // 返回数据
        return ApiRes.ok(mapList);
    }

    /**
     * @author: pangxiaoyu
     * @date: 2021/6/7 16:18
     * @describe: 支付方式统计
     */
    @PreAuthorize("hasAuthority('ENT_C_MAIN_PAY_TYPE_COUNT')")
    @RequestMapping(value = "/payTypeCount", method = RequestMethod.GET)
    public ApiRes payWayCount() {
        JSONObject paramJSON = getReqParamJSON();
        // 开始、结束时间
        String createdStart = paramJSON.getString("createdStart");
        String createdEnd = paramJSON.getString("createdEnd");
        ArrayList arrayResult = payOrderService.mainPagePayTypeCount(null, createdStart, createdEnd);
        return ApiRes.ok(arrayResult);
    }
}
