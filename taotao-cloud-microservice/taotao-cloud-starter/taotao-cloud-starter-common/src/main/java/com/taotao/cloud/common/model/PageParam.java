package com.taotao.cloud.common.model;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.taotao.cloud.common.utils.lang.StringUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询参数
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-28 11:24:11
 */
public class PageParam implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 当前第几页
	 */
	@Schema(description = "当前第几页，默认1", example = "1", required = true)
	@NotNull(message = "当前页显示数量不能为空")
	@Min(value = 0, message = "当前页数不能小于0")
	@Max(value = Integer.MAX_VALUE)
	private Integer currentPage;

	/**
	 * 每页显示条数
	 */
	@Schema(description = "每页显示条数，默认10", example = "10", required = true)
	@NotNull(message = "每页数据显示数量不能为空")
	@Min(value = 5, message = "每页显示条数最小为5条")
	@Max(value = 100, message = "每页显示条数最大为100条")
	private Integer pageSize;

	/**
	 * 排序字段
	 */
	@Schema(description = "排序字段")
	private String sort;

	/**
	 * 排序方式 asc/desc
	 */
	@Schema(description = "排序方式 asc/desc")
	private String order;

	/**
	 * 构造mp分页参数
	 *
	 * @return 分页参数
	 * @since 2022/3/14 13:50
	 */
	public <T> IPage<T> buildMpPage() {
		com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> page = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(
			currentPage, pageSize);

		if (StringUtil.isNotBlank(sort) && StringUtil.isNotBlank(order)) {
			List<OrderItem> orders = new ArrayList<>();
			OrderItem orderItem = "asc".equals(order) ? OrderItem.asc(sort) : OrderItem.desc(sort);
			orders.add(orderItem);
			page.setOrders(orders);
		}

		return page;
	}

	/**
	 * 构造jpa分页参数
	 *
	 * @return 分页参数
	 * @since 2022/3/14 13:53
	 */
	public Pageable buildJpaPage() {
		PageRequest page = PageRequest.of(currentPage, pageSize);

		if (StringUtil.isNotBlank(sort) && StringUtil.isNotBlank(order)) {
			List<Order> orders = new ArrayList<>();
			Order orderItem = "asc".equals(order) ? Order.asc(sort) : Order.desc(sort);
			orders.add(orderItem);
			page = PageRequest.of(currentPage, pageSize, Sort.by(orders));
		}

		return page;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}
}
