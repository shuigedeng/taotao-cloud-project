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

import com.taotao.cloud.order.api.dto.aftersale.AfterSaleReasonDTO;
import com.taotao.cloud.order.api.vo.aftersale.AfterSaleLogVO;
import com.taotao.cloud.order.api.vo.aftersale.AfterSaleReasonVO;
import com.taotao.cloud.order.biz.entity.aftersale.AfterSaleLog;
import com.taotao.cloud.order.biz.entity.aftersale.AfterSaleReason;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * IAfterSaleMapStruct
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-07 20:55:46
 */
@Mapper(builder = @Builder(disableBuilder = true),
	unmappedSourcePolicy = ReportingPolicy.IGNORE,
	unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IAfterSaleReasonMapStruct {

	IAfterSaleReasonMapStruct INSTANCE = Mappers.getMapper(IAfterSaleReasonMapStruct.class);

	List<AfterSaleReasonVO> afterSaleReasonsToAfterSaleReasonVOs(List<AfterSaleReason> afterSaleReasonList);
	AfterSaleReasonVO afterSaleReasonToAfterSaleReasonVO(AfterSaleReason afterSaleReason);
	AfterSaleReason afterSaleReasonDTOToAfterSaleReason(AfterSaleReasonDTO afterSaleReasonDTO);

}
