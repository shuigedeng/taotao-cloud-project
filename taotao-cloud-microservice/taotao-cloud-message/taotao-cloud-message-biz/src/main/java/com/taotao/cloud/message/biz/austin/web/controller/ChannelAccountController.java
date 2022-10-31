package com.taotao.cloud.message.biz.austin.web.controller;


import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.web.request.annotation.RequestLogger;
import com.taotao.cloud.message.biz.austin.common.vo.BasicResultVO;
import com.taotao.cloud.message.biz.austin.support.domain.ChannelAccount;
import com.taotao.cloud.message.biz.austin.web.service.ChannelAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 渠道账号管理接口
 *
 * @author 3y
 */
@Slf4j
@RestController
@RequestMapping("/account")
@Tag(name = "pc端-渠道账号管理接口API", description = "pc端-渠道账号管理接口API")
public class ChannelAccountController {

	@Autowired
	private ChannelAccountService channelAccountService;


	/**
	 * 如果Id存在，则修改
	 * 如果Id不存在，则保存
	 */
	@Operation(summary = "保存数据", description = "保存数据")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping("/save")
	public BasicResultVO saveOrUpdate(@RequestBody ChannelAccount channelAccount) {
		return BasicResultVO.success(channelAccountService.save(channelAccount));
	}

	/**
	 * 根据渠道标识查询渠道账号相关的信息
	 */
	@Operation(summary = "根据渠道标识查询相关的记录", description = "根据渠道标识查询相关的记录")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/queryByChannelType")
	public BasicResultVO query(Integer channelType) {
		return BasicResultVO.success(channelAccountService.queryByChannelType(channelType));
	}

	/**
	 * 所有的渠道账号信息
	 */
	@Operation(summary = "渠道账号列表信息", description = "渠道账号列表信息")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/list")
	public BasicResultVO list() {
		return BasicResultVO.success(channelAccountService.list());
	}

	/**
	 * 根据Id删除
	 * id多个用逗号分隔开
	 */
	@Operation(summary = "根据Ids删除", description = "根据Ids删除")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@DeleteMapping("delete/{id}")
	public BasicResultVO deleteByIds(@PathVariable("id") String id) {
		if (StrUtil.isNotBlank(id)) {
			List<Long> idList = Arrays.stream(id.split(StrUtil.COMMA)).map(s -> Long.valueOf(s)).collect(Collectors.toList());
			channelAccountService.deleteByIds(idList);
			return BasicResultVO.success();
		}
		return BasicResultVO.fail();
	}

}
