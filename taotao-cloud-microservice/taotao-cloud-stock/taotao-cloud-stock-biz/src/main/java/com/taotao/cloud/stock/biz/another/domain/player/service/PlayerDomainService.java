package com.taotao.cloud.stock.biz.another.domain.player.service;

import com.taotao.cloud.ddd.biz.client.base.error.BizException;
import com.taotao.cloud.ddd.biz.domain.player.adapter.PlayerDomainAdapter;
import com.taotao.cloud.ddd.biz.domain.player.domain.PlayerUpdateDomain;
import com.taotao.cloud.ddd.biz.domain.player.event.PlayerMessageSender;
import com.taotao.cloud.ddd.biz.infrastructure.player.entity.PlayerEntity;
import com.taotao.cloud.ddd.biz.integration.user.proxy.UserClientProxy;
import jakarta.annotation.Resource;

// 领域服务
public class PlayerDomainService {

	@Resource
	private UserClientProxy userClientProxy;
	@Resource
	private PlayerRepository playerEntityMapper;
	@Resource
	private PlayerDomainAdapter playerDomainAdapter;
	@Resource
	private PlayerMessageSender playerMessageSender;

	public boolean updatePlayer(PlayerUpdateDomain player) {
		AssertUtil.notNull(player, new BizException(ErrorCodeBizEnum.ILLEGAL_ARGUMENT));
		player.validate();

		// 更新运动员信息
		PlayerEntity entity = playerDomainAdapter.convertUpdate(player);
		playerEntityMapper.updateById(entity);

		// 发送更新消息
		playerMessageSender.sendPlayerUpdatemessage(player);

		// 查询用户信息
		UserSimpleBaseInfoVO userInfo = userClientProxy.getUserInfo(
			player.getMaintainInfo().getUpdator());
		log.info("updatePlayer maintainInfo={}", JacksonUtil.bean2Json(userInfo));
		return true;
	}
}

