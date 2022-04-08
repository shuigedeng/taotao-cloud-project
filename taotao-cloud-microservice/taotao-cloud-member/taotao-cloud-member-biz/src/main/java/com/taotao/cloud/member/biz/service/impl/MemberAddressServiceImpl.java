package com.taotao.cloud.member.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.model.PageModel;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.common.utils.common.SecurityUtil;
import com.taotao.cloud.member.api.vo.MemberAddressVO;
import com.taotao.cloud.member.biz.entity.MemberAddress;
import com.taotao.cloud.member.biz.mapper.MemberAddressMapper;
import com.taotao.cloud.member.biz.service.MemberAddressService;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 收货地址业务层实现
 */
@Service
public class MemberAddressServiceImpl extends
	ServiceImpl<MemberAddressMapper, MemberAddress> implements MemberAddressService {

	@Override
	public PageModel<MemberAddressVO> getAddressByMember(PageParam page, Long memberId) {
		IPage<MemberAddress> memberAddressPage = this.page(page.buildMpPage(),
			new LambdaQueryWrapper<MemberAddress>().eq(MemberAddress::getMemberId, memberId));

		return PageModel.convertMybatisPage(memberAddressPage, MemberAddressVO.class);
	}

	@Override
	public MemberAddress getMemberAddress(Long id) {
		return this.getOne(new LambdaQueryWrapper<MemberAddress>()
			.eq(MemberAddress::getMemberId, Objects.requireNonNull(SecurityUtil.getUserId()))
			.eq(MemberAddress::getId, id));
	}

	@Override
	public MemberAddress getDefaultMemberAddress() {
		return this.getOne(new LambdaQueryWrapper<MemberAddress>()
			.eq(MemberAddress::getMemberId, Objects.requireNonNull(SecurityUtil.getUserId()))
			.eq(MemberAddress::getDefaulted, true));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean saveMemberAddress(MemberAddress memberAddress) {
		//判断当前地址是否为默认地址，如果为默认需要将其他的地址修改为非默认
		removeDefaultAddress(memberAddress);

		//添加会员地址
		return this.save(memberAddress);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean updateMemberAddress(MemberAddress memberAddress) {
		MemberAddress originalMemberAddress = this.getMemberAddress(
			memberAddress.getId());

		if (originalMemberAddress != null &&
			originalMemberAddress.getMemberId()
				.equals(Objects.requireNonNull(SecurityUtil.getUserId()))) {
			if (memberAddress.getDefaulted() == null) {
				memberAddress.setDefaulted(false);
			}

			//判断当前地址是否为默认地址，如果为默认需要将其他的地址修改为非默认
			removeDefaultAddress(memberAddress);
			this.saveOrUpdate(memberAddress);
		}

		return true;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean removeMemberAddress(Long id) {
		return this.remove(new LambdaQueryWrapper<MemberAddress>().eq(MemberAddress::getId, id));
	}

	/**
	 * 修改会员默认收件地址
	 *
	 * @param memberAddress 收件地址
	 */
	private void removeDefaultAddress(MemberAddress memberAddress) {
		//如果不是默认地址不需要处理
		if (Boolean.TRUE.equals(memberAddress.getDefaulted())) {
			//将会员的地址修改为非默认地址
			LambdaUpdateWrapper<MemberAddress> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
			lambdaUpdateWrapper.set(MemberAddress::getDefaulted, false);
			lambdaUpdateWrapper.eq(MemberAddress::getMemberId, memberAddress.getMemberId());
			this.update(lambdaUpdateWrapper);
		}
	}
}
