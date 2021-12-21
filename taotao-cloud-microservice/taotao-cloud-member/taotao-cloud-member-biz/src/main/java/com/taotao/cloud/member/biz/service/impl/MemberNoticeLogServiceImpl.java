package com.taotao.cloud.member.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.member.biz.entity.MemberNoticeLog;
import com.taotao.cloud.member.biz.mapper.MemberNoticeLogMapper;
import com.taotao.cloud.member.biz.service.MemberNoticeLogService;
import org.springframework.stereotype.Service;

/**
 * 会员消息业务层实现
 *
 * 
 * @since 2020/11/17 3:44 下午
 */
@Service
public class MemberNoticeLogServiceImpl extends ServiceImpl<MemberNoticeLogMapper, MemberNoticeLog> implements
	MemberNoticeLogService {
}
