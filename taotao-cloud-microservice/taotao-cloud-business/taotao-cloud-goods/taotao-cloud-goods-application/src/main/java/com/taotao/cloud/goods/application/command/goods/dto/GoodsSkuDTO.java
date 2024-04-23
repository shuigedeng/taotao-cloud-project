package com.taotao.cloud.goods.application.command.goods.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.taotao.cloud.goods.api.enums.GoodsAuthEnum;
import com.taotao.cloud.goods.api.enums.GoodsSalesModeEnum;
import com.taotao.cloud.goods.api.enums.GoodsStatusEnum;
import com.taotao.cloud.goods.api.enums.GoodsTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * @since 2022/6/13
 **/
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class GoodsSkuDTO {

	private String id;
	private String createBy;
	private Date createTime;
	private String updateBy;
	private Date updateTime;
	private Boolean deleteFlag;

    private static final long serialVersionUID = 6600436187015048097L;
	@Schema(description = "商品id")
	private String goodsId;

	@Schema(description = "规格信息json", hidden = true)
	@JsonIgnore
	private String specs;

	@Schema(description = "规格信息")
	private String simpleSpecs;

	@Schema(description = "配送模版id")
	private String freightTemplateId;

	@Schema(description = "是否是促销商品")
	private Boolean promotionFlag;

	@Schema(description = "促销价")
	private Double promotionPrice;

	@Schema(description = "商品名称")
	private String goodsName;

	@Length(max = 30, message = "商品规格编号太长，不能超过30个字符")
	@Schema(description = "商品编号")
	private String sn;

	@Schema(description = "品牌id")
	private String brandId;

	@Schema(description = "分类path")
	private String categoryPath;

	@Schema(description = "计量单位")
	private String goodsUnit;

	@Schema(description = "卖点")
	private String sellingPoint;

	@Schema(description = "重量")
	@Max(value = 99999999, message = "重量不能超过99999999")
	private Double weight;
	/**
	 * @see GoodsStatusEnum
	 */
	@Schema(description = "上架状态")
	private String marketEnable;

	@Schema(description = "商品详情")
	private String intro;

	@Max(value = 99999999, message = "价格不能超过99999999")
	@Schema(description = "商品价格")
	private Double price;

	@Max(value = 99999999, message = "成本价格99999999")
	@Schema(description = "成本价格")
	private Double cost;

	@Schema(description = "浏览数量")
	private Integer viewCount;

	@Schema(description = "购买数量")
	private Integer buyCount;

	@Max(value = 99999999, message = "库存不能超过99999999")
	@Schema(description = "库存")
	private Integer quantity;

	@Schema(description = "商品好评率")
	private Double grade;

	@Schema(description = "缩略图路径")
	private String thumbnail;

	@Schema(description = "大图路径")
	private String big;

	@Schema(description = "小图路径")
	private String small;

	@Schema(description = "原图路径")
	private String original;

	@Schema(description = "店铺分类id")
	private String storeCategoryPath;

	@Schema(description = "评论数量")
	private Integer commentNum;

	@Schema(description = "卖家id")
	private String storeId;

	@Schema(description = "卖家名字")
	private String storeName;

	@Schema(description = "运费模板id")
	private String templateId;

	/**
	 * @see GoodsAuthEnum
	 */
	@Schema(description = "审核状态")
	private String authFlag;

	@Schema(description = "审核信息")
	private String authMessage;

	@Schema(description = "下架原因")
	private String underMessage;

	@Schema(description = "是否自营")
	private Boolean selfOperated;

	@Schema(description = "商品移动端详情")
	private String mobileIntro;

	@Schema(description = "商品视频")
	private String goodsVideo;

	@Schema(description = "是否为推荐商品", requiredMode = Schema.RequiredMode.REQUIRED)
	private Boolean recommend;

	/**
	 * @see GoodsSalesModeEnum
	 */
	@Schema(description = "销售模式", requiredMode = Schema.RequiredMode.REQUIRED)
	private String salesModel;
	/**
	 * @see GoodsTypeEnum
	 */
	@Schema(description = "商品类型", requiredMode = Schema.RequiredMode.REQUIRED)
	private String goodsType;

    @Schema(description = "商品参数json")
    private String params;

}
