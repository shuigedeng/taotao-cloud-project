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

package com.taotao.cloud.wechat.biz.wechat.controller;

import static me.chanjar.weixin.mp.bean.material.WxMpMaterialNewsBatchGetResult.WxMaterialNewsBatchGetNewsItem;

import cn.bootx.common.core.rest.PageResult;
import cn.bootx.common.core.rest.Res;
import cn.bootx.common.core.rest.ResResult;
import cn.bootx.common.core.rest.param.PageQuery;
import cn.bootx.starter.wechat.core.media.service.WeChatMediaService;
import cn.bootx.starter.wechat.dto.media.WeChatMediaDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author xxm
 * @since 2022/8/9
 */
@Tag(name = "微信素材管理")
@RestController
@RequestMapping("/wechat/media")
@RequiredArgsConstructor
public class WeChatMediaController {
    private final WeChatMediaService weChatMediaService;

    @Operation(summary = "非图文素材分页")
    @GetMapping("/pageFile")
    public ResResult<PageResult<WeChatMediaDto>> pageFile(PageQuery PageQuery, String type) {
        return Res.ok(weChatMediaService.pageFile(PageQuery, type));
    }

    @Operation(summary = "图文素材分页")
    @GetMapping("/pageNews")
    public ResResult<PageResult<WxMaterialNewsBatchGetNewsItem>> pageNews(PageQuery PageQuery) {
        return Res.ok(weChatMediaService.pageNews(PageQuery));
    }

    @Operation(summary = "删除素材")
    @DeleteMapping("/deleteFile")
    public ResResult<Void> deleteFile(String mediaId) {
        weChatMediaService.deleteFile(mediaId);
        return Res.ok();
    }

    @Operation(summary = "上传素材")
    @PostMapping("/uploadFile")
    public ResResult<Void> uploadFile(String mediaType, MultipartFile file) {
        weChatMediaService.uploadFile(mediaType, file);
        return Res.ok();
    }
}
