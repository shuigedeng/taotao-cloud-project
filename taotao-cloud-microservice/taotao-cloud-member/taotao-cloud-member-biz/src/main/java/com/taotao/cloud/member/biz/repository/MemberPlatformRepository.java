package com.taotao.cloud.member.biz.repository;

import com.taotao.cloud.data.jpa.repository.BaseJpaRepository;
import com.taotao.cloud.member.biz.entity.MemberPlatform;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

/**
 * 第三方登录信息Repository
 *
 * @author dengtao
 * @date 2020/9/29 18:02
 * @since v1.0
 */
@Repository
public class MemberPlatformRepository extends BaseJpaRepository<MemberPlatform, Long> {
	public MemberPlatformRepository(EntityManager em) {
		super(MemberPlatform.class, em);
	}
}
