package com.taotao.cloud.goods.api.dto;

import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.goods.api.enums.GoodsAuthEnum;
import com.taotao.cloud.goods.api.enums.GoodsStatusEnum;
import com.taotao.cloud.goods.api.enums.GoodsTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.util.Arrays;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 商品查询参数
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-14 21:11:30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsPageQuery extends PageParam {

	@Serial
	private static final long serialVersionUID = 2544015852728566887L;

	@Schema(description = "商品编号")
	private Long goodsId;

	@Schema(description = "商品名称")
	private String goodsName;

	@Schema(description = "商品编号")
	private String id;

	@Schema(description = "商家ID")
	private Long storeId;

	@Schema(description = "卖家名字")
	private String storeName;

	@Schema(description = "价格,可以为范围，如10_1000")
	private String price;

	@Schema(description = "分类path")
	private String categoryPath;

	@Schema(description = "店铺分类id")
	private String storeCategoryPath;

	@Schema(description = "是否自营")
	private Boolean selfOperated;

	/**
	 * @see GoodsStatusEnum
	 */
	@Schema(description = "上下架状态")
	private String marketEnable;

	/**
	 * @see GoodsAuthEnum
	 */
	@Schema(description = "审核状态")
	private String authFlag;

	@Schema(description = "库存数量")
	private Integer leQuantity;

	@Schema(description = "库存数量")
	private Integer geQuantity;

	@Schema(description = "是否为推荐商品")
	private Boolean recommend;

	/**
	 * @see GoodsTypeEnum
	 */
	@Schema(description = "商品类型")
	private String goodsType;

	public <T> QueryWrapper<T> queryWrapper() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		if (Objects.nonNull(goodsId)) {
			queryWrapper.eq("goods_id", goodsId);
		}
		if (CharSequenceUtil.isNotEmpty(goodsName)) {
			queryWrapper.like("goods_name", goodsName);
		}
		if (CharSequenceUtil.isNotEmpty(id)) {
			queryWrapper.in("id", Arrays.asList(id.split(",")));
		}
		if (Objects.nonNull(storeId)) {
			queryWrapper.eq("store_id", storeId);
		}
		if (CharSequenceUtil.isNotEmpty(storeName)) {
			queryWrapper.like("store_name", storeName);
		}
		if (CharSequenceUtil.isNotEmpty(categoryPath)) {
			queryWrapper.like("category_path", categoryPath);
		}
		if (CharSequenceUtil.isNotEmpty(storeCategoryPath)) {
			queryWrapper.like("store_category_path", storeCategoryPath);
		}
		if (selfOperated != null) {
			queryWrapper.eq("self_operated", selfOperated);
		}
		if (CharSequenceUtil.isNotEmpty(marketEnable)) {
			queryWrapper.eq("market_enable", marketEnable);
		}
		if (CharSequenceUtil.isNotEmpty(authFlag)) {
			queryWrapper.eq("auth_flag", authFlag);
		}
		if (leQuantity != null) {
			queryWrapper.le("quantity", leQuantity);
		}
		if (geQuantity != null) {
			queryWrapper.ge("quantity", geQuantity);
		}
		if (recommend != null) {
			queryWrapper.le("recommend", recommend);
		}
		if (CharSequenceUtil.isNotEmpty(goodsType)) {
			queryWrapper.eq("goods_type", goodsType);
		}

		queryWrapper.eq("delete_flag", false);
		this.betweenWrapper(queryWrapper);
		return queryWrapper;
	}

	private <T> void betweenWrapper(QueryWrapper<T> queryWrapper) {
		if (CharSequenceUtil.isNotEmpty(price)) {
			String[] s = price.split("_");
			if (s.length > 1) {
				queryWrapper.between("price", s[0], s[1]);
			} else {
				queryWrapper.ge("price", s[0]);
			}
		}
	}


}
