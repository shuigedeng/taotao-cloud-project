package com.taotao.cloud.promotion.api.web.vo.kanjia;

import com.taotao.cloud.promotion.api.enums.KanJiaStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 砍价活动参与实体类
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:24:57
 */
@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class KanjiaActivityBaseVO implements Serializable {

	/**
	 * 砍价商品id
	 */
	private Long kanjiaActivityGoodsId;
	/**
	 * 发起砍价活动会员id
	 */
	private Long memberId;
	/**
	 * 发起砍价活动会员名称
	 */
	private String memberName;
	/**
	 * 剩余购买金额
	 */
	private BigDecimal surplusPrice;
	/**
	 * 砍价最低购买金额
	 */
	private BigDecimal purchasePrice;
	/**
	 * 砍价商品skuId
	 */
	private Long skuId;
	/**
	 * 货品名称
	 */
	private String goodsName;
	/**
	 * 缩略图
	 */
	private String thumbnail;

	/**
	 * 砍价活动状态
	 * @see KanJiaStatusEnum
	 */
	private String status;
}
