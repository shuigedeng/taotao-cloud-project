package com.taotao.cloud.member.biz.service.business.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.member.biz.mapper.IMemberGradeMapper;
import com.taotao.cloud.member.biz.model.entity.MemberGrade;
import com.taotao.cloud.member.biz.service.business.IMemberGradeService;
import org.springframework.stereotype.Service;

/**
 * 会员等级业务层实现
 *
 * @since 2021/5/14 5:58 下午
 */
@Service
public class MemberGradeServiceImpl extends ServiceImpl<IMemberGradeMapper, MemberGrade> implements
	IMemberGradeService {

	@Override
	public IPage<MemberGrade> getByPage(PageParam pageParam) {
		return this.page(pageParam.buildMpPage());
	}
}
