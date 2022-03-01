package com.taotao.cloud.sys.biz.tools.core.security.entitys;

import org.springframework.beans.BeanUtils;

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

	public String getRouteName() {
		return routeName;
	}

	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}

	public String getPluginName() {
		return pluginName;
	}

	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}
}
