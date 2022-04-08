package com.taotao.cloud.goods.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.goods.api.enums.GoodsAuthEnum;
import com.taotao.cloud.goods.api.enums.GoodsStatusEnum;
import com.taotao.cloud.goods.api.enums.GoodsTypeEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 商品sku表
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = GoodsSku.TABLE_NAME)
@TableName(GoodsSku.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = GoodsSku.TABLE_NAME, comment = "商品sku表")
public class GoodsSku extends BaseSuperEntity<GoodsSku, Long> {

	public static final String TABLE_NAME = "tt_goods_sku";
	/**
	 * 商品id
	 */
	@Column(name = "goods_id", nullable = false, columnDefinition = "varchar(64) not null comment '商品id'")
	private Long goodsId;

	/**
	 * 规格信息json
	 */
	@Column(name = "specs", nullable = false, columnDefinition = "varchar(64) not null comment '规格信息json'")
	private String specs;

	/**
	 * 规格信息
	 */
	@Column(name = "simple_specs", nullable = false, columnDefinition = "varchar(64) not null comment '规格信息'")
	private String simpleSpecs;

	/**
	 * 配送模版id
	 */
	@Column(name = "freight_template_id", nullable = false, columnDefinition = "varchar(64) not null comment '配送模版id'")
	private Long freightTemplateId;

	/**
	 * 是否是促销商品
	 */
	@Column(name = "is_promotion", nullable = false, columnDefinition = "boolean not null comment '是否是促销商品'")
	private Boolean isPromotion;

	/**
	 * 促销价
	 */
	@Column(name = "promotion_price", nullable = false, columnDefinition = "decimal(10,2) not null comment '促销价'")
	private BigDecimal promotionPrice;

	/**
	 * 商品名称
	 */
	@Column(name = "goods_name", nullable = false, columnDefinition = "varchar(64) not null comment '商品名称'")
	private String goodsName;

	/**
	 * 商品编号
	 */
	@Column(name = "sn", nullable = false, columnDefinition = "varchar(64) not null comment '商品编号'")
	private String sn;

	/**
	 * 品牌id
	 */
	@Column(name = "brand_id", nullable = false, columnDefinition = "varchar(64) not null comment '品牌id'")
	private Long brandId;

	/**
	 * 分类path
	 */
	@Column(name = "category_path", nullable = false, columnDefinition = "varchar(64) not null comment '分类path'")
	private String categoryPath;

	/**
	 * 计量单位
	 */
	@Column(name = "goods_unit", nullable = false, columnDefinition = "varchar(64) not null comment '计量单位'")
	private String goodsUnit;

	/**
	 * 卖点
	 */
	@Column(name = "selling_point", nullable = false, columnDefinition = "varchar(64) not null comment '卖点'")
	private String sellingPoint;

	/**
	 * 重量
	 */
	@Column(name = "weight", nullable = false, columnDefinition = "decimal(10,2) not null comment '重量'")
	private BigDecimal weight;

	/**
	 * 上架状态
	 *
	 * @see GoodsStatusEnum
	 */
	@Column(name = "market_enable", nullable = false, columnDefinition = "varchar(64) not null comment '上架状态'")
	private String marketEnable;

	/**
	 * 商品详情
	 */
	@Column(name = "intro", nullable = false, columnDefinition = "varchar(64) not null comment '商品详情'")
	private String intro;

	/**
	 * 商品价格
	 */
	@Column(name = "price", nullable = false, columnDefinition = "decimal(10,2) not null comment '商品价格'")
	private BigDecimal price;

	/**
	 * 成本价格
	 */
	@Column(name = "cost", nullable = false, columnDefinition = "decimal(10,2) not null comment '成本价格'")
	private BigDecimal cost;

	/**
	 * 浏览数量
	 */
	@Column(name = "view_count", nullable = false, columnDefinition = "int not null default 0 comment '浏览数量'")
	private Integer viewCount;

	/**
	 * 购买数量
	 */
	@Column(name = "buy_count", columnDefinition = "int comment '购买数量'")
	private Integer buyCount;

	/**
	 * 库存
	 */
	@Column(name = "quantity", nullable = false, columnDefinition = "int not null default 0 comment '库存'")
	private Integer quantity;

	/**
	 * 商品好评率
	 */
	@Column(name = "grade", nullable = false, columnDefinition = "decimal(10,2) not null comment '商品好评率'")
	private BigDecimal grade;

	/**
	 * 缩略图路径
	 */
	@Column(name = "thumbnail", nullable = false, columnDefinition = "varchar(64) not null comment '缩略图路径'")
	private String thumbnail;

	/**
	 * 大图路径
	 */
	@Column(name = "big", nullable = false, columnDefinition = "varchar(64) not null comment '大图路径'")
	private String big;

	/**
	 * 小图路径
	 */
	@Column(name = "small", nullable = false, columnDefinition = "varchar(64) not null comment '小图路径'")
	private String small;

	/**
	 * 原图路径
	 */
	@Column(name = "original", nullable = false, columnDefinition = "varchar(64) not null comment '原图路径'")
	private String original;

	/**
	 * 店铺分类id
	 */
	@Column(name = "store_category_path", nullable = false, columnDefinition = "varchar(64) not null comment '店铺分类id'")
	private String storeCategoryPath;

	/**
	 * 评论数量
	 */
	@Column(name = "comment_num", nullable = false, columnDefinition = "int not null comment '评论数量'")
	private Integer commentNum;

	/**
	 * 卖家id
	 */
	@Column(name = "store_id", nullable = false, columnDefinition = "varchar(64) not null comment '卖家id'")
	private Long storeId;

	/**
	 * 卖家名字
	 */
	@Column(name = "store_name", nullable = false, columnDefinition = "varchar(64) not null comment '卖家名字'")
	private String storeName;

	/**
	 * 运费模板id
	 */
	@Column(name = "template_id", nullable = false, columnDefinition = "varchar(64) not null comment '运费模板id'")
	private Long templateId;

	/**
	 * 审核状态
	 *
	 * @see GoodsAuthEnum
	 */
	@Column(name = "is_auth", nullable = false, columnDefinition = "varchar(64) not null comment '审核状态'")
	private String isAuth;

	/**
	 * 审核信息
	 */
	@Column(name = "auth_message", nullable = false, columnDefinition = "varchar(64) not null comment '审核信息'")
	private String authMessage;

	/**
	 * 下架原因
	 */
	@Column(name = "under_message", nullable = false, columnDefinition = "varchar(64) not null comment '下架原因'")
	private String underMessage;

	/**
	 * 是否自营
	 */
	@Column(name = "self_operated", nullable = false, columnDefinition = "varchar(64) not null comment '是否自营'")
	private Boolean selfOperated;

	/**
	 * 商品移动端详情
	 */
	@Column(name = "mobile_intro", nullable = false, columnDefinition = "varchar(64) not null comment '商品移动端详情'")
	private String mobileIntro;

	/**
	 * 商品视频
	 */
	@Column(name = "goods_video", nullable = false, columnDefinition = "varchar(64) not null comment '商品视频'")
	private String goodsVideo;

	/**
	 * 是否为推荐商品
	 */
	@Column(name = "recommend", nullable = false, columnDefinition = "boolean not null comment '是否为推荐商品'")
	private Boolean recommend;

	/**
	 * 销售模式
	 */
	@Column(name = "sales_model", nullable = false, columnDefinition = "varchar(64) not null comment '销售模式'")
	private String salesModel;

	/**
	 * 商品类型
	 *
	 * @see GoodsTypeEnum
	 */
	@Column(name = "goods_type", nullable = false, columnDefinition = "varchar(64) not null comment '商品类型'")
	private String goodsType;

	//public BigDecimal getWeight() {
	//	if (weight == null) {
	//		return 0d;
	//	}
	//	return weight;
	//}
	//
	//@Override
	//public Date getUpdateTime() {
	//	if (super.getUpdateTime() == null) {
	//		return new Date(1593571928);
	//	} else {
	//		return super.getUpdateTime();
	//	}
	//}
}
