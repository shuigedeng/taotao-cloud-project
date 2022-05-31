package com.taotao.cloud.member.biz.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.member.biz.entity.MemberGrade;
import com.taotao.cloud.member.biz.mapper.MemberGradeMapper;
import com.taotao.cloud.member.biz.service.MemberGradeService;
import org.springframework.stereotype.Service;

/**
 * 会员等级业务层实现
 *
 * 
 * @since 2021/5/14 5:58 下午
 */
@Service
public class MemberGradeServiceImpl extends ServiceImpl<MemberGradeMapper, MemberGrade> implements
	MemberGradeService {

    @Override
    public IPage<MemberGrade> getByPage(PageParam pageParam) {
		return this.page(pageParam.buildMpPage());
    }
}
