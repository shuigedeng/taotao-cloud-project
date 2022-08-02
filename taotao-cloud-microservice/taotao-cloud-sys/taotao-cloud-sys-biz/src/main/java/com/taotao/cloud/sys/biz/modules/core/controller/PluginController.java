package com.taotao.cloud.sys.biz.modules.core.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.taotao.cloud.sys.biz.modules.core.service.plugin.PluginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/plugin")
@Validated
public class PluginController {
    @Autowired
    private PluginManager pluginManager;

    /**
     * 获取插件列表,经过排序的
     * @return
     */
    @GetMapping("/list")
    public List<PluginManager.EnhancePlugin> plugins() throws IOException {
        return pluginManager.list();
    }

    /**
     * 访问某个插件
     * @param pluginId 插件标识
     */
    @GetMapping("/visited")
    public void visited(@NotNull String pluginId){
        pluginManager.visitedPlugin(pluginId);
    }

    /**
     * 获取插件详情
     * @param pluginId 插件标识
     * @return
     */
    @GetMapping("/detail")
    public PluginManager.PluginWithHelpContent detail(@NotNull String pluginId) throws IOException {
        return pluginManager.detailWithHelpContent(pluginId);
    }

    /**
     * 将当前访问量和访问次数进行序列化
     */
    @GetMapping("/serializer")
    public void serializer(){
        pluginManager.serializer();
    }
}
