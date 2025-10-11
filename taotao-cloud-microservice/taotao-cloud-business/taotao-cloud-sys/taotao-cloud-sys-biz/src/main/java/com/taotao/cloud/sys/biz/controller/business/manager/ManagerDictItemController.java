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

package com.taotao.cloud.sys.biz.controller.business.manager;

import com.taotao.boot.common.model.request.BaseQuery;
import com.taotao.cloud.sys.biz.model.dto.dictItem.DictItemSaveDTO;
import com.taotao.cloud.sys.biz.model.dto.dictItem.DictItemUpdateDTO;
import com.taotao.cloud.sys.biz.model.vo.dict_item.DictItemQueryVO;
import com.taotao.cloud.sys.biz.model.entity.dict.DictItem;
import com.taotao.cloud.sys.biz.service.business.IDictItemService;
import com.taotao.boot.webagg.controller.BaseSuperController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端-字典项管理API
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-10-09 14:58:55
 */
@Validated
@RestController
@RequestMapping("/sys/manager/dict/item")
@Tag(name = "管理端-字典项管理API", description = "管理端-字典项管理API")
public class ManagerDictItemController
        extends BaseSuperController<
                IDictItemService, DictItem, Long, BaseQuery, DictItemSaveDTO, DictItemUpdateDTO, DictItemQueryVO> {

    /// **
    // * 根据code查询字典项列表
    // *
    // * @param code 字典码
    // * @return Result<List < SysDict>>
    // */
    // @PreAuth
    // @Log(value = "字典项列表", exception = "字典项列表异常")
    // @GetMapping("list-value")
    // @ApiOperation(value = "字典项列表", notes = "字典项列表")
    // public Result<?> listValue(@RequestParam String code) {
    //	return Result.data(sysDictService.list(new LambdaQueryWrapper<SysDict>()
    //		.eq(SysDict::getCode, code)
    //		.ne(SysDict::getParentId, 0)
    //		.orderByAsc(SysDict::getSort)));
    // }
}
