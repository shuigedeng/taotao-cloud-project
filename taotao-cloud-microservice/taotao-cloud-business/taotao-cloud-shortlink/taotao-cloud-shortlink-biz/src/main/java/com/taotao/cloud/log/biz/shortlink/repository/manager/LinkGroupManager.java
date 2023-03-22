package com.taotao.cloud.log.biz.shortlink.repository.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.log.api.api.dto.LinkGroupDTO;
import com.taotao.cloud.log.api.api.enums.BooleanEnum;
import com.taotao.cloud.log.biz.shortlink.repository.mapper.LinkGroupMapper;
import com.taotao.cloud.log.biz.shortlink.repository.model.Domain;
import com.taotao.cloud.log.biz.shortlink.repository.model.LinkGroup;
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
public class LinkGroupManager extends ServiceImpl<LinkGroupMapper, LinkGroup> {

	@Resource
	private LinkGroupMapper linkGroupMapper;


	public Optional<LinkGroupDTO> findLinkGroup(Long accountId, Long groupId) {
		QueryWrapper<LinkGroup> queryWrapper = new QueryWrapper<LinkGroup>()
			.eq(LinkGroup.COL_ID, groupId)
			.eq(LinkGroup.COL_ACCOUNT_NO, accountId)
			.eq(Domain.COL_IS_DELETED, BooleanEnum.FALSE.getCode());

		List<LinkGroup> LinkGroups = linkGroupMapper.selectList(queryWrapper);
		if (CollectionUtils.isNotEmpty(LinkGroups)) {
			LinkGroup linkGroup = LinkGroups.get(0);

			LinkGroupDTO domainDTO = LinkGroupDTO.builder().id(linkGroup.getId())
				.accountNo(linkGroup.getAccountNo()).title(linkGroup.getTitle())
				.build();
			return Optional.of(domainDTO);
		}

		return Optional.empty();
	}

}
