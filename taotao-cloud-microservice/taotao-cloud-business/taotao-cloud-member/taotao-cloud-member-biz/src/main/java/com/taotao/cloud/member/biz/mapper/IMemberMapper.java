/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.member.biz.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.taotao.cloud.member.sys.model.vo.MemberVO;
import com.taotao.cloud.member.biz.model.entity.Member;
import com.taotao.boot.data.mybatis.mybatisplus.base.mapper.MpSuperMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/** 会员数据处理层 */
public interface IMemberMapper extends MpSuperMapper<Member, Long> {

    /**
     * 获取所有的会员手机号
     *
     * @return 会员手机号
     */
    @Select("""
		select m.mobile
		from tt_member m
		""")
    List<String> getAllMemberMobile();

    @Select("""
		select *
		from tt_member
		${ew.customSqlSegment}
		""")
    IPage<MemberVO> pageByMemberVO(IPage<MemberVO> page, @Param(Constants.WRAPPER) Wrapper<Member> queryWrapper);
}
