package com.taotao.cloud.stock.biz.another.domain.player.domain;

import com.taotao.cloud.ddd.biz.domain.base.domain.BaseDomain;
import com.taotao.cloud.ddd.biz.domain.base.vo.MaintainUpdateVO;
import com.taotao.cloud.ddd.biz.domain.player.vo.GamePerformanceVO;
import com.taotao.cloud.ddd.biz.util.validate.BizValidator;
import com.taotao.cloud.stock.biz.another.client.base.error.BizException;

// 修改领域对象
public class PlayerUpdateDomain extends BaseDomain implements BizValidator {

	private String playerId;
	private String playerName;
	private Integer height;
	private Integer weight;
	private String updator;
	private Date updatetime;
	private GamePerformanceVO gamePerformance;
	private MaintainUpdateVO maintainInfo;

	@Override
	public void validate() {
		if (StringUtils.isEmpty(playerId)) {
			throw new BizException(ErrorCodeBizEnum.ILLEGAL_ARGUMENT);
		}
		if (StringUtils.isEmpty(playerName)) {
			throw new BizException(ErrorCodeBizEnum.ILLEGAL_ARGUMENT);
		}
		if (null == height) {
			throw new BizException(ErrorCodeBizEnum.ILLEGAL_ARGUMENT);
		}
		if (height > 300) {
			throw new BizException(ErrorCodeBizEnum.ILLEGAL_ARGUMENT);
		}
		if (null == weight) {
			throw new BizException(ErrorCodeBizEnum.ILLEGAL_ARGUMENT);
		}
		if (null != gamePerformance) {
			gamePerformance.validate();
		}
		if (null == maintainInfo) {
			throw new BizException(ErrorCodeBizEnum.ILLEGAL_ARGUMENT);
		}
		maintainInfo.validate();
	}
}

