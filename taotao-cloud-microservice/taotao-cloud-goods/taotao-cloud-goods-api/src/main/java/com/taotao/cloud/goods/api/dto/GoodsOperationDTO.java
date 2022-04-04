package com.taotao.cloud.goods.api.dto;

import com.taotao.cloud.goods.api.enums.GoodsTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 商品编辑DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsOperationDTO implements Serializable {

	private static final long serialVersionUID = -509667581371776913L;

	@Schema(hidden = true)
	private String goodsId;

	@Schema(description = "商品价格", required = true)
	@NotNull(message = "商品价格不能为空")
	@Min(value = 0, message = "商品价格不能为负数")
	@Max(value = 99999999, message = "商品价格不能超过99999999")
	private BigDecimal price;

	@Schema(description = "分类path")
	private String categoryPath;

	@Schema(description = "店铺分类id", required = true)
	@Size(max = 200, message = "选择了太多店铺分类")
	private String storeCategoryPath;

	@Schema(description = "品牌id")
	@Min(value = 0, message = "品牌值不正确")
	private String brandId;

	@Schema(description = "商品名称", required = true)
	@NotEmpty(message = "商品名称不能为空")
	@Length(max = 50, message = "商品名称不能超过50个字符")
	private String goodsName;

	@Schema(description = "详情")
	private String intro;

	@Schema(description = "商品移动端详情")
	private String mobileIntro;

	@Schema(description = "库存")
	@Min(value = 0, message = "库存不能为负数")
	@Max(value = 99999999, message = "库存不能超过99999999")
	private Integer quantity;

	@Schema(description = "是否立即发布")
	private Boolean release;

	@Schema(description = "是否是推荐商品")
	private Boolean recommend;

	@Schema(description = "商品参数")
	private List<GoodsParamsDTO> goodsParamsDTOList;

	@Schema(description = "商品图片")
	private List<String> goodsGalleryList;

	@Schema(description = "运费模板id,不需要运费模板时值是0", required = true)
	@NotNull(message = "运费模板不能为空，没有运费模板时，传值0")
	@Min(value = 0, message = "运费模板值不正确")
	private String templateId;

	@Schema(description = "sku列表")
	@Valid
	private List<Map<String, Object>> skuList;

	@Schema(description = "卖点")
	private String sellingPoint;

	@Schema(description = "销售模式", required = true)
	private String salesModel;

	@Schema(description = "是否有规格", hidden = true)
	private String haveSpec;

	@Schema(description = "销售模式", required = true)
	private String goodsUnit;

	@Schema(description = "商品描述")
	private String info;

	@Schema(description = "是否重新生成sku数据")
	private Boolean regeneratorSkuFlag = true;

	/**
	 * @see GoodsTypeEnum
	 */
	@Schema(description = "商品类型")
	//@EnumValue(strValues = {"PHYSICAL_GOODS", "VIRTUAL_GOODS", "E_COUPON"}, message = "商品类型参数值错误")
	private String goodsType;

	/**
	 * 商品视频
	 */
	@Schema(description = "商品视频")
	private String goodsVideo;

	public String getGoodsName() {
		//对商品对名称做一个极限处理。这里没有用xss过滤是因为xss过滤为全局过滤，影响很大。
		// 业务中，全局代码中只有商品名称不能拥有英文逗号，是由于商品名称存在一个数据库联合查询，结果要根据逗号分组
		return goodsName.replace(",", "");
	}
}
