package com.taotao.cloud.stock.biz.another.application.player.listener;

import com.taotao.cloud.ddd.biz.domain.game.service.GameDomainService;
import com.taotao.cloud.ddd.biz.domain.player.event.PlayerUpdateEvent;

// 比赛领域监听运动员变更事件
public class PlayerUpdateListener {

	@Resource
	private GameDomainService gameDomainService;

	@PostConstruct
	public void init() {
		EventBusManager.register(this);
	}

	@Subscribe
	public void listen(PlayerUpdateEvent event) {
		// 更新比赛计划
		gameDomainService.updateGameSchedule();
	}
}
