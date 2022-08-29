package com.taotao.cloud.member.biz.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.common.utils.common.SecurityUtils;
import com.taotao.cloud.common.utils.lang.StringUtils;
import com.taotao.cloud.member.api.web.vo.MemberPointsHistoryVO;
import com.taotao.cloud.member.biz.model.entity.Member;
import com.taotao.cloud.member.biz.model.entity.MemberPointsHistory;
import com.taotao.cloud.member.biz.mapper.MemberPointsHistoryMapper;
import com.taotao.cloud.member.biz.service.MemberPointsHistoryService;
import com.taotao.cloud.member.biz.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 会员积分历史业务层实现
 *
 * @since 2020-02-25 14:10:16
 */
@Service
public class MemberPointsHistoryServiceImpl extends
	ServiceImpl<MemberPointsHistoryMapper, MemberPointsHistory> implements MemberPointsHistoryService {


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
	public IPage<MemberPointsHistory> getByPage(PageParam pageParam) {
		LambdaQueryWrapper<MemberPointsHistory> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(MemberPointsHistory::getMemberId, SecurityUtils.getUserId());
		queryWrapper.orderByDesc(MemberPointsHistory::getCreateTime);
		return this.page(pageParam.buildMpPage(), queryWrapper);
	}

	@Override
	public IPage<MemberPointsHistory> memberPointsHistoryList(PageParam pageParam,
		Long memberId, String memberName) {
		LambdaQueryWrapper<MemberPointsHistory> lambdaQueryWrapper = new LambdaQueryWrapper<MemberPointsHistory>()
			.eq(memberId != null, MemberPointsHistory::getMemberId, memberId)
			.like(memberName != null, MemberPointsHistory::getMemberName, memberName);

		//如果排序为空，则默认创建时间倒序
		if (StringUtils.isNotBlank(pageParam.getSort())) {
			pageParam.setSort("createTime");
			pageParam.setOrder("desc");
		}
		return this.page(pageParam.buildMpPage(), lambdaQueryWrapper);
	}

}
