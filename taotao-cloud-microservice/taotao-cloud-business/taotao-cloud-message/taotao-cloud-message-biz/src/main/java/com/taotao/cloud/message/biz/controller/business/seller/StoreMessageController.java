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

package com.taotao.cloud.message.biz.controller.business.seller;

import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.web.utils.OperationalJudgment;
import com.taotao.cloud.message.api.enums.MessageStatusEnum;
import com.taotao.cloud.message.biz.service.business.StoreMessageService;
import com.taotao.boot.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 店铺端,消息接口 */
@Validated
@RestController
@Tag(name = "店铺端-消息API", description = "店铺端-消息API")
@RequestMapping("/message/seller/storeMessage")
public class StoreMessageController {

    /** 商家消息 */
    @Autowired
    private StoreMessageService storeMessageService;

    // @Operation(summary = "获取商家消息", description = "获取商家消息")
    // @RequestLogger("获取商家消息")
    // @PreAuthorize("hasAuthority('dept:tree:data')")
    // @GetMapping
    // public Result<IPage<StoreMessage>> getPage(String status, PageVO pageVo) {
    // 	String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
    // 	StoreMessageQueryVO storeMessageQueryVO = new StoreMessageQueryVO();
    // 	storeMessageQueryVO.setStatus(status);
    // 	storeMessageQueryVO.setStoreId(storeId);
    // 	IPage<StoreMessage> page = storeMessageService.getPage(storeMessageQueryVO, pageVo);
    // 	return Result.success(page);
    // }

    // @Operation(summary = "获取商家消息总汇", description = "获取商家消息总汇")
    // @RequestLogger("获取商家消息总汇")
    // @PreAuthorize("hasAuthority('dept:tree:data')")
    // @GetMapping("/all")
    // public Result<Map<String, Object>> getPage(PageVO pageVo) {
    // 	// String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
    // 	//返回值定义
    // 	Map<String, Object> map = new HashMap<>(4);
    // 	// StoreMessageQueryVO storeMessageQueryVO = new StoreMessageQueryVO();
    // 	// storeMessageQueryVO.setStoreId(storeId);
    // 	// //未读消息
    // 	// storeMessageQueryVO.setStatus(MessageStatusEnum.UN_READY.name());
    // 	// IPage<StoreMessage> page = storeMessageService.getPage(storeMessageQueryVO, pageVo);
    // 	// map.put("UN_READY", page);
    // 	// //已读消息
    // 	// storeMessageQueryVO.setStatus(MessageStatusEnum.ALREADY_READY.name());
    // 	// page = storeMessageService.getPage(storeMessageQueryVO, pageVo);
    // 	// map.put("ALREADY_READY", page);
    // 	// //回收站
    // 	// storeMessageQueryVO.setStatus(MessageStatusEnum.ALREADY_REMOVE.name());
    // 	// page = storeMessageService.getPage(storeMessageQueryVO, pageVo);
    // 	// map.put("ALREADY_REMOVE", page);
    // 	return Result.success(map);
    // }

    @Operation(summary = "已读操作", description = "已读操作")
    @RequestLogger("已读操作")
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PutMapping("/{id}/read")
    public Result<Boolean> readMessage(@PathVariable String id) {
        OperationalJudgment.judgment(storeMessageService.getById(id));
        Boolean result = storeMessageService.editStatus(MessageStatusEnum.ALREADY_READY.name(), id);
        return Result.success(result);
    }

    @Operation(summary = "回收站还原消息", description = "回收站还原消息")
    @RequestLogger("回收站还原消息")
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PutMapping("/{id}/reduction")
    public Result<Boolean> reductionMessage(@PathVariable String id) {
        OperationalJudgment.judgment(storeMessageService.getById(id));
        Boolean result = storeMessageService.editStatus(MessageStatusEnum.ALREADY_READY.name(), id);
        return Result.success(result);
    }

    @Operation(summary = "删除操作", description = "删除操作")
    @RequestLogger("删除操作")
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteMessage(@PathVariable String id) {
        OperationalJudgment.judgment(storeMessageService.getById(id));
        Boolean result = storeMessageService.editStatus(MessageStatusEnum.ALREADY_REMOVE.name(), id);
        return Result.success(result);
    }

    @Operation(summary = "彻底删除操作", description = "彻底删除操作")
    @RequestLogger("彻底删除操作")
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @DeleteMapping("/{id}/thorough")
    public Result<Boolean> disabled(@PathVariable String id) {
        OperationalJudgment.judgment(storeMessageService.getById(id));
        Boolean result = storeMessageService.deleteByMessageId(id);
        return Result.success(result);
    }
}
