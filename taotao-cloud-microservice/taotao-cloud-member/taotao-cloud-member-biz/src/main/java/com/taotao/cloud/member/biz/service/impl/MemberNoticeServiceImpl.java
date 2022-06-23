package com.taotao.cloud.member.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.member.biz.model.entity.MemberNotice;
import com.taotao.cloud.member.biz.mapper.MemberNoticeMapper;
import com.taotao.cloud.member.biz.service.MemberNoticeService;
import org.springframework.stereotype.Service;

/**
 * 会员站内信业务层实现
 *
 * 
 * @since 2020/11/17 3:44 下午
 */
@Service
public class MemberNoticeServiceImpl extends ServiceImpl<MemberNoticeMapper, MemberNotice> implements
	MemberNoticeService {

}
