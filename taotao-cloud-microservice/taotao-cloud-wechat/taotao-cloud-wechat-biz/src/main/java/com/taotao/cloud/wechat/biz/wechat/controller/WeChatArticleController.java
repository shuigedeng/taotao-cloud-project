package com.taotao.cloud.wechat.biz.wechat.controller;

import cn.bootx.common.core.rest.PageResult;
import cn.bootx.common.core.rest.Res;
import cn.bootx.common.core.rest.ResResult;
import cn.bootx.common.core.rest.param.PageParam;
import cn.bootx.starter.wechat.core.article.service.WeChatArticleService;
import cn.bootx.starter.wechat.dto.article.WeChatArticleDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author xxm
 * @date 2022/8/11
 */
@Tag(name = "微信文章管理")
@RestController
@RequestMapping("/wechat/article")
@RequiredArgsConstructor
public class WeChatArticleController {
    private final WeChatArticleService weChatArticleService;

    @Operation(summary = "分页")
    @GetMapping("/page")
    public ResResult<PageResult<WeChatArticleDto>> page(PageParam pageParam){
        return Res.ok(weChatArticleService.page(pageParam));
    }
}
