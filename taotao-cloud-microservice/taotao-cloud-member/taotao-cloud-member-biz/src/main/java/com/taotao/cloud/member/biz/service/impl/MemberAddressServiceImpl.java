package com.taotao.cloud.member.biz.service.impl;

import com.taotao.cloud.member.biz.repository.MemberAddressRepository;
import com.taotao.cloud.member.biz.service.IMemberAddressService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 会员收货地址服务实现类
 *
 * @author dengtao
 * @date 2020-10-16 16:23:49
 * @since 1.0
 */
@Service
@AllArgsConstructor
public class MemberAddressServiceImpl implements IMemberAddressService {
    private final MemberAddressRepository memberAddressRepository;
}
