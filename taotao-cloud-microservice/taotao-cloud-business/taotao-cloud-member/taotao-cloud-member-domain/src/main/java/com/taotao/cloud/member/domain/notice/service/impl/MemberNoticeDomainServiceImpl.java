package com.taotao.cloud.member.domain.notice.service.impl;

import com.taotao.cloud.member.domain.notice.entity.MemberNoticeEntity;
import com.taotao.cloud.member.domain.notice.repository.MemberNoticeDomainRepository;
import com.taotao.cloud.member.domain.notice.service.MemberNoticeDomainService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MemberNoticeDomainServiceImpl implements MemberNoticeDomainService {

	private MemberNoticeDomainRepository memberNoticeDomainRepository;

	@Override
	public void create(MemberNoticeEntity dept) {

	}

	@Override
	public void modify(MemberNoticeEntity dept) {

	}

	@Override
	public void remove(Long[] ids) {

	}
}
