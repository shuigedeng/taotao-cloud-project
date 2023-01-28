package com.taotao.cloud.store.api.model.vo;

import com.taotao.cloud.store.api.enums.StoreStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 店铺VO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "店铺VO")
public class StoreVO {
	private Long id;

	private String nickname;

	private Long memberId;

	private String memberName;

	private String storeName;

	private LocalDateTime storeEndTime;

	/**
	 * @see StoreStatusEnum
	 */
	private String storeDisable;

	private Boolean selfOperated;

	private String storeLogo;

	private String storeCenter;

	private String storeDesc;

	private String storeAddressPath;

	private String storeAddressIdPath;

	private String storeAddressDetail;

	private BigDecimal descriptionScore;

	private BigDecimal serviceScore;

	private BigDecimal deliveryScore;

	private Integer goodsNum;

	private Integer collectionNum;

	private String yzfSign;

	private String yzfMpSign;

	private String merchantEuid;

}

