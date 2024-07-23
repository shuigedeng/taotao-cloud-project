/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.member.application.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.utils.bean.BeanUtils;
import com.taotao.cloud.common.utils.lang.StringUtils;
import com.taotao.cloud.member.application.service.IMemberReceiptService;
import com.taotao.cloud.member.application.service.IMemberService;
import com.taotao.cloud.member.infrastructure.persistent.mapper.IMemberReceiptMapper;
import com.taotao.cloud.member.infrastructure.persistent.po.MemberPO;
import com.taotao.cloud.member.infrastructure.persistent.po.MemberReceiptPO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 会员发票业务层实现
 *
 * @author shuigedeng
 * @version 2023.01
 * @since 2023-02-01 13:48:11
 */
@Service
public class MemberReceiptServiceImpl extends ServiceImpl<IMemberReceiptMapper, MemberReceiptPO>
	implements IMemberReceiptService {

	@Autowired
	private IMemberService memberService;

	@Override
	public IPage<MemberReceiptPO> getPage(MemberReceiptPageQuery memberReceiptPageQuery) {
		LambdaQueryWrapper<MemberReceiptPO> queryWrapper = new LambdaQueryWrapper<>();
		// 会员名称查询
		if (StringUtils.isNotEmpty(memberReceiptPageQuery.getMemberName())) {
			queryWrapper.like(MemberReceiptPO::getMemberName, memberReceiptPageQuery.getMemberName());
		}
		// 会员id查询
		if (StringUtils.isNotEmpty(memberReceiptPageQuery.getMemberId())) {
			queryWrapper.eq(MemberReceiptPO::getMemberId, memberReceiptPageQuery.getMemberId());
		}
		// 会员id查询
		if (StringUtils.isNotEmpty(memberReceiptPageQuery.getReceiptType())) {
			queryWrapper.eq(MemberReceiptPO::getReceiptType, memberReceiptPageQuery.getReceiptType());
		}
		queryWrapper.eq(MemberReceiptPO::getDeleteFlag, true);
		queryWrapper.orderByDesc(MemberReceiptPO::getCreateTime);
		return this.page(memberReceiptPageQuery.buildMpPage(), queryWrapper);
	}

	@Override
	public Boolean addMemberReceipt(MemberReceiptAddVO memberReceiptAddVO, Long memberId) {
		// 校验发票抬头是否重复
		List<MemberReceiptPO> receipts = this.baseMapper.selectList(new QueryWrapper<MemberReceiptPO>()
			.eq("member_id", memberId)
			.eq("receipt_title", memberReceiptAddVO.getReceiptTitle()));
		if (receipts.size() > 0) {
			throw new BusinessException(ResultEnum.USER_RECEIPT_REPEAT_ERROR);
		}
		// 参数封装
		MemberReceiptPO memberReceiptPO = new MemberReceiptPO();
		BeanUtils.copyProperties(memberReceiptAddVO, memberReceiptPO);
		// 根据会员信息查询会员
		MemberPO member = memberService.getById(memberId);
		if (member != null) {
			memberReceiptPO.setMemberId(memberId);
			memberReceiptPO.setMemberName(member.getUsername());
			// 设置发票默认
			List<MemberReceiptPO> list =
				this.baseMapper.selectList(
					new QueryWrapper<MemberReceiptPO>().eq("member_id", memberId));
			// 如果当前会员只有一个发票则默认为默认发票，反之需要校验参数默认值，做一些处理
			if (list.size() <= 0) {
				memberReceiptPO.setDefaulted(1);
			}
			else {
				if (memberReceiptAddVO.getIsDefault().equals(1)) {
					// 如果参数传递新添加的发票信息为默认，则需要把其他发票置为非默认
					this.update(new UpdateWrapper<MemberReceiptPO>().eq("member_id", memberId));
					// 设置当前发票信息为默认
					memberReceiptPO.setDefaulted(memberReceiptAddVO.getIsDefault());
				}
				else {
					memberReceiptAddVO.setIsDefault(0);
				}
			}
			return this.baseMapper.insert(memberReceiptPO) > 0;
		}
		throw new BusinessException(ResultEnum.USER_RECEIPT_NOT_EXIST);
	}

	@Override
	public Boolean editMemberReceipt(MemberReceiptAddVO memberReceiptAddVO, Long memberId) {
		// 根据会员id查询发票信息
		MemberReceiptPO memberReceiptPODb = this.baseMapper.selectById(memberReceiptAddVO.getId());
		if (memberReceiptPODb != null) {
			// 检验是否有权限修改
			if (!memberReceiptPODb.getMemberId().equals(memberId)) {
				throw new BusinessException(ResultEnum.USER_AUTHORITY_ERROR);
			}
			// 校验发票抬头是否重复
			List<MemberReceiptPO> receipts = this.baseMapper.selectList(
				new QueryWrapper<MemberReceiptPO>()
					.eq("member_id", memberId)
					.eq("receipt_title", memberReceiptAddVO.getReceiptTitle())
					.ne("id", memberReceiptAddVO.getId()));
			if (receipts.size() > 0) {
				throw new BusinessException(ResultEnum.USER_RECEIPT_REPEAT_ERROR);
			}
			BeanUtils.copyProperties(memberReceiptAddVO, memberReceiptPODb);
			// 对发票默认进行处理  如果参数传递新添加的发票信息为默认，则需要把其他发票置为非默认
			if (memberReceiptAddVO.getIsDefault().equals(1)) {
				this.update(new UpdateWrapper<MemberReceiptPO>().eq("member_id", memberId));
			}
			return this.baseMapper.updateById(memberReceiptPODb) > 0;
		}
		throw new BusinessException(ResultEnum.USER_RECEIPT_NOT_EXIST);
	}

	@Override
	public Boolean deleteMemberReceipt(Long id) {
		// 根据会员id查询发票信息
		MemberReceiptPO memberReceiptPODb = this.baseMapper.selectById(id);
		if (memberReceiptPODb != null) {
			// 如果会员发票信息不为空 则逻辑删除此发票信息
			memberReceiptPODb.setDeleteFlag(false);
			this.baseMapper.updateById(memberReceiptPODb);
		}
		return true;
	}
}
