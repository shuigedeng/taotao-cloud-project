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

package com.taotao.cloud.payment.biz.jeepay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.payment.biz.jeepay.core.entity.MchDivisionReceiverGroup;
import com.taotao.cloud.payment.biz.jeepay.service.mapper.MchDivisionReceiverGroupMapper;
import org.springframework.stereotype.Service;

/**
 * 分账账号组 服务实现类
 *
 * @author [mybatis plus generator]
 * @since 2021-08-23
 */
@Service
public class MchDivisionReceiverGroupService
        extends ServiceImpl<MchDivisionReceiverGroupMapper, MchDivisionReceiverGroup> {

    /** 根据ID和商户号查询 * */
    public MchDivisionReceiverGroup findByIdAndMchNo(Long groupId, String mchNo) {
        return getOne(
                MchDivisionReceiverGroup.gw()
                        .eq(MchDivisionReceiverGroup::getReceiverGroupId, groupId)
                        .eq(MchDivisionReceiverGroup::getMchNo, mchNo));
    }
}
