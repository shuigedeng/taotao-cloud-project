package com.taotao.cloud.maven.plugin.aa;

import java.util.Map;
import java.util.TreeMap;
import org.elasticsearch.indices.analysis.AnalysisModule;
import org.elasticsearch.plugins.AnalysisPlugin;
import org.elasticsearch.plugins.MapperPlugin;
import org.elasticsearch.plugins.Plugin;

public class EncPlugin extends Plugin implements AnalysisPlugin, MapperPlugin {

	/**
	 * CharFilter
	 *
	 * @return
	 */
	@Override
	public Map<String, AnalysisModule.AnalysisProvider<CharFilterFactory>> getCharFilters() {
		Map<String, AnalysisModule.AnalysisProvider<CharFilterFactory>> filters = new TreeMap<>();
		filters.put("enc_filter", EncCharFilterFactory::new);
		return filters;
	}
}

