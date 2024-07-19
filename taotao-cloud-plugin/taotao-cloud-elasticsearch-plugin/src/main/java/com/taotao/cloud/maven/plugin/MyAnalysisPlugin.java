package com.taotao.cloud.maven.plugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.lucene.analysis.Analyzer;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;
import org.elasticsearch.index.analysis.AnalyzerProvider;
import org.elasticsearch.indices.analysis.AnalysisModule;
import org.elasticsearch.indices.analysis.AnalysisModule.AnalysisProvider;
import org.elasticsearch.plugins.AnalysisPlugin;
import org.elasticsearch.plugins.Plugin;

public class MyAnalysisPlugin extends Plugin implements AnalysisPlugin {

    @Override
    public Map<String, AnalysisModule.AnalysisProvider<TokenizerFactory>> getTokenizers() {
        Map<String, AnalysisModule.AnalysisProvider<TokenizerFactory>> extra = new HashMap<>();

        extra.put("demo_tokenizer", new AnalysisModule.AnalysisProvider<TokenizerFactory>() {
            @Override
            public TokenizerFactory get(IndexSettings indexSettings, Environment environment, String name, Settings settings) throws IOException {
                return MyTokenizerFactory.getTokenizerFactory(indexSettings, environment, name, settings);
            }
        });

        return extra;
    }
    @Override
    public Map<String, AnalysisProvider<AnalyzerProvider<? extends Analyzer>>> getAnalyzers() {
        Map<String, AnalysisModule.AnalysisProvider<AnalyzerProvider<? extends Analyzer>>> extra = new HashMap<>();

        extra.put("demo_analyzer", new AnalysisModule.AnalysisProvider() {
            @Override
            public Object get(IndexSettings indexSettings, Environment environment, String name, Settings settings) throws IOException {
                return MyAnalyzerProvider.getAnalyzerProvider(indexSettings, environment, name, settings);
            }
        });
        return extra;
    }
}
