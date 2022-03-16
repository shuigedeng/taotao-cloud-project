package com.taotao.cloud.member.biz.mapper;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.taotao.cloud.member.api.vo.MemberVO;
import com.taotao.cloud.member.biz.entity.Member;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 会员数据处理层
 */
public interface MemberMapper extends BaseMapper<Member> {

	/**
	 * 获取所有的会员手机号
	 *
	 * @return 会员手机号
	 */
	@Select("select m.mobile from li_member m")
	List<String> getAllMemberMobile();

	@Select("select * from li_member ${ew.customSqlSegment}")
	IPage<MemberVO> pageByMemberVO(IPage<MemberVO> page,
		@Param(Constants.WRAPPER) Wrapper<Member> queryWrapper);
}
