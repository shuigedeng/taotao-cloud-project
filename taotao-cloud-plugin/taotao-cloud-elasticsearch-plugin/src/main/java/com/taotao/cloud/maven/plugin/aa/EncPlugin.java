package com.xx.plugin.es.enc;

import com.xx.plugin.es.enc.character.EncCharFilterFactory;
import com.xx.plugin.es.enc.token.EncCharTokenFilterFactory;
import org.elasticsearch.index.analysis.CharFilterFactory;
import org.elasticsearch.index.analysis.TokenFilterFactory;
import org.elasticsearch.indices.analysis.AnalysisModule;
import org.elasticsearch.plugins.AnalysisPlugin;
import org.elasticsearch.plugins.MapperPlugin;
import org.elasticsearch.plugins.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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

