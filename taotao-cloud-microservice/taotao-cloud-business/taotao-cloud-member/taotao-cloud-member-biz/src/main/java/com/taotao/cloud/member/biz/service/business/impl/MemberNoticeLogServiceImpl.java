package com.taotao.cloud.member.biz.service.business.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.member.biz.mapper.IMemberNoticeLogMapper;
import com.taotao.cloud.member.biz.model.entity.MemberNoticeLog;
import com.taotao.cloud.member.biz.service.business.IMemberNoticeLogService;
import org.springframework.stereotype.Service;

/**
 * 会员消息业务层实现
 *
 * @since 2020/11/17 3:44 下午
 */
@Service
public class MemberNoticeLogServiceImpl extends ServiceImpl<IMemberNoticeLogMapper, MemberNoticeLog> implements
	IMemberNoticeLogService {
}
