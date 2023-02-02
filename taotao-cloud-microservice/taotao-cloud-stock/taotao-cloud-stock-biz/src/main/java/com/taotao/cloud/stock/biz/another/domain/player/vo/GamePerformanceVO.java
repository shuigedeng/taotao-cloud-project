package com.taotao.cloud.stock.biz.another.domain.player.vo;

import com.taotao.cloud.ddd.biz.util.validate.BizValidator;
import com.taotao.cloud.stock.biz.another.client.base.error.BizException;

// 比赛表现值对象
public class GamePerformanceVO implements BizValidator {

	// 跑动距离
	private Double runDistance;
	// 传球成功率
	private Double passSuccess;
	// 进球数
	private Integer scoreNum;

	@Override
	public void validate() {
		if (null == runDistance) {
			throw new BizException(ErrorCodeBizEnum.ILLEGAL_ARGUMENT);
		}
		if (null == passSuccess) {
			throw new BizException(ErrorCodeBizEnum.ILLEGAL_ARGUMENT);
		}
		if (Double.compare(passSuccess, 100) > 0) {
			throw new BizException(ErrorCodeBizEnum.ILLEGAL_ARGUMENT);
		}
		if (null == runDistance) {
			throw new BizException(ErrorCodeBizEnum.ILLEGAL_ARGUMENT);
		}
		if (null == scoreNum) {
			throw new BizException(ErrorCodeBizEnum.ILLEGAL_ARGUMENT);
		}
	}
}
