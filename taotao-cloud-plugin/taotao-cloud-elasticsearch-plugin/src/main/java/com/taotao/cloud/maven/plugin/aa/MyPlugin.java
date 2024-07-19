package com.taotao.cloud.maven.plugin.aa;

import java.util.HashMap;
import java.util.Map;
import org.elasticsearch.index.IndexModule;
import org.elasticsearch.indices.analysis.AnalysisModule;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.plugins.SearchPlugin.AggregationSpec;

public class MyPlugin extends Plugin {

	@Override
	public void onModule(AnalysisModule analysisModule) {
		analysisModule.addTokenizer("lowercase", LowercaseTokenizerFactory::new);
	}

	@Override
	public void onIndexModule(IndexModule indexModule) {
		indexModule.addIndexStore("file", FileIndexStorePlugin::new);
	}

	@Override
	public Map<String, AggregationSpec> getAggregations() {
		Map<String, AggregationSpec> aggregations = new HashMap<>();
		aggregations.put("city_population",
			new CityPopulationAggregationPlugin.CityPopulationAggregationSpec());
		return aggregations;
	}

}
