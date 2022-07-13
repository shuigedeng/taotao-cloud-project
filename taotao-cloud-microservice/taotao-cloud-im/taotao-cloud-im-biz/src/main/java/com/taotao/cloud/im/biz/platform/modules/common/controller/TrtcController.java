package com.taotao.cloud.im.biz.platform.modules.common.controller;

import com.platform.common.version.ApiVersion;
import com.platform.common.version.VersionEnum;
import com.platform.common.web.domain.AjaxResult;
import com.platform.modules.common.service.TrtcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 实时语音/视频
 */
@RestController
@RequestMapping("/trtc")
@Slf4j
public class TrtcController {

    @Resource
    private TrtcService trtcService;

    /**
     * 获取签名
     */
    @ApiVersion(VersionEnum.V1_0_0)
    @GetMapping("/getSign")
    public AjaxResult getSign() {
        return AjaxResult.success(trtcService.getSign());
    }

}
