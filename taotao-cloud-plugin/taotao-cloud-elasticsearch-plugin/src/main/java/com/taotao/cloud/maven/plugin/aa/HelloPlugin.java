package com.taotao.cloud.maven.plugin.aa;

import com.sun.source.util.Plugin;
import java.util.Collections;
import java.util.function.Supplier;

/**
 * @author caspar.chen
 * @date 2018/9/16
 **/
public class HelloPlugin extends Plugin implements ActionPlugin {


	@Override
	public List<RestHandler> getRestHandlers(Settings settings, RestController restController, ClusterSettings clusterSettings, IndexScopedSettings indexScopedSettings, SettingsFilter settingsFilter, IndexNameExpressionResolver indexNameExpressionResolver, Supplier<DiscoveryNodes> nodesInCluster) {
		return Collections.singletonList(new com.caspar.es.plugin.hello.HelloHandler(settings, restController));
	}

}
