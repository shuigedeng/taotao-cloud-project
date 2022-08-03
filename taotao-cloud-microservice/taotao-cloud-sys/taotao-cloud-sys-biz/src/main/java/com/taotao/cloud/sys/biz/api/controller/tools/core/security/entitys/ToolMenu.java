package com.taotao.cloud.sys.biz.api.controller.tools.core.security.entitys;

import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class ToolMenu extends ToolResource{
    /**
     * 路由标识 , 由 / 开头
     */
    private String routeName;

    /**
     * 插件标识
     */
    private String pluginName;

    public ToolMenu(ToolResource toolResource) {
        BeanUtils.copyProperties(toolResource,this);
    }
}
