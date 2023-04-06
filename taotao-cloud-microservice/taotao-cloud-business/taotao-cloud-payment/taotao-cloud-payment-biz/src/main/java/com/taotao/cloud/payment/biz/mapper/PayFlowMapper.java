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

package com.taotao.cloud.payment.biz.mapper;

import com.taotao.cloud.dubbo.biz.entity.PayFlow;
import com.taotao.cloud.payment.api.vo.PayFlowVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author shuigedeng
 * @since 2020/11/11 14:42
 * @version 2022.03
 */
@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PayFlowMapper {

    PayFlowMapper INSTANCE = Mappers.getMapper(PayFlowMapper.class);

    /**
     * payFlow转PayFlowVO
     *
     * @param payFlow payFlow
     * @return com.taotao.cloud.uc.api.vo.user.UserVO
     * @author shuigedeng
     * @since 2020/11/11 14:47
     * @version 2022.03
     */
    PayFlowVO payFlowToPayFlowVO(PayFlow payFlow);
}
