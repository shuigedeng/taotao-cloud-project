package com.taotao.cloud.store.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 结算单VO
 *
 * 
 * @since 2020年3月07日 上午11:04:25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "结算单VO")
public class BillListVO {

	@Schema(description = "账单ID")
	private String id;

	@Schema(description = "账单号")
	private String sn;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	@Schema(description = "结算开始时间")
	private LocalDateTime startTime;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	@Schema(description = "结算结束时间")
	private LocalDateTime endTime;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	@Schema(description = "出账时间")
	private LocalDateTime createTime;

	/**
	 * @see BillStatusEnum
	 */
	@Schema(description = "状态：OUT(已出账),RECON(已对账),PASS(已审核),PAY(已付款)")
	private String billStatus;

	@Schema(description = "店铺名称")
	private String storeName;

	@Schema(description = "最终结算金额")
	private BigDecimal billPrice;
}
