package com.taotao.cloud.goods.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.goods.api.enums.GoodsTypeEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 商品表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 17:21:04
 */
@Entity
@Table(name = Goods.TABLE_NAME)
@TableName(Goods.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = Goods.TABLE_NAME, comment = "商品表")
public class Goods extends BaseSuperEntity<Goods, Long> {

	public static final String TABLE_NAME = "tt_goods";
	/**
	 * 商品名称
	 */
	@Column(name = "goods_name", nullable = false, columnDefinition = "varchar(64) not null comment '商品名称'")
	private String goodsName;

	/**
	 * 商品价格
	 */
	@Column(name = "price", nullable = false, columnDefinition = "decimal(10,2) not null comment '商品价格'")
	private BigDecimal price;

	/**
	 * 品牌id
	 */
	@Column(name = "brand_id", nullable = false, columnDefinition = "varchar(64) not null comment '品牌id'")
	private String brandId;

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
	 * 商品卖点太长，不能超过60个字符
	 */
	@Column(name = "selling_point", nullable = false, columnDefinition = "varchar(64) not null comment '商品卖点'")
	private String sellingPoint;

	/**
	 * 上架状态
	 */
	@Column(name = "market_enable", nullable = false, columnDefinition = "varchar(64) not null comment '上架状态'")
	private String marketEnable;

	/**
	 * 详情
	 */
	@Column(name = "intro", nullable = false, columnDefinition = "varchar(64) not null comment '详情'")
	private String intro;

	/**
	 * 购买数量
	 */
	@Column(name = "buy_count", nullable = false, columnDefinition = "int not null comment '购买数量'")
	private Integer buyCount;

	/**
	 * 库存
	 */
	@Column(name = "quantity", nullable = false, columnDefinition = "int not null comment '库存'")
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
	@Column(name = "comment_num", columnDefinition = "int comment '评论数量'")
	private Integer commentNum;

	/**
	 * 卖家id
	 */
	@Column(name = "store_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员卖家id'")
	private String storeId;

	/**
	 * 卖家名字
	 */
	@Column(name = "store_name", nullable = false, columnDefinition = "varchar(64) not null comment '卖家名字'")
	private String storeName;

	/**
	 * 运费模板id
	 */
	@Column(name = "template_id", nullable = false, columnDefinition = "varchar(64) not null comment '运费模板id'")
	private String templateId;

	/**
	 * 审核状态
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
	@Column(name = "self_operated", nullable = false, columnDefinition = "boolean not null comment '是否自营'")
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
	 * 商品海报id
	 */
	@Column(name = "poster_pic_id", columnDefinition = "bigint comment '商品海报id'")
	private Long posterPicId;

	/**
	 * 商品类型
	 *
	 * @see GoodsTypeEnum
	 */
	@Column(name = "goods_type", nullable = false, columnDefinition = "varchar(64) not null comment '商品类型'")
	private String goodsType;

	/**
	 * 商品参数json
	 */
	@Column(name = "params", nullable = false, columnDefinition = "varchar(64) not null comment '商品参数json'")
	private String params;

	//public Goods() {
	//}
	//
	//public Goods(GoodsOperationDTO goodsOperationDTO) {
	//	this.goodsName = goodsOperationDTO.getGoodsName();
	//	this.categoryPath = goodsOperationDTO.getCategoryPath();
	//	this.storeCategoryPath = goodsOperationDTO.getStoreCategoryPath();
	//	this.brandId = goodsOperationDTO.getBrandId();
	//	this.templateId = goodsOperationDTO.getTemplateId();
	//	this.recommend = goodsOperationDTO.getRecommend();
	//	this.sellingPoint = goodsOperationDTO.getSellingPoint();
	//	this.salesModel = goodsOperationDTO.getSalesModel();
	//	this.goodsUnit = goodsOperationDTO.getGoodsUnit();
	//	this.intro = goodsOperationDTO.getIntro();
	//	this.mobileIntro = goodsOperationDTO.getMobileIntro();
	//	this.goodsVideo = goodsOperationDTO.getGoodsVideo();
	//	this.price = goodsOperationDTO.getPrice();
	//	if (goodsOperationDTO.getGoodsParamsDTOList() != null
	//		&& goodsOperationDTO.getGoodsParamsDTOList().isEmpty()) {
	//		this.params = JSONUtil.toJsonStr(goodsOperationDTO.getGoodsParamsDTOList());
	//	}
	//	//如果立即上架则
	//	this.marketEnable =
	//		Boolean.TRUE.equals(goodsOperationDTO.getRelease()) ? GoodsStatusEnum.UPPER.name()
	//			: GoodsStatusEnum.DOWN.name();
	//	this.goodsType = goodsOperationDTO.getGoodsType();
	//	this.grade = 100D;
	//
	//	//循环sku，判定sku是否有效
	//	for (Map<String, Object> sku : goodsOperationDTO.getSkuList()) {
	//		//判定参数不能为空
	//		if (sku.get("sn") == null) {
	//			throw new BusinessException(ResultEnum.GOODS_SKU_SN_ERROR);
	//		}
	//		if (StringUtil.isEmpty(sku.get("price").toString())
	//			|| Convert.toDouble(sku.get("price")) <= 0) {
	//			throw new BusinessException(ResultEnum.GOODS_SKU_PRICE_ERROR);
	//		}
	//		if (StringUtil.isEmpty(sku.get("cost").toString())
	//			|| Convert.toDouble(sku.get("cost")) <= 0) {
	//			throw new BusinessException(ResultEnum.GOODS_SKU_COST_ERROR);
	//		}
	//		//虚拟商品没有重量字段
	//		if (sku.containsKey("weight") && (StringUtil.isEmpty(sku.get("weight").toString())
	//			|| Convert.toDouble(sku.get("weight").toString()) < 0)) {
	//			throw new BusinessException(ResultEnum.GOODS_SKU_WEIGHT_ERROR);
	//		}
	//		if (StringUtil.isEmpty(sku.get("quantity").toString())
	//			|| Convert.toInt(sku.get("quantity").toString()) < 0) {
	//			throw new BusinessException(ResultEnum.GOODS_SKU_QUANTITY_ERROR);
	//		}
	//
	//	}
	//}
	//
	//public String getIntro() {
	//	if (CharSequenceUtil.isNotEmpty(intro)) {
	//		return HtmlUtil.unescape(intro);
	//	}
	//	return intro;
	//}
	//
	//public String getMobileIntro() {
	//	if (CharSequenceUtil.isNotEmpty(mobileIntro)) {
	//		return HtmlUtil.unescape(mobileIntro);
	//	}
	//	return mobileIntro;
	//}


	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public String getCategoryPath() {
		return categoryPath;
	}

	public void setCategoryPath(String categoryPath) {
		this.categoryPath = categoryPath;
	}

	public String getGoodsUnit() {
		return goodsUnit;
	}

	public void setGoodsUnit(String goodsUnit) {
		this.goodsUnit = goodsUnit;
	}

	public String getSellingPoint() {
		return sellingPoint;
	}

	public void setSellingPoint(String sellingPoint) {
		this.sellingPoint = sellingPoint;
	}

	public String getMarketEnable() {
		return marketEnable;
	}

	public void setMarketEnable(String marketEnable) {
		this.marketEnable = marketEnable;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public Integer getBuyCount() {
		return buyCount;
	}

	public void setBuyCount(Integer buyCount) {
		this.buyCount = buyCount;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getGrade() {
		return grade;
	}

	public void setGrade(BigDecimal grade) {
		this.grade = grade;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getSmall() {
		return small;
	}

	public void setSmall(String small) {
		this.small = small;
	}

	public String getOriginal() {
		return original;
	}

	public void setOriginal(String original) {
		this.original = original;
	}

	public String getStoreCategoryPath() {
		return storeCategoryPath;
	}

	public void setStoreCategoryPath(String storeCategoryPath) {
		this.storeCategoryPath = storeCategoryPath;
	}

	public Integer getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(Integer commentNum) {
		this.commentNum = commentNum;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getIsAuth() {
		return isAuth;
	}

	public void setIsAuth(String isAuth) {
		this.isAuth = isAuth;
	}

	public String getAuthMessage() {
		return authMessage;
	}

	public void setAuthMessage(String authMessage) {
		this.authMessage = authMessage;
	}

	public String getUnderMessage() {
		return underMessage;
	}

	public void setUnderMessage(String underMessage) {
		this.underMessage = underMessage;
	}

	public Boolean getSelfOperated() {
		return selfOperated;
	}

	public void setSelfOperated(Boolean selfOperated) {
		this.selfOperated = selfOperated;
	}

	public String getMobileIntro() {
		return mobileIntro;
	}

	public void setMobileIntro(String mobileIntro) {
		this.mobileIntro = mobileIntro;
	}

	public String getGoodsVideo() {
		return goodsVideo;
	}

	public void setGoodsVideo(String goodsVideo) {
		this.goodsVideo = goodsVideo;
	}

	public Boolean getRecommend() {
		return recommend;
	}

	public void setRecommend(Boolean recommend) {
		this.recommend = recommend;
	}

	public String getSalesModel() {
		return salesModel;
	}

	public void setSalesModel(String salesModel) {
		this.salesModel = salesModel;
	}

	public Long getPosterPicId() {
		return posterPicId;
	}

	public void setPosterPicId(Long posterPicId) {
		this.posterPicId = posterPicId;
	}

	public String getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}
}
