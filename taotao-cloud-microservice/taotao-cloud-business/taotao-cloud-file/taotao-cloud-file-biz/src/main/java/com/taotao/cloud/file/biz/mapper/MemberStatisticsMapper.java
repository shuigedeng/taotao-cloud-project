package com.taotao.cloud.file.biz.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.taotao.cloud.file.biz.entity.MemberStatisticsData;
import com.taotao.cloud.web.base.mapper.BaseSuperMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 会员统计数据处理层
 */
public interface MemberStatisticsMapper extends BaseSuperMapper<MemberStatisticsData> {

	/**
	 * 获取会员统计数量
	 *
	 * @param queryWrapper 查询条件
	 * @return 会员统计数量
	 */
	@Select("SELECT  COUNT(0)  FROM tt_member  ${ew.customSqlSegment}")
	long customSqlQuery(@Param(Constants.WRAPPER) Wrapper queryWrapper);


	/**
	 * 获取会员分布列表
	 *
	 * @return 会员分布列表
	 */
	@Select("select client_enum,count(0) as num from tt_member group by client_enum")
	List<MemberDistributionVO> distribution();
}
