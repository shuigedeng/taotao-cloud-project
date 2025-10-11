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

package com.taotao.cloud.sys.biz.supports.aggregate;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.data.mybatis.mybatisplus.aggregate.AggregateQueries;
import com.taotao.boot.data.mybatis.mybatisplus.aggregate.PaginationDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AggregateTestController {
	/**
	 * 聚合查询
	 * {
	 *   "queries": {
	 *       "phone":"16688880818"
	 *   },
	 *   "pagination": {
	 *     "page": 1,
	 *     "size": 3
	 *   },
	 *   "condition":{
	 *       "city":"南市"
	 *   },
	 *   "fuzzyQueries": {
	 *       "phone": "166",
	 *       "address": "CCC写字楼"
	 *   },
	 *   "sortField": "id",
	 *   "sortType": 1
	 * }
	 *
	 * @param aggregate 聚合查询对象
	 */
	@PostMapping("/get")
	public Result<List<Address>> get(@RequestBody AggregateQueries<AddressQueries, AddressCondition, AddressFuzzyQueries> aggregate) {
		if (!aggregate.hasPagination()) {
			return Result.fail("");
		}
		PaginationDTO pagination = aggregate.getPagination();
		QueryWrapper<Address> wrapper = AggregateQueries.splicingAggregateQueries(new QueryWrapper<>(), aggregate);
		Page<Address> page = new Page<>(pagination.getPage(), pagination.getSize());
//		return Result.ok(addressService.page(page, wrapper).getRecords());
		return Result.success(new ArrayList<>());
	}


}
