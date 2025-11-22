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

package com.taotao.cloud.member.biz.controller.business.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.boot.common.model.request.PageQuery;
import com.taotao.boot.common.model.result.PageResult;
import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.member.sys.model.vo.MemberGradeVO;
import com.taotao.cloud.member.biz.model.convert.MemberGradeConvert;
import com.taotao.cloud.member.biz.model.entity.MemberGrade;
import com.taotao.cloud.member.biz.service.business.IMemberGradeService;
import com.taotao.boot.webmvc.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端,会员等级API
 *
 * @since 2021/5/16 11:29 下午
 */
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/member/manager/member/grade")
@Tag(name = "管理端-会员等级管理API", description = "管理端-会员等级管理API")
public class MemberGradeController {

    private final IMemberGradeService memberGradeService;

    @Operation(summary = "通过id获取会员等级", description = "通过id获取会员等级")
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    @GetMapping(value = "/{id}")
    public Result<MemberGradeVO> getById(@PathVariable Long id) {
        MemberGrade memberGrade = memberGradeService.getById(id);
        return Result.success(MemberGradeConvert.INSTANCE.convert(memberGrade));
    }

    @Operation(summary = "获取会员等级分页", description = "获取会员等级分页")
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    @GetMapping(value = "/page")
    public Result<PageResult<MemberGradeVO>> queryPage(PageQuery pageQuery) {
        IPage<MemberGrade> memberGradePage = memberGradeService.queryPage(pageQuery);
        return Result.success(MpUtils.convertMybatisPage(memberGradePage, MemberGradeVO.class));
    }

    @Operation(summary = "添加会员等级", description = "添加会员等级")
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    @PostMapping
    public Result<Boolean> save(@Validated MemberGrade memberGrade) {
        return Result.success(memberGradeService.save(memberGrade));
    }

    @Operation(summary = "修改会员等级", description = "修改会员等级")
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    @GetMapping
    @PutMapping(value = "/{id}")
    public Result<Boolean> updateById(@PathVariable Long id, MemberGrade memberGrade) {
        return Result.success(memberGradeService.updateById(memberGrade));
    }

    @Operation(summary = "删除会员等级", description = "删除会员等级")
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    @DeleteMapping(value = "/{id}")
    public Result<Boolean> removeById(@PathVariable Long id) {
        if (memberGradeService.getById(id).getDefaulted()) {
            throw new BusinessException(ResultEnum.USER_GRADE_IS_DEFAULT);
        }
        return Result.success(memberGradeService.removeById(id));
    }
}
