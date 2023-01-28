package com.taotao.cloud.member.biz.service.business;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.common.model.PageQuery;
import com.taotao.cloud.member.api.model.vo.MemberPointsHistoryVO;
import com.taotao.cloud.member.biz.model.entity.MemberPointsHistory;

/**
 * 会员积分历史业务层
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-05-31 14:16:21
 */
public interface IMemberPointsHistoryService extends IService<MemberPointsHistory> {

	/**
	 * 获取会员积分VO
	 *
	 * @param memberId 会员ID
	 * @return {@link MemberPointsHistoryVO }
	 * @since 2022-05-31 14:16:21
	 */
	MemberPointsHistoryVO getMemberPointsHistoryVO(Long memberId);

	/**
	 * 通过页面
	 *
	 * @param pageQuery 页面参数
	 * @return {@link IPage }<{@link MemberPointsHistory }>
	 * @since 2022-05-31 14:16:22
	 */
	IPage<MemberPointsHistory> pageQuery(PageQuery pageQuery);

	/**
	 * 会员积分历史
	 *
	 * @param pageQuery  分页
	 * @param memberId   会员ID
	 * @param memberName 会员名称
	 * @return {@link IPage }<{@link MemberPointsHistory }>
	 * @since 2022-05-31 14:16:22
	 */
	IPage<MemberPointsHistory> memberPointsHistoryPageQuery(PageQuery pageQuery, Long memberId,
													   String memberName);

}
