package com.taotao.cloud.member.biz.repository;

import com.taotao.cloud.data.jpa.repository.BaseJpaRepository;
import com.taotao.cloud.member.biz.entity.MemberAddress;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

/**
 * 会员收货地址Repository
 *
 * @author dengtao
 * @date 2020/9/29 18:02
 * @since v1.0
 */
@Repository
public class MemberAddressRepository extends BaseJpaRepository<MemberAddress, Long> {
    public MemberAddressRepository(EntityManager em) {
        super(MemberAddress.class, em);
    }
}
