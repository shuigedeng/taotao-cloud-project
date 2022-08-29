package com.taotao.cloud.store.api.web.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.common.utils.date.DateUtils;
import com.taotao.cloud.store.api.enums.StoreStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 店铺搜索参数VO
 *
 * 
 * @since 2020-03-07 17:02:05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "店铺搜索参数VO")
public class StorePageQuery extends PageParam {

	@Serial
	private static final long serialVersionUID = 6916054310764833369L;

	@Schema(description = "会员名称")
	private String memberName;

	@Schema(description = "店铺名称")
	private String storeName;
	/**
	 * @see StoreStatusEnum
	 */
	@Schema(description = "店铺状态")
	private String storeDisable;

	@Schema(description = "开始时间")
	private String startDate;

	@Schema(description = "结束时间")
	private String endDate;

	public <T> QueryWrapper<T> queryWrapper() {
	    QueryWrapper<T> queryWrapper = new QueryWrapper<>();
	    if (StringUtils.isNotEmpty(storeName)) {
	        queryWrapper.like("store_name", storeName);
	    }
	    if (StringUtils.isNotEmpty(memberName)) {
	        queryWrapper.like("member_name", memberName);
	    }
	    if (StringUtils.isNotEmpty(storeDisable)) {
	        queryWrapper.eq("store_disable", storeDisable);
	    } else {
	        queryWrapper.eq("store_disable", StoreStatusEnum.OPEN.name()).or().eq("store_disable", StoreStatusEnum.CLOSED.name());
	    }
	    //按时间查询
	    if (StringUtils.isNotEmpty(startDate)) {
	        queryWrapper.ge("create_time", DateUtils.parse(startDate));
	    }
	    if (StringUtils.isNotEmpty(endDate)) {
	        queryWrapper.le("create_time", DateUtils.parse(endDate));
	    }
	    return queryWrapper;
	}
}
