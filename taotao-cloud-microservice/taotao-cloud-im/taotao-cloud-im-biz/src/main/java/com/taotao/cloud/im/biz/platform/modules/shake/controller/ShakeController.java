package com.taotao.cloud.im.biz.platform.modules.shake.controller;

import com.platform.common.version.ApiVersion;
import com.platform.common.version.VersionEnum;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.modules.shake.service.ShakeService;
import com.platform.modules.shake.vo.ShakeVo01;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 摇一摇
 */
@RestController
@Slf4j
@RequestMapping("/shake")
public class ShakeController extends BaseController {

    @Resource
    private ShakeService shakeService;

    /**
     * 发送摇一摇
     *
     * @return 结果
     */
    @ApiVersion(VersionEnum.V1_0_0)
    @PostMapping(value = "/doShake")
    public AjaxResult sendShake(@Validated @RequestBody ShakeVo01 shakeVo) {
        return AjaxResult.success(shakeService.doShake(shakeVo));
    }

}
