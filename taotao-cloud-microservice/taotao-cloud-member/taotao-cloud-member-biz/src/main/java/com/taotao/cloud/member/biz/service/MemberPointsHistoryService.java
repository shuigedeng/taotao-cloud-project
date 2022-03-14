package com.taotao.cloud.member.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.common.model.PageModel;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.member.api.vo.MemberPointsHistoryPageVO;
import com.taotao.cloud.member.api.vo.MemberPointsHistoryVO;
import com.taotao.cloud.member.biz.entity.MemberPointsHistory;

/**
 * 会员积分历史业务层
 *
 * 
 * @since 2020-02-25 14:10:16
 */
public interface MemberPointsHistoryService extends IService<MemberPointsHistory> {

    /**
     * 获取会员积分VO
     *
     * @param memberId 会员ID
     * @return 会员积分VO
     */
    MemberPointsHistoryVO getMemberPointsHistoryVO(String memberId);

	PageModel<MemberPointsHistoryPageVO> getByPage(PageParam pageParam);

    /**
     * 会员积分历史
     *
     * @param pageParam       分页
     * @param memberId   会员ID
     * @param memberName 会员名称
     * @return 积分历史分页
     */
    PageModel<MemberPointsHistory> memberPointsHistoryList(PageParam pageParam, String memberId, String memberName);

}
