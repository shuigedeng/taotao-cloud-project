package com.taotao.cloud.order.biz.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 订单明细表
 *
 * @author shuigedeng
 * @since 2020/4/30 15:42
 */
@Entity
@TableName(OrderItem.TABLE_NAME)
@Table(name = OrderItem.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = OrderItem.TABLE_NAME, comment = "订单明细表")
public class OrderItem extends BaseSuperEntity<OrderItem,Long> {

	public static final String TABLE_NAME = "order_item";

	/**
	 * 订单子编码
	 */
	@Column(name = "item_code", unique = true, nullable = false, columnDefinition = "varchar(32) not null comment '订单子编码'")
	private String itemCode;

	/**
	 * 商品SPU ID
	 */
	@Column(name = "product_spu_id", nullable = false, columnDefinition = "bigint not null comment '商品SPU ID'")
	private Long productSpuId;

	/**
	 * 商品SPU_CODE
	 */
	@Column(name = "product_spu_code", nullable = false, columnDefinition = "varchar(32) not null comment '商品SPU CODE'")
	private String productSpuCode;

	/**
	 * 商品SPU名称
	 */
	@Column(name = "product_spu_name", nullable = false, columnDefinition = "varchar(32) not null comment '商品SPU名称'")
	private String productSpuName;

	/**
	 * 商品SKU ID
	 */
	@Column(name = "product_sku_id", nullable = false, columnDefinition = "bigint not null comment '商品SKU ID'")
	private Long productSkuId;

	/**
	 * 商品SKU 规格名称
	 */
	@Column(name = "product_sku_name", nullable = false, columnDefinition = "varchar(255) not null comment '商品SKU 规格名称'")
	private String productSkuName;

	/**
	 * 商品单价
	 */
	@Column(name = "product_price", nullable = false, columnDefinition = "decimal(10,2) not null default 0 comment '商品单价'")
	private BigDecimal productPrice = BigDecimal.ZERO;

	/**
	 * 购买数量
	 */
	@Column(name = "num", columnDefinition = "int not null default 1 comment '购买数量'")
	private Integer num = 1;

	/**
	 * 合计金额
	 */
	@Column(name = "sum_amount", columnDefinition = "decimal(10,2) not null default 0 comment '合计金额'")
	private BigDecimal sumAmount = BigDecimal.ZERO;

	/**
	 * 商品主图
	 */
	@Column(name = "product_pic_url", columnDefinition = "varchar(255) comment '商品主图'")
	private String productPicUrl;

	/**
	 * 供应商id
	 */
	@Column(name = "supplier_id", nullable = false, columnDefinition = "bigint not null comment '供应商id'")
	private Long supplierId;

	/**
	 * 供应商名称
	 */
	@Column(name = "supplier_name", nullable = false, columnDefinition = "varchar(255) not null comment '供应商名称'")
	private String supplierName;

	/**
	 * 超时退货期限
	 */
	@Column(name = "refund_time", columnDefinition = "int default 0 comment '超时退货期限'")
	private Integer refundTime;

	/**
	 * 退货数量
	 */
	@Column(name = "reject_count", columnDefinition = "int not null default 0 comment '退货数量'")
	private Integer rejectCount = 0;

	/**
	 * 商品类型 0 普通商品 1 秒杀商品
	 */
	@Column(name = "type", columnDefinition = "int not null default 0 comment '0-普通商品 1-秒杀商品'")
	private Integer type = 0;

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public Long getProductSpuId() {
		return productSpuId;
	}

	public void setProductSpuId(Long productSpuId) {
		this.productSpuId = productSpuId;
	}

	public String getProductSpuCode() {
		return productSpuCode;
	}

	public void setProductSpuCode(String productSpuCode) {
		this.productSpuCode = productSpuCode;
	}

	public String getProductSpuName() {
		return productSpuName;
	}

	public void setProductSpuName(String productSpuName) {
		this.productSpuName = productSpuName;
	}

	public Long getProductSkuId() {
		return productSkuId;
	}

	public void setProductSkuId(Long productSkuId) {
		this.productSkuId = productSkuId;
	}

	public String getProductSkuName() {
		return productSkuName;
	}

	public void setProductSkuName(String productSkuName) {
		this.productSkuName = productSkuName;
	}

	public BigDecimal getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(BigDecimal productPrice) {
		this.productPrice = productPrice;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public BigDecimal getSumAmount() {
		return sumAmount;
	}

	public void setSumAmount(BigDecimal sumAmount) {
		this.sumAmount = sumAmount;
	}

	public String getProductPicUrl() {
		return productPicUrl;
	}

	public void setProductPicUrl(String productPicUrl) {
		this.productPicUrl = productPicUrl;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public Integer getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(Integer refundTime) {
		this.refundTime = refundTime;
	}

	public Integer getRejectCount() {
		return rejectCount;
	}

	public void setRejectCount(Integer rejectCount) {
		this.rejectCount = rejectCount;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public OrderItem() {
	}

	public OrderItem(String itemCode, Long productSpuId, String productSpuCode,
		String productSpuName, Long productSkuId, String productSkuName,
		BigDecimal productPrice, Integer num, BigDecimal sumAmount, String productPicUrl,
		Long supplierId, String supplierName, Integer refundTime, Integer rejectCount,
		Integer type) {
		this.itemCode = itemCode;
		this.productSpuId = productSpuId;
		this.productSpuCode = productSpuCode;
		this.productSpuName = productSpuName;
		this.productSkuId = productSkuId;
		this.productSkuName = productSkuName;
		this.productPrice = productPrice;
		this.num = num;
		this.sumAmount = sumAmount;
		this.productPicUrl = productPicUrl;
		this.supplierId = supplierId;
		this.supplierName = supplierName;
		this.refundTime = refundTime;
		this.rejectCount = rejectCount;
		this.type = type;
	}

	public static OrderItemBuilder builder() {
		return new OrderItemBuilder();
	}

	public static final class OrderItemBuilder {

		private String itemCode;
		private Long productSpuId;
		private String productSpuCode;
		private String productSpuName;
		private Long productSkuId;
		private String productSkuName;
		private BigDecimal productPrice = BigDecimal.ZERO;
		private Integer num = 1;
		private BigDecimal sumAmount = BigDecimal.ZERO;
		private String productPicUrl;
		private Long supplierId;
		private String supplierName;
		private Integer refundTime;
		private Integer rejectCount = 0;
		private Integer type = 0;
		private Long id;
		private Long createBy;
		private Long lastModifiedBy;
		private LocalDateTime createTime;
		private LocalDateTime lastModifiedTime;
		private int version = 1;
		private Boolean delFlag = false;

		private OrderItemBuilder() {
		}

		public static OrderItemBuilder anOrderItem() {
			return new OrderItemBuilder();
		}

		public OrderItemBuilder itemCode(String itemCode) {
			this.itemCode = itemCode;
			return this;
		}

		public OrderItemBuilder productSpuId(Long productSpuId) {
			this.productSpuId = productSpuId;
			return this;
		}

		public OrderItemBuilder productSpuCode(String productSpuCode) {
			this.productSpuCode = productSpuCode;
			return this;
		}

		public OrderItemBuilder productSpuName(String productSpuName) {
			this.productSpuName = productSpuName;
			return this;
		}

		public OrderItemBuilder productSkuId(Long productSkuId) {
			this.productSkuId = productSkuId;
			return this;
		}

		public OrderItemBuilder productSkuName(String productSkuName) {
			this.productSkuName = productSkuName;
			return this;
		}

		public OrderItemBuilder productPrice(BigDecimal productPrice) {
			this.productPrice = productPrice;
			return this;
		}

		public OrderItemBuilder num(Integer num) {
			this.num = num;
			return this;
		}

		public OrderItemBuilder sumAmount(BigDecimal sumAmount) {
			this.sumAmount = sumAmount;
			return this;
		}

		public OrderItemBuilder productPicUrl(String productPicUrl) {
			this.productPicUrl = productPicUrl;
			return this;
		}

		public OrderItemBuilder supplierId(Long supplierId) {
			this.supplierId = supplierId;
			return this;
		}

		public OrderItemBuilder supplierName(String supplierName) {
			this.supplierName = supplierName;
			return this;
		}

		public OrderItemBuilder refundTime(Integer refundTime) {
			this.refundTime = refundTime;
			return this;
		}

		public OrderItemBuilder rejectCount(Integer rejectCount) {
			this.rejectCount = rejectCount;
			return this;
		}

		public OrderItemBuilder type(Integer type) {
			this.type = type;
			return this;
		}

		public OrderItemBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public OrderItemBuilder createBy(Long createBy) {
			this.createBy = createBy;
			return this;
		}

		public OrderItemBuilder lastModifiedBy(Long lastModifiedBy) {
			this.lastModifiedBy = lastModifiedBy;
			return this;
		}

		public OrderItemBuilder createTime(LocalDateTime createTime) {
			this.createTime = createTime;
			return this;
		}

		public OrderItemBuilder lastModifiedTime(LocalDateTime lastModifiedTime) {
			this.lastModifiedTime = lastModifiedTime;
			return this;
		}

		public OrderItemBuilder version(int version) {
			this.version = version;
			return this;
		}

		public OrderItemBuilder delFlag(Boolean delFlag) {
			this.delFlag = delFlag;
			return this;
		}

		public OrderItem build() {
			OrderItem orderItem = new OrderItem();
			orderItem.setItemCode(itemCode);
			orderItem.setProductSpuId(productSpuId);
			orderItem.setProductSpuCode(productSpuCode);
			orderItem.setProductSpuName(productSpuName);
			orderItem.setProductSkuId(productSkuId);
			orderItem.setProductSkuName(productSkuName);
			orderItem.setProductPrice(productPrice);
			orderItem.setNum(num);
			orderItem.setSumAmount(sumAmount);
			orderItem.setProductPicUrl(productPicUrl);
			orderItem.setSupplierId(supplierId);
			orderItem.setSupplierName(supplierName);
			orderItem.setRefundTime(refundTime);
			orderItem.setRejectCount(rejectCount);
			orderItem.setType(type);
			orderItem.setId(id);
			orderItem.setCreatedBy(createBy);
			orderItem.setLastModifiedBy(lastModifiedBy);
			orderItem.setCreateTime(createTime);
			orderItem.setLastModifiedTime(lastModifiedTime);
			orderItem.setVersion(version);
			orderItem.setDelFlag(delFlag);
			return orderItem;
		}
	}
}
