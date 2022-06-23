package com.taotao.cloud.promotion.api.web.vo;

import com.taotao.cloud.promotion.api.enums.SeckillApplyStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 秒杀活动视图对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeckillVO {

	private static final long serialVersionUID = 2891461638257152270L;

	/**
	 * 报名截至时间
	 */
	private LocalDateTime applyEndTime;
	/**
	 * 申请规则
	 */
	private String seckillRule;
	/**
	 * 开启几点场 例如：6，8，12
	 */
	private String hours;

	/**
	 * 已参与此活动的商家id集合 商家id集合以逗号分隔
	 */
	private String storeIds;
	/**
	 * 商品数量
	 */
	private Integer goodsNum;

	// **********************************************************************************

	/**
	 * @see SeckillApplyStatusEnum
	 */
	@Schema(description = "报名状态")
	private String seckillApplyStatus;

	/**
	 * 当前秒杀活动下所有的秒杀申请信息
	 */
	private List<SeckillApplyVO> seckillApplyList;

}
