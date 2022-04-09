package com.taotao.cloud.member.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.member.biz.entity.MemberPointsHistory;
import org.apache.ibatis.annotations.Select;

/**
 * 会员积分历史数据处理层
 */
public interface MemberPointsHistoryMapper extends BaseMapper<MemberPointsHistory> {

	/**
	 * 获取所有用户的积分历史VO
	 *
	 * @param pointType 积分类型
	 * @return 积分
	 */
	@Select("""
		SELECT SUM( variable_point )
		FROM li_member_points_history
		WHERE point_type = #{pointType}
		""")
	Long getALLMemberPointsHistoryVO(String pointType);

	/**
	 * 获取用户的积分数量
	 *
	 * @param pointType 积分类型
	 * @param memberId  会员ID
	 * @return 积分数量
	 */
	@Select("""
		SELECT SUM( variable_point )
		FROM li_member_points_history
		WHERE point_type = #{pointType} AND member_id=#{memberId}
		""")
	Long getMemberPointsHistoryVO(String pointType, String memberId);


}
