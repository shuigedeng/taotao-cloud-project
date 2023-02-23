package com.taotao.cloud.stock.biz.another.client.player.service;

import com.taotao.cloud.ddd.biz.client.player.dto.PlayerQueryResultDTO;

public interface PlayerClientService {

	public ResultDTO<PlayerQueryResultDTO> queryById(String playerId);
}
