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

package com.taotao.cloud.order.biz.controller.feign;

import com.taotao.cloud.order.api.feign.IFeignOrderApi;
import com.taotao.cloud.order.sys.model.dto.order_info.OrderSaveDTO;
import com.taotao.cloud.order.sys.model.vo.order.OrderDetailVO;
import com.taotao.cloud.order.sys.model.vo.order.OrderVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

/**
 * 移动端-字典API
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-10-09 14:24:19
 */
@Validated
@RestController
@Tag(name = "移动端-字典API", description = "移动端-字典API")
public class FeignOrderController implements IFeignOrderApi {

    @Override
    public OrderVO findOrderInfoByCode(String code) {
        return null;
    }

    @Override
    public OrderVO saveOrder(OrderSaveDTO orderDTO) {
        return null;
    }

    @Override
    public OrderDetailVO queryDetail(String sn) {
        return null;
    }

    @Override
    public Boolean payOrder(String sn, String paymentMethod, String receivableNo) {
        return null;
    }

    @Override
    public OrderVO getBySn(String sn) {
        return null;
    }

    @Override
    public List<OrderVO> getByTradeSn(String sn) {
        return null;
    }
}
