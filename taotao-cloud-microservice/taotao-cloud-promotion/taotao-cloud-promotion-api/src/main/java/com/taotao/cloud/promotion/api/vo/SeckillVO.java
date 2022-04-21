package com.taotao.cloud.promotion.api.vo;

import com.taotao.cloud.promotion.api.enums.SeckillApplyStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 秒杀活动视图对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
//public class SeckillVO extends Seckill {
public class SeckillVO {

	private static final long serialVersionUID = 2891461638257152270L;

	/**
	 * @see SeckillApplyStatusEnum
	 */
	@Schema(description = "报名状态")
	private String seckillApplyStatus;

	/**
	 * 当前秒杀活动下所有的秒杀申请信息
	 */
	//private List<SeckillApply> seckillApplyList;

}
