package com.taotao.cloud.web.base.request;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.taotao.cloud.common.base.StrPool;
import com.taotao.cloud.common.utils.AntiSqlFilterUtils;
import com.taotao.cloud.data.mybatis.plus.entity.Entity;
import com.taotao.cloud.data.mybatis.plus.entity.SuperEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 分页参数
 */
@Schema(name = "PageParams", description = "分页参数")
public class PageParams<T> {

	@NotNull(message = "查询对象model不能为空")
	@Schema(description = "查询参数", example = "10", required = true)
	private T model;

	@Schema(description = "每页显示条数，默认10", example = "10", required = true)
	@NotNull(message = "每页数据显示数量不能为空")
	@Min(value = 5)
	@Max(value = 100)
	private long size = 10;

	@Schema(description = "当前第几页，默认1", example = "1", required = true)
	@NotNull(message = "当前页显示数量不能为空")
	@Min(value = 0)
	@Max(value = Integer.MAX_VALUE)
	private long current = 1;

	@Schema(description = "排序,默认createTime", allowableValues = "id,createTime,updateTime", example = "id")
	private String sort = SuperEntity.FIELD_ID;

	@Schema(description = "排序规则, 默认descending", allowableValues = "descending,ascending", example = "descending")
	private String order = "descending";

	@Schema(description = "扩展参数")
	private Map<String, Object> extra = new HashMap<>(16);

	/**
	 * 支持多个字段排序，用法： eg.1, 参数：{order:"name,id", order:"descending,ascending" }。 排序： name desc, id asc
	 * eg.2, 参数：{order:"name", order:"descending,ascending" }。 排序： name desc eg.3,
	 * 参数：{order:"name,id", order:"descending" }。 排序： name desc
	 *
	 * @return 分页对象
	 */
	@JsonIgnore
	public <E> IPage<E> buildPage() {
		PageParams params = this;
		//没有排序参数
		if (StrUtil.isEmpty(params.getSort())) {
			return new Page(params.getCurrent(), params.getSize());
		}

		Page<E> page = new Page(params.getCurrent(), params.getSize());

		List<OrderItem> orders = new ArrayList<>();
		String[] sortArr = StrUtil.splitToArray(params.getSort(), StrPool.COMMA.charAt(0));
		String[] orderArr = StrUtil.splitToArray(params.getOrder(), StrPool.COMMA.charAt(0));

		int len = Math.min(sortArr.length, orderArr.length);
		for (int i = 0; i < len; i++) {
			String humpSort = sortArr[i];
			// 简单的 驼峰 转 下划线
			String underlineSort = StrUtil.toUnderlineCase(humpSort);

			// 除了 create_time 和 updateTime 都过滤sql关键字
			if (!StrUtil.equalsAny(humpSort, SuperEntity.CREATE_TIME, Entity.UPDATE_TIME)) {
				underlineSort = AntiSqlFilterUtils.getSafeValue(underlineSort);
			}

			orders.add(
				StrUtil.equalsAny(orderArr[i], "ascending", "ascend") ? OrderItem.asc(underlineSort)
					: OrderItem.desc(underlineSort));
		}

		page.setOrders(orders);

		return page;
	}

	/**
	 * 计算当前分页偏移量
	 */
	@JsonIgnore
	public long offset() {
		long current = this.current;
		if (current <= 1L) {
			return 0L;
		}
		return (current - 1) * this.size;
	}

	@JsonIgnore
	public PageParams<T> put(String key, Object value) {
		if (this.extra == null) {
			this.extra = new HashMap<>(16);
		}
		this.extra.put(key, value);
		return this;
	}

	@JsonIgnore
	public PageParams<T> putAll(Map<String, Object> extra) {
		if (this.extra == null) {
			this.extra = new HashMap<>(16);
		}
		this.extra.putAll(extra);
		return this;
	}

	public PageParams() {
	}

	public PageParams(T model, long size, long current, String sort, String order,
		Map<String, Object> extra) {
		this.model = model;
		this.size = size;
		this.current = current;
		this.sort = sort;
		this.order = order;
		this.extra = extra;
	}

	public T getModel() {
		return model;
	}

	public void setModel(T model) {
		this.model = model;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public long getCurrent() {
		return current;
	}

	public void setCurrent(long current) {
		this.current = current;
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

	public Map<String, Object> getExtra() {
		return extra;
	}

	public void setExtra(Map<String, Object> extra) {
		this.extra = extra;
	}

	public static <T> PageParamsBuilder<T> builder() {
		return new PageParamsBuilder<>();
	}

	public static final class PageParamsBuilder<T> {

		private T model;
		private long size = 10;
		private long current = 1;
		private String sort = SuperEntity.FIELD_ID;
		private String order = "descending";
		private Map<String, Object> extra = new HashMap<>(16);

		private PageParamsBuilder() {
		}

		public static PageParamsBuilder aPageParams() {
			return new PageParamsBuilder();
		}

		public PageParamsBuilder model(T model) {
			this.model = model;
			return this;
		}

		public PageParamsBuilder size(long size) {
			this.size = size;
			return this;
		}

		public PageParamsBuilder current(long current) {
			this.current = current;
			return this;
		}

		public PageParamsBuilder sort(String sort) {
			this.sort = sort;
			return this;
		}

		public PageParamsBuilder order(String order) {
			this.order = order;
			return this;
		}

		public PageParamsBuilder extra(Map<String, Object> extra) {
			this.extra = extra;
			return this;
		}

		public PageParams build() {
			PageParams pageParams = new PageParams();
			pageParams.setModel(model);
			pageParams.setSize(size);
			pageParams.setCurrent(current);
			pageParams.setSort(sort);
			pageParams.setOrder(order);
			pageParams.setExtra(extra);
			return pageParams;
		}
	}
}
