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

package com.taotao.cloud.order.application.converter;

import com.taotao.cloud.order.sys.model.dto.order.OrderComplaintDTO;
import com.taotao.cloud.order.application.model.entity.order.OrderComplaint;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/11/11 14:42
 */
public interface OrderComplainConvertTest {


    public static OrderComplaint convert(OrderComplaintDTO orderComplaintDTO){


		if (orderComplaintDTO == null) {
			return null;
		}
		OrderComplaint orderComplaint = new OrderComplaint();
// Not mapped TO fields:
// complainTopic
// content
// images
// complainStatus
// appealContent
// appealTime
// appealImages
// orderSn
// orderTime
// goodsName
// goodsId
// skuId
// goodsPrice
// goodsImage
// num
// freightPrice
// orderPrice
// logisticsNo
// storeId
// storeName
// memberId
// memberName
// consigneeName
// consigneeAddressPath
// consigneeMobile
// arbitrationResult
// createTime
// createBy
// updateTime
// updateBy
// version
// delFlag
// id
// entityClass
// Not mapped FROM fields:
// complainTopic
// content
// images
// orderSn
// goodsId
// skuId
		return orderComplaint;
	}
}
