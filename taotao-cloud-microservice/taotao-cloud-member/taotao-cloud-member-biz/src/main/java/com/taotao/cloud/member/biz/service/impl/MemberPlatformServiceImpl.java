package com.taotao.cloud.member.biz.service.impl;

import com.taotao.cloud.member.biz.repository.MemberPlatformRepository;
import com.taotao.cloud.member.biz.service.IMemberPlatformService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 第三方登录信息服务实现类
 *
 * @author dengtao
 * @date 2020-10-16 16:23:49
 * @since 1.0
 */
@Service
@AllArgsConstructor
public class MemberPlatformServiceImpl implements IMemberPlatformService {
    private final MemberPlatformRepository memberPlatformRepository;
}
