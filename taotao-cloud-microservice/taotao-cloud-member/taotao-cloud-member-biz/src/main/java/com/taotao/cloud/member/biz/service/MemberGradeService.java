package com.taotao.cloud.member.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.member.biz.model.entity.MemberGrade;

/**
 * 会员等级业务层
 */
public interface MemberGradeService extends IService<MemberGrade> {


	IPage<MemberGrade> getByPage(PageParam pageParam);
}
