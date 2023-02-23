package com.taotao.cloud.stock.biz.another.infrastructure.player.mapper;

import com.taotao.cloud.ddd.biz.infrastructure.player.entity.PlayerEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerEntityMapper {

	int insert(PlayerEntity record);

	int updateById(PlayerEntity record);

	PlayerEntity selectById(@Param("playerId") String playerId);
}
