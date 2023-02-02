package com.taotao.cloud.stock.biz.another.facade.player.adapter;

import com.taotao.cloud.stock.biz.another.client.player.dto.PlayerQueryResultDTO;
import com.taotao.cloud.stock.biz.another.domain.player.domain.PlayerQueryResultDomain;

public class PlayerFacadeAdapter {

	// domain -> dto
	public PlayerQueryResultDTO convertQuery(PlayerQueryResultDomain domain) {
		if (null == domain) {
			return null;
		}
		PlayerQueryResultDTO result = new PlayerQueryResultDTO();
		result.setPlayerId(domain.getPlayerId());
		result.setPlayerName(domain.getPlayerName());
		result.setHeight(domain.getHeight());
		result.setWeight(domain.getWeight());
		if (null != domain.getGamePerformance()) {
			GamePerformanceDTO performance = convertGamePerformance(domain.getGamePerformance());
			result.setGamePerformanceDTO(performance);
		}
		return result;
	}
}

