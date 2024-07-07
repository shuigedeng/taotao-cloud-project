package com.xx.plugin.es.enc.character;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;
import org.elasticsearch.index.analysis.AbstractCharFilterFactory;
import org.elasticsearch.index.analysis.NormalizingCharFilterFactory;

import java.io.Reader;

/**
 * 参考：
 * org.elasticsearch.analysis.common.PatternReplaceCharFilterFactory
 * org.elasticsearch.analysis.common.MappingCharFilterFactory
 */
public class EncCharFilterFactory extends AbstractCharFilterFactory implements NormalizingCharFilterFactory {

    private EncNormalizer normalizer = null;


    public EncCharFilterFactory(IndexSettings indexSettings, String name) {
        super(indexSettings, name);
        normalizer = new EncNormalizerImpl();
    }

    public EncCharFilterFactory(IndexSettings indexSettings, Environment environment, String name, Settings settings) {
        super(indexSettings, name);
        normalizer = new EncNormalizerImpl();
    }

    @Override
    public Reader create(Reader reader) {
        return new EncCharFilter(reader, normalizer);
    }
}

