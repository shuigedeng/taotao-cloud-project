package com.taotao.cloud.maven.plugin;

import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;
import org.elasticsearch.index.analysis.AbstractTokenizerFactory;

public class MyTokenizerFactory extends AbstractTokenizerFactory {

    private MyConfiguration configuration;

    /**
     * 构造函数
     * @param indexSettings 索引配置
     * @param name 分析器或者分词器名称。如果是自定义分析器，则为自定义分析器名称
     * @param env es环境配置
     * @param settings 自定义分析器配置参数
     */
    public MyTokenizerFactory(IndexSettings indexSettings, String name, Environment env, Settings settings) {
        super(indexSettings,  settings, name);
        configuration = new MyConfiguration(indexSettings, name, env, settings);
    }

    public static TokenizerFactory getTokenizerFactory(IndexSettings indexSettings, Environment environment, String name, Settings settings) {
        return new MyTokenizerFactory(indexSettings, name, environment, settings).setSmart(false);
    }
    
    @Override
    public Tokenizer create() {
        return new MyTokenizer(configuration);
    }
}
