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

package com.taotao.cloud.order.biz.model.convert;

import com.taotao.cloud.order.api.model.vo.aftersale.AfterSaleVO;
import com.taotao.cloud.order.biz.model.entity.aftersale.AfterSale;
import com.taotao.cloud.stock.api.model.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * IAfterSaleMapStruct
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-07 20:55:46
 */
public interface AfterSaleConvertTest {

	public static List<AfterSaleVO> convert(List<AfterSale> userDTOList) {

		if (userDTOList == null) {
			return Collections.emptyList();
		}
		List<AfterSaleVO> afterSaleVOList = new ArrayList<>();
		for (AfterSale afterSale : userDTOList) {
			afterSaleVOList.add(toAfterSaleVO(afterSale));
		}
		return afterSaleVOList;
	}

	static AfterSaleVO toAfterSaleVO(AfterSale afterSale) {
		if (afterSale == null) {
			return null;
		}
		AfterSaleVO afterSaleVO = new AfterSaleVO();
// Not mapped TO fields:
// id
// sn
// orderSn
// orderItemSn
// tradeSn
// memberId
// memberName
// storeId
// storeName
// goodsId
// skuId
// num
// goodsImage
// goodsName
// specs
// flowPrice
// reason
// problemDesc
// afterSaleImage
// serviceType
// serviceStatus
// refundWay
// accountType
// bankAccountNumber
// bankAccountName
// bankDepositName
// auditRemark
// payOrderNo
// applyRefundPrice
// actualRefundPrice
// refundPoint
// refundTime
// mLogisticsNo
// mLogisticsCode
// mLogisticsName
// mDeliverTime
// Not mapped FROM fields:
// sn
// orderSn
// orderItemSn
// tradeSn
// memberId
// memberName
// storeId
// storeName
// goodsId
// skuId
// num
// goodsImage
// goodsName
// specs
// flowPrice
// reason
// problemDesc
// afterSaleImage
// serviceType
// serviceStatus
// refundWay
// accountType
// bankAccountNumber
// bankAccountName
// bankDepositName
// auditRemark
// payOrderNo
// applyRefundPrice
// actualRefundPrice
// refundPoint
// refundTime
// logisticsNo
// logisticsCode
// logisticsName
// deliverTime
// createTime
// createBy
// updateTime
// updateBy
// version
// delFlag
// id
// entityClass
		return afterSaleVO;
	}
}
