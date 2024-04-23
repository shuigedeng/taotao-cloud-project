package com.taotao.cloud.member.domain.notice.repository;

import com.taotao.cloud.member.domain.notice.entity.MemberNoticeEntity;

public interface MemberNoticeDomainRepository {
	/**
	 * 新增部门.
	 *
	 * @param dept 部门对象
	 */
	void create(MemberNoticeEntity dept);

	/**
	 * 修改部门.
	 *
	 * @param dept 部门对象
	 */
	void modify(MemberNoticeEntity dept);

	/**
	 * 根据ID删除部门.
	 *
	 * @param ids IDS
	 */
	void remove(Long[] ids);
}
