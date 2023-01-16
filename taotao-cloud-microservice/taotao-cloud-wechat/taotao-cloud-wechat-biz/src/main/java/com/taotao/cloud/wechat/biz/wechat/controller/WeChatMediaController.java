package com.taotao.cloud.wechat.biz.wechat.controller;

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

import static me.chanjar.weixin.mp.bean.material.WxMpMaterialNewsBatchGetResult.WxMaterialNewsBatchGetNewsItem;

/**
 *
 * @author xxm
 * @date 2022/8/9
 */
@Tag(name = "微信素材管理")
@RestController
@RequestMapping("/wechat/media")
@RequiredArgsConstructor
public class WeChatMediaController {
    private final WeChatMediaService weChatMediaService;

    @Operation(summary = "非图文素材分页")
    @GetMapping("/pageFile")
    public ResResult<PageResult<WeChatMediaDto>> pageFile(PageQuery PageQuery, String type){
        return Res.ok(weChatMediaService.pageFile(PageQuery,type));
    }

    @Operation(summary = "图文素材分页")
    @GetMapping("/pageNews")
    public ResResult<PageResult<WxMaterialNewsBatchGetNewsItem>> pageNews(PageQuery PageQuery){
        return Res.ok(weChatMediaService.pageNews(PageQuery));
    }

    @Operation(summary = "删除素材")
    @DeleteMapping("/deleteFile")
    public ResResult<Void> deleteFile(String mediaId){
        weChatMediaService.deleteFile(mediaId);
        return Res.ok();
    }
    
    @Operation(summary = "上传素材")
    @PostMapping("/uploadFile")
    public ResResult<Void> uploadFile(String mediaType, MultipartFile file){
        weChatMediaService.uploadFile(mediaType,file);
        return Res.ok();
    }
}
