package com.taotao.cloud.maven.plugin.aa;

import org.elasticsearch.plugins.Plugin;

public class MyPlugin11 extends Plugin {

	@Override
	public void onModule(RestModule restModule) {
		restModule.addRestAction(MyRestAction.class);
	}
}
