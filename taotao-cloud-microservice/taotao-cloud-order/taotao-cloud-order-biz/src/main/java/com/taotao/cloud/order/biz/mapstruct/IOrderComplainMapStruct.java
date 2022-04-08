/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.order.biz.mapstruct;

import com.taotao.cloud.order.api.bo.order_info.OrderBO;
import com.taotao.cloud.order.api.dto.order.OrderComplaintDTO;
import com.taotao.cloud.order.api.vo.order_info.OrderVO;
import com.taotao.cloud.order.biz.entity.order.OrderComplaint;
import com.taotao.cloud.order.biz.entity.order.OrderInfo;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/11/11 14:42
 */
@Mapper(builder = @Builder(disableBuilder = true),
	unmappedSourcePolicy = ReportingPolicy.IGNORE,
	unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IOrderComplainMapStruct {

	IOrderComplainMapStruct INSTANCE = Mappers.getMapper(IOrderComplainMapStruct.class);

	OrderComplaint orderComplaintDTOToOrderComplaint(OrderComplaintDTO orderComplaintDTO);

}
