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

package com.taotao.cloud.payment.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.payment.biz.entity.RefundLog;
import com.taotao.cloud.payment.biz.mapper.RefundLogMapper;
import com.taotao.cloud.payment.biz.service.RefundLogService;
import org.springframework.stereotype.Service;

/**
 * 退款日志 业务实现
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-05-30 16:47:38
 */
@Service
public class RefundLogServiceImpl extends ServiceImpl<RefundLogMapper, RefundLog> implements RefundLogService {

    @Override
    public RefundLog queryByAfterSaleSn(String sn) {
        return this.getOne(new LambdaUpdateWrapper<RefundLog>().eq(RefundLog::getAfterSaleNo, sn));
    }
}
