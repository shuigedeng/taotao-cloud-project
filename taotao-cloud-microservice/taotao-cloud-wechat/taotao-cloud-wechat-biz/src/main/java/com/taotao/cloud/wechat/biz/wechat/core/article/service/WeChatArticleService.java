package com.taotao.cloud.wechat.biz.wechat.core.article.service;

import cn.bootx.common.core.rest.PageResult;
import cn.bootx.common.core.rest.param.PageQuery;
import cn.bootx.starter.wechat.dto.article.WeChatArticleDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 *
 * @author xxm
 * @date 2022/8/11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatArticleService {
    private final WxMpService wxMpService;


    /**
     * 查询图文
     *
     * @return
     */
    @SneakyThrows
    public PageResult<WeChatArticleDto> page(PageQuery PageQuery){
        val freePublishService = wxMpService.getFreePublishService();
        val result = freePublishService.getPublicationRecords(PageQuery.start(), PageQuery.getSize());
        val items = result.getItems().stream()
                .map(WeChatArticleDto::init)
                .collect(Collectors.toList());
        PageResult<WeChatArticleDto> pageResult = new PageResult<>();
        pageResult.setCurrent(PageQuery.getCurrent())
                .setRecords(items)
                .setSize(PageQuery.getSize())
                .setTotal(result.getTotalCount());
        return pageResult;
    }

}
