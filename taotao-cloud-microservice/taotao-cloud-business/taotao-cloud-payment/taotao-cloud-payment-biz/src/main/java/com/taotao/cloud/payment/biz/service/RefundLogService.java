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

package com.taotao.cloud.payment.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.payment.biz.entity.RefundLog;

/**
 * 退款日志 业务层
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-05-30 16:46:59
 */
public interface RefundLogService extends IService<RefundLog> {
    /**
     * 根据售后sn查询退款日志
     *
     * @param sn
     * @return {@link RefundLog }
     * @since 2022-05-30 16:46:59
     */
    RefundLog queryByAfterSaleSn(String sn);
}
