/*
 * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.common.model;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.taotao.cloud.common.utils.common.AntiSqlFilterUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

/**
 * 基础分页查询
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:09:09
 */
@Schema(name = "PageQuery", description = "通用分页查询Query")
public record PageQuery<QueryDTO>(

	/**
	 * 当前第几页
	 */
	@Schema(description = "当前第几页，默认1", example = "1", required = true)
	@NotNull(message = "当前页显示数量不能为空")
	@Min(value = 0)
	@Max(value = Integer.MAX_VALUE)
	Integer currentPage,

	/**
	 * 每页显示条数
	 */
	@Schema(description = "每页显示条数，默认10", example = "10", required = true)
	@NotNull(message = "每页数据显示数量不能为空")
	@Min(value = 5)
	@Max(value = 100)
	Integer pageSize,

	/**
	 * 查询参数
	 */
	@Schema(description = "查询参数对象")
	QueryDTO query) implements Serializable {

	@Serial
	private static final long serialVersionUID = -2483306509077581330L;

	/**
	 * 支持多个字段排序，用法： eg.1, 参数：{order:"name,id", order:"desc,asc" }。 排序： name desc, id asc eg.2,
	 * 参数：{order:"name", order:"desc,asc" }。 排序： name desc eg.3, 参数：{order:"name,id", order:"desc"
	 * }。 排序： name desc
	 *
	 * @return IPage对象
	 * @since 2021-09-02 21:19:05
	 */
	@JsonIgnore
	public <T> IPage<T> buildMpPage() {
		PageQuery<QueryDTO> params = this;
		QueryDTO query = params.query();

		com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> page = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(
			params.currentPage(), params.pageSize());
		if (query instanceof BaseQuery baseQuery) {
			//没有排序参数
			if (CollectionUtil.isEmpty(baseQuery.sortQuery())) {
				return page;
			}

			List<OrderItem> orders = new ArrayList<>();
			baseQuery.sortQuery().forEach(sortDTO -> {
				String filed = sortDTO.filed();
				String order = sortDTO.order();
				// 驼峰转下划线
				String underlineSort = StrUtil.toUnderlineCase(filed);
				// 除了 createTime 和 updateTime 都过滤sql关键字
				if (!StrUtil.equalsAny(filed, "createTime", "updateTime")) {
					underlineSort = AntiSqlFilterUtil.getSafeValue(underlineSort);
				}

				if (StrUtil.equalsAny(order, "asc")) {
					orders.add(OrderItem.asc(underlineSort));
				} else {
					orders.add(OrderItem.desc(underlineSort));
				}
			});
			page.setOrders(orders);
		}

		return page;
	}

	/**
	 * 构造JpaPage
	 *
	 * @return Pageable对象
	 * @since 2022-03-28 11:24:59
	 */
	@JsonIgnore
	public Pageable buildJpaPage() {
		PageQuery<QueryDTO> params = this;
		QueryDTO query = params.query();
		if (query instanceof BaseQuery baseQuery) {
			if (CollectionUtil.isEmpty(baseQuery.sortQuery())) {
				return PageRequest.of(params.currentPage(), params.pageSize());
			}

			List<Order> orders = new ArrayList<>();
			baseQuery.sortQuery().forEach(sortDTO -> {
				String filed = sortDTO.filed();
				String order = sortDTO.order();
				// 驼峰转下划线
				String underlineSort = StrUtil.toUnderlineCase(filed);
				// 除了 createTime 和 updateTime 都过滤sql关键字
				if (!StrUtil.equalsAny(filed, "createTime", "updateTime")) {
					underlineSort = AntiSqlFilterUtil.getSafeValue(underlineSort);
				}

				if (StrUtil.equalsAny(order, "asc")) {
					orders.add(Order.asc(underlineSort));
				} else {
					orders.add(Order.desc(underlineSort));
				}
			});
			return PageRequest.of(params.currentPage(), params.pageSize(), Sort.by(orders));
		}
		return PageRequest.of(params.currentPage(), params.pageSize());
	}

	/**
	 * offset
	 *
	 * @return offset
	 * @since 2022-03-28 11:24:49
	 */
	@JsonIgnore
	public long offset() {
		long current = this.currentPage;
		if (current <= 1L) {
			return 0L;
		}
		return (current - 1) * this.pageSize;
	}
}
