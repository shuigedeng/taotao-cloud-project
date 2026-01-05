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

package com.taotao.cloud.order.biz.service.business.aftersale.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.order.biz.mapper.aftersale.AfterSaleLogMapper;
import com.taotao.cloud.order.biz.model.entity.aftersale.AfterSaleLog;
import com.taotao.cloud.order.biz.service.business.aftersale.AfterSaleLogService;
import java.util.List;
import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 订单日志业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:49:24
 */
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class AfterSaleLogServiceImpl extends ServiceImpl<AfterSaleLogMapper, AfterSaleLog>
        implements AfterSaleLogService {

    @Override
    public List<AfterSaleLog> getAfterSaleLog(String sn) {
        LambdaQueryWrapper<AfterSaleLog> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(AfterSaleLog::getSn, sn);
        return this.list(queryWrapper);
    }
}
