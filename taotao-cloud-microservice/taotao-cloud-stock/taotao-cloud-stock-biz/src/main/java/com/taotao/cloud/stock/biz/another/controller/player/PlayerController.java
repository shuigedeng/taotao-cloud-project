package com.taotao.cloud.stock.biz.another.controller.player;

import com.taotao.cloud.ddd.biz.client.base.result.ResultDTO;
import com.taotao.cloud.ddd.biz.client.player.dto.PlayerCreateDTO;
import com.taotao.cloud.ddd.biz.client.player.dto.PlayerQueryResultDTO;
import com.taotao.cloud.ddd.biz.client.player.dto.PlayerUpdateDTO;
import com.taotao.cloud.ddd.biz.client.player.service.PlayerClientService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/player")
public class PlayerController {

	@Resource
	private PlayerClientService playerClientService;

	@PostMapping("/add")
	public ResultDTO<Boolean> add(@RequestHeader("test-login-info") String loginUserId,
		@RequestBody PlayerCreateDTO dto) {
		dto.setCreator(loginUserId);
		ResultCommonDTO<Boolean> resultDTO = playerClientService.addPlayer(dto);
		return resultDTO;
	}

	@PostMapping("/update")
	public ResultDTO<Boolean> update(@RequestHeader("test-login-info") String loginUserId,
		@RequestBody PlayerUpdateDTO dto) {
		dto.setUpdator(loginUserId);
		ResultCommonDTO<Boolean> resultDTO = playerClientService.updatePlayer(dto);
		return resultDTO;
	}

	@GetMapping("/{playerId}/query")
	public ResultDTO<PlayerQueryResultDTO> queryById(
		@RequestHeader("test-login-info") String loginUserId,
		@PathVariable("playerId") String playerId) {
		ResultCommonDTO<PlayerQueryResultDTO> resultDTO = playerClientService.queryById(playerId);
		return resultDTO;
	}
}
