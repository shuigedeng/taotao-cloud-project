package com.taotao.cloud.goods.api.vo;

import com.taotao.cloud.goods.api.enums.GoodsStatusEnum;
import io.soabase.recordbuilder.core.RecordBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * 商品操作允许的范围
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:34:05
 */
@RecordBuilder
public record GoodsOperateAllowable(
	/**
	 * 上下架状态
	 *
	 * @see GoodsStatusEnum
	 */
	String marketEnable,

	/**
	 * 删除状态 true 已删除 false 未删除
	 */
	Boolean deleteFlag,

	/**
	 * 是否允许下架
	 */
	Boolean allowDown,
	/**
	 * 是否允许放入回收站
	 */
	Boolean allowDelete,
	/**
	 * 是否允许回收站的商品还原
	 */
	Boolean allowReduction,
	/**
	 * 是否允许回收站的商品彻底删除
	 */
	Boolean allowClear,
	/**
	 * 是否允许上架
	 */
	Boolean allowUpper
) implements Serializable {

	@Serial
	private static final long serialVersionUID = 3829199991161122317L;

	//public GoodsOperateAllowable(String marketEnable, Boolean deleteFlag) {
	//	this.marketEnable = marketEnable;
	//	this.deleteFlag = deleteFlag;
	//}


	public Boolean getAllowDown() {
		//上架状态 不在回收站的商品可以下架
		return Objects.equals(marketEnable, GoodsStatusEnum.UPPER.name()) && !deleteFlag;
	}

	public Boolean getAllowReduction() {
		//下架状态 在回收站的商品可以还原
		return Objects.equals(marketEnable, GoodsStatusEnum.DOWN.name()) && deleteFlag;
	}

	public Boolean getAllowClear() {
		//下架状态 在回收站的商品可以彻底删除
		return Objects.equals(marketEnable, GoodsStatusEnum.DOWN.name()) && deleteFlag;
	}

	public Boolean getAllowUpper() {
		//下架状态 未删除的商品可以上架
		return Objects.equals(marketEnable, GoodsStatusEnum.DOWN.name()) && !deleteFlag;
	}

	public Boolean getAllowDelete() {
		//下架状态 未删除的商品可以删除
		return Objects.equals(marketEnable, GoodsStatusEnum.DOWN.name()) && !deleteFlag;
	}

}
