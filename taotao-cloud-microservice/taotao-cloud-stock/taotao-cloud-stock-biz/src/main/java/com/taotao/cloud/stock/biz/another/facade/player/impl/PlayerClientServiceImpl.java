package com.taotao.cloud.stock.biz.another.facade.player.impl;

import com.taotao.cloud.ddd.biz.client.player.dto.PlayerQueryResultDTO;
import com.taotao.cloud.ddd.biz.client.player.service.PlayerClientService;
import com.taotao.cloud.ddd.biz.domain.player.domain.PlayerQueryResultDomain;
import com.taotao.cloud.ddd.biz.domain.player.service.PlayerDomainService;
import com.taotao.cloud.ddd.biz.facade.player.adapter.PlayerFacadeAdapter;
import jakarta.annotation.Resource;

/**
 * 本层可以引用applicationService，也可以引用domainService，因为对于类似查询等简单业务场景，没有多领域聚合，可以直接使用领域服务。
 */
public class PlayerClientServiceImpl implements PlayerClientService {

	@Resource
	private PlayerDomainService playerDomainService;
	@Resource
	private PlayerFacadeAdapter playerFacadeAdapter;

	@Override
	public ResultDTO<PlayerQueryResultDTO> queryById(String playerId) {
		PlayerQueryResultDomain resultDomain = playerDomainService.queryPlayerById(playerId);
		if (null == resultDomain) {
			return ResultCommonDTO.success();
		}
		PlayerQueryResultDTO result = playerFacadeAdapter.convertQuery(resultDomain);
		return ResultCommonDTO.success(result);
	}
}

