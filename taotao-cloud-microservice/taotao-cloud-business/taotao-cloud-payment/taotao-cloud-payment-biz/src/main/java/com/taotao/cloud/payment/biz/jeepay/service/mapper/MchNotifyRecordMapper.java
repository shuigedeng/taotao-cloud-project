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

package com.taotao.cloud.payment.biz.jeepay.service.mapper;

import com.taotao.cloud.payment.biz.jeepay.core.entity.MchNotifyRecord;
import org.apache.ibatis.annotations.Param;

/**
 * 商户通知表 Mapper 接口
 *
 * @author [mybatis plus generator]
 * @since 2021-04-27
 */
public interface MchNotifyRecordMapper extends BaseSuperMapper<MchNotifyRecord> {

    Integer updateNotifyResult(
            @Param("notifyId") Long notifyId, @Param("state") Byte state, @Param("resResult") String resResult);

    /*
     * 功能描述: 更改为通知中 & 增加允许重发通知次数
     * @param notifyId
     * @Author: terrfly
     * @Date: 2021/6/21 17:38
     */
    Integer updateIngAndAddNotifyCountLimit(@Param("notifyId") Long notifyId);
}
