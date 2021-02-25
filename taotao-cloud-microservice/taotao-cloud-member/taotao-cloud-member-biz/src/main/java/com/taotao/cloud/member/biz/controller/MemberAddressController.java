package com.taotao.cloud.member.biz.controller;

import com.taotao.cloud.member.biz.service.IMemberAddressService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 会员收货地址管理API
 *
 * @author dengtao
 * @date 2020-10-16 16:23:49
 * @since 1.0
 */
@Validated
@RestController
@RequestMapping("/member/address")
@Api(value = "会员收货地址管理API", tags = {"会员收货地址管理API"})
@AllArgsConstructor
public class MemberAddressController {
    private final IMemberAddressService memberAddressService;
}
