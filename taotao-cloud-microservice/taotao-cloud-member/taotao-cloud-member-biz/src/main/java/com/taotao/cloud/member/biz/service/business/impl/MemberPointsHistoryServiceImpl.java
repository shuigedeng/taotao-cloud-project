package com.taotao.cloud.member.biz.service.business.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.model.PageQuery;
import com.taotao.cloud.common.utils.common.SecurityUtils;
import com.taotao.cloud.common.utils.lang.StringUtils;
import com.taotao.cloud.member.api.model.vo.MemberPointsHistoryVO;
import com.taotao.cloud.member.biz.mapper.IMemberPointsHistoryMapper;
import com.taotao.cloud.member.biz.model.entity.Member;
import com.taotao.cloud.member.biz.model.entity.MemberPointsHistory;
import com.taotao.cloud.member.biz.service.business.IMemberPointsHistoryService;
import com.taotao.cloud.member.biz.service.business.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 会员积分历史业务层实现
 *
 * @since 2020-02-25 14:10:16
 */
@Service
public class MemberPointsHistoryServiceImpl extends
	ServiceImpl<IMemberPointsHistoryMapper, MemberPointsHistory> implements IMemberPointsHistoryService {


	@Autowired
	private MemberService memberService;

	@Override
	public MemberPointsHistoryVO getMemberPointsHistoryVO(Long memberId) {
		//获取会员积分历史
		Member member = memberService.getById(memberId);

		MemberPointsHistoryVO memberPointsHistoryVO = new MemberPointsHistoryVO();
		if (member != null) {
			memberPointsHistoryVO.setPoint(member.getPoint());
			memberPointsHistoryVO.setTotalPoint(member.getTotalPoint());
			return memberPointsHistoryVO;
		}
		return new MemberPointsHistoryVO();
	}

	@Override
	public IPage<MemberPointsHistory> getByPage(PageQuery PageQuery) {
		LambdaQueryWrapper<MemberPointsHistory> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(MemberPointsHistory::getMemberId, SecurityUtils.getUserId());
		queryWrapper.orderByDesc(MemberPointsHistory::getCreateTime);
		return this.page(PageQuery.buildMpPage(), queryWrapper);
	}

	@Override
	public IPage<MemberPointsHistory> memberPointsHistoryList(PageQuery PageQuery,
															  Long memberId, String memberName) {
		LambdaQueryWrapper<MemberPointsHistory> lambdaQueryWrapper = new LambdaQueryWrapper<MemberPointsHistory>()
			.eq(memberId != null, MemberPointsHistory::getMemberId, memberId)
			.like(memberName != null, MemberPointsHistory::getMemberName, memberName);

		//如果排序为空，则默认创建时间倒序
		if (StringUtils.isNotBlank(PageQuery.getSort())) {
			PageQuery.setSort("createTime");
			PageQuery.setOrder("desc");
		}
		return this.page(PageQuery.buildMpPage(), lambdaQueryWrapper);
	}

}
