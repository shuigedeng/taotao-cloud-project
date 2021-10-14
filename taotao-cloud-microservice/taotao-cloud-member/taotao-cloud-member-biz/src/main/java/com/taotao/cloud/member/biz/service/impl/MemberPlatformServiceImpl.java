package com.taotao.cloud.member.biz.service.impl;

import com.taotao.cloud.member.biz.repository.MemberPlatformSuperRepository;
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
    private final MemberPlatformSuperRepository memberPlatformRepository;

	public MemberPlatformServiceImpl(
		MemberPlatformSuperRepository memberPlatformRepository) {
		this.memberPlatformRepository = memberPlatformRepository;
	}
}
