package com.taotao.cloud.member.biz.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.model.PageModel;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.common.utils.common.SecurityUtil;
import com.taotao.cloud.common.utils.lang.StringUtil;
import com.taotao.cloud.member.api.vo.MemberPointsHistoryPageVO;
import com.taotao.cloud.member.api.vo.MemberPointsHistoryVO;
import com.taotao.cloud.member.biz.entity.Member;
import com.taotao.cloud.member.biz.entity.MemberPointsHistory;
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
	ServiceImpl<MemberPointsHistoryMapper, MemberPointsHistory> implements
	MemberPointsHistoryService {


	@Autowired
	private MemberService memberService;

	@Override
	public MemberPointsHistoryVO getMemberPointsHistoryVO(String memberId) {
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
	public PageModel<MemberPointsHistoryPageVO> getByPage(PageParam pageParam) {
		LambdaQueryWrapper<MemberPointsHistory> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(MemberPointsHistory::getMemberId, SecurityUtil.getUserId());
		queryWrapper.orderByDesc(MemberPointsHistory::getCreateTime);
		IPage<MemberPointsHistory> memberPointsHistoryPage = this.page(pageParam.buildMpPage(),
			queryWrapper);
		return PageModel.convertMybatisPage(memberPointsHistoryPage,
			MemberPointsHistoryPageVO.class);
	}

	@Override
	public PageModel<MemberPointsHistory> memberPointsHistoryList(PageParam pageParam,
		String memberId, String memberName) {
		LambdaQueryWrapper<MemberPointsHistory> lambdaQueryWrapper = new LambdaQueryWrapper<MemberPointsHistory>()
			.eq(memberId != null, MemberPointsHistory::getMemberId, memberId)
			.like(memberName != null, MemberPointsHistory::getMemberName, memberName);

		//如果排序为空，则默认创建时间倒序
		if (StringUtil.isNotBlank(pageParam.getSort())) {
			pageParam.setSort("createTime");
			pageParam.setOrder("desc");
		}
		IPage<MemberPointsHistory> memberPointsHistoryPage = this.page(pageParam.buildMpPage(),
			lambdaQueryWrapper);
		return PageModel.convertMybatisPage(memberPointsHistoryPage,
			MemberPointsHistoryPageVO.class);
	}

}
