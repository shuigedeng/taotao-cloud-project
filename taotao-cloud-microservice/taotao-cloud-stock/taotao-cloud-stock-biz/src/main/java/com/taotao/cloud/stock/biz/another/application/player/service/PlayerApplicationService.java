package com.taotao.cloud.stock.biz.another.application.player.service;

import com.taotao.cloud.ddd.biz.domain.player.service.PlayerDomainService;

// 运动员应用服务
public class PlayerApplicationService {

	@Resource
	private LogDomainService logDomainService;
	@Resource
	private PlayerDomainService playerDomainService;
	@Resource
	private PlayerApplicationAdapter playerApplicationAdapter;

	public boolean updatePlayer(PlayerUpdateAgg agg) {
		// 运动员领域
		boolean result = playerDomainService.updatePlayer(agg.getPlayer());
		// 日志领域
		LogReportDomain logDomain = playerApplicationAdapter.convert(
			agg.getPlayer().getPlayerName());
		logDomainService.log(logDomain);
		return result;
	}
}

