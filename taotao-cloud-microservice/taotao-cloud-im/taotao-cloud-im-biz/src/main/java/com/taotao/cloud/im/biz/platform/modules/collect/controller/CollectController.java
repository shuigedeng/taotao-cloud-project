package com.taotao.cloud.im.biz.platform.modules.collect.controller;

import com.platform.common.version.ApiVersion;
import com.platform.common.version.VersionEnum;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.page.TableDataInfo;
import com.platform.modules.collect.domain.ChatCollect;
import com.platform.modules.collect.service.ChatCollectService;
import com.platform.modules.collect.vo.CollectVo01;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 收藏
 */
@RestController
@Slf4j
@RequestMapping("/collect")
public class CollectController extends BaseController {

    @Resource
    private ChatCollectService collectService;

    /**
     * 增加
     */
    @ApiVersion(VersionEnum.V1_0_0)
    @PostMapping("/add")
    public AjaxResult addCollect(@Validated @RequestBody CollectVo01 collectVo) {
        collectService.addCollect(collectVo);
        return AjaxResult.success();
    }

    /**
     * 删除
     */
    @ApiVersion(VersionEnum.V1_0_0)
    @GetMapping("/remove/{collectId}")
    public AjaxResult remove(@PathVariable Long collectId) {
        collectService.deleteCollect(collectId);
        return AjaxResult.success();
    }

    /**
     * 列表
     */
    @ApiVersion(VersionEnum.V1_0_0)
    @GetMapping("/list")
    public TableDataInfo list(ChatCollect collect) {
        startPage("create_time desc");
        return getDataTable(collectService.collectList(collect));
    }

}
