package com.taotao.cloud.log.biz.shortlink.repository.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.log.api.api.dto.DomainDTO;
import com.taotao.cloud.log.api.api.enums.BooleanEnum;
import com.taotao.cloud.log.api.api.enums.ShortLinkDomainTypeEnum;
import com.taotao.cloud.log.biz.shortlink.repository.mapper.DomainMapper;
import com.taotao.cloud.log.biz.shortlink.repository.model.Domain;
import java.util.List;
import java.util.Optional;
import javax.annotation.Resource;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.springframework.stereotype.Service;

/**
 * This is Description
 *
 * @since 2022/05/03
 */
@Service
public class DomainManager extends ServiceImpl<DomainMapper, Domain> {

	@Resource
	private DomainMapper domainMapper;

	public Optional<DomainDTO> findDomain(Long accountId, Long domainId, Integer domainType) {
		QueryWrapper<Domain> queryWrapper = new QueryWrapper<Domain>()
			.eq(Domain.COL_ID, domainId)
			.eq(Domain.COL_DOMAIN_TYPE, domainType)
			.eq(Domain.COL_IS_DELETED, BooleanEnum.FALSE.getCode());

		List<Domain> domains = domainMapper.selectList(queryWrapper);
		if (CollectionUtils.isNotEmpty(domains)) {
			Domain domain = domains.get(0);
			if (ShortLinkDomainTypeEnum.CUSTOMER.getCode().equals(domainType)
				&& !domain.getAccountNo().equals(accountId)) {
				return Optional.empty();
			}

			DomainDTO domainDTO = DomainDTO.builder().id(domain.getId())
				.domainType(domain.getDomainType())
				.accountNo(domain.getAccountNo()).value(domain.getValue())
				.build();
			return Optional.of(domainDTO);
		}

		return Optional.empty();
	}

}
