package com.taotao.cloud.workflow.biz.app.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Map;
import jnpf.base.ActionResult;
import jnpf.base.SysConfigApi;
import jnpf.model.BaseSystemInfo;
import jnpf.util.NoDataSourceBind;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 获取AppVersion
 *
 * @author ：JNPF开发平台组
 * @version: V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date ：2022/3/31 11:26
 */
@Api(tags = "获取APP版本号", value = "AppVersion")
@RestController
@RequestMapping("/app")
public class AppVersionController {

    @Autowired
    private SysConfigApi sysConfigApi;

    /**
     * 判断是否需要验证码
     *
     * @return
     */
    @NoDataSourceBind
    @ApiOperation("判断是否需要验证码")
    @GetMapping("/Version")
    public ActionResult getAppVersion() {
        BaseSystemInfo sysConfigInfo = sysConfigApi.getSysConfigInfo();
        String sysVersion = "";
        Map<String, String> map = new HashedMap<>();
        if (sysConfigInfo != null) {
            sysVersion = sysConfigInfo.getSysVersion();
        }
        map.put("sysVersion", sysVersion);
        return ActionResult.success(map);
    }
}
