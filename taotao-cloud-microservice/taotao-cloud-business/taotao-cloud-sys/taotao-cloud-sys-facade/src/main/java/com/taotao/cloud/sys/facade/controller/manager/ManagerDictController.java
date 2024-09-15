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

package com.taotao.cloud.sys.facade.controller.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taotao.boot.common.model.BaseQuery;
import com.taotao.boot.common.model.Result;
import com.taotao.boot.web.base.controller.BaseSuperController;
import com.taotao.boot.web.utils.CollectionUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Collection;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端-字典管理API
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-10-09 14:24:19
 */
@Validated
@RestController
@RequestMapping("/sys/manager/dict")
@Tag(name = "管理端-字典管理API", description = "管理端-字典管理API")
public class ManagerDictController{

//    @GetMapping("/list-code")
//    // @ApiOperation(value = "字典列表code查询", notes = "字典列表code查询")
//    public Result<Dict> listCode(String code) {
//        return Result.success(service().findByCode(code));
//    }
//
//    @GetMapping("/get-dict-value")
//    // @ApiOperation(value = "字典列表key查询", notes = "字典列表key查询")
//    public Result<Dict> getDictValue(String code, String dictKey) {
//        return Result.success(service().getById(code));
//    }
//
//    @PostMapping("/del")
//    @Operation(summary= "字典删除")
//    // @ApiImplicitParams({
//    //	@ApiImplicitParam(name = "ids", required = true, value = "多个用,号隔开", paramType = "form")
//    // })
//    public Result<?> del(@RequestParam String ids) {
//        Collection<?> idsCollection = CollectionUtil.stringToCollection(ids);
//        if (service().removeByIds(idsCollection)) {
//            // 批量删除字典列表的同时，也要删除字典项的内容
//            for (Object obj : idsCollection) {
//                service().remove(new LambdaQueryWrapper<Dict>().eq(Dict::getId, obj));
//            }
//            return Result.success("删除成功");
//        }
//        return Result.fail("删除失败");
//    }
//
//    @GetMapping("/testMybatisQueryStructure")
//    // @ApiOperation(value = "字典列表code查询", notes = "字典列表code查询")
//    public Result<Dict> testMybatisQueryStructure(DictQuery dictQuery) {
//        return Result.success(service().testMybatisQueryStructure(dictQuery));
//    }
}
