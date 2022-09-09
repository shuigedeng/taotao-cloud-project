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
package com.taotao.cloud.order.biz.convert;

import com.taotao.cloud.order.api.bo.order_info.OrderBO;
import com.taotao.cloud.order.api.model.vo.aftersale.AfterSaleVO;
import com.taotao.cloud.order.api.vo.order_info.OrderVO;
import com.taotao.cloud.order.biz.model.entity.aftersale.AfterSale;
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
@Mapper(
	unmappedSourcePolicy = ReportingPolicy.IGNORE,
	unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AfterSaleConvert {

	AfterSaleConvert INSTANCE = Mappers.getMapper(AfterSaleConvert.class);

	AfterSaleVO afterSaleToAfterSaleVO(AfterSale afterSale);

	List<AfterSaleVO> afterSalesToAfterSaleVOs(List<AfterSale> afterSales);
}