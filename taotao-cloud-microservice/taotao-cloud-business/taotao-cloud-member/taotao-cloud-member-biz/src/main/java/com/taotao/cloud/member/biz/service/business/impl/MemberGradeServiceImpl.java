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

package com.taotao.cloud.member.biz.service.business.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.boot.common.model.request.PageQuery;
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
public class MemberGradeServiceImpl extends ServiceImpl<IMemberGradeMapper, MemberGrade>
        implements IMemberGradeService {

    @Override
    public IPage<MemberGrade> queryPage(PageQuery pageQuery) {
        return this.page(pageQuery.buildMpPage());
    }
}
