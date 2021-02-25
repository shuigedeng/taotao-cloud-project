package com.taotao.cloud.member.biz.controller;

import com.taotao.cloud.member.biz.service.IMemberPlatformService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 第三方用户信息管理API
 *
 * @author dengtao
 * @date 2020-10-16 16:23:49
 * @since 1.0
 */
@Validated
@RestController
@RequestMapping("/member/platform")
@Api(value = "第三方用户信息管理API", tags = {"第三方用户信息管理API"})
@AllArgsConstructor
public class MemberPlatformController {
    private final IMemberPlatformService memberPlatformService;
}
