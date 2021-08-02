package com.taotao.cloud.member.biz.service.impl;

import com.taotao.cloud.member.biz.repository.MemberPlatformRepository;
import com.taotao.cloud.member.biz.service.IMemberPlatformService;
import org.springframework.stereotype.Service;

/**
 * 第三方登录信息服务实现类
 *
 * @author shuigedeng
 * @since 2020-10-16 16:23:49
 * @since 1.0
 */
@Service
public class MemberPlatformServiceImpl implements IMemberPlatformService {
    private final MemberPlatformRepository memberPlatformRepository;

	public MemberPlatformServiceImpl(
		MemberPlatformRepository memberPlatformRepository) {
		this.memberPlatformRepository = memberPlatformRepository;
	}
}
