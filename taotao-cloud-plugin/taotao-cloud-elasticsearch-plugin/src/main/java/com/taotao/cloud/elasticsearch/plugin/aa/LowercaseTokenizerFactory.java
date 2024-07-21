package com.taotao.cloud.elasticsearch.plugin.aa;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;
import org.elasticsearch.index.analysis.AbstractTokenizerFactory;

import java.io.IOException;

public class LowercaseTokenizerFactory extends AbstractTokenizerFactory {
 
    public LowercaseTokenizerFactory(IndexSettings indexSettings, Environment environment, String name, Settings settings) {
        super(indexSettings, settings, name);
    }
 
    @Override
    public Tokenizer create() {
        return new LowercaseTokenizer();
    }
 
    private static class LowercaseTokenizer extends Tokenizer {
 
        private CharTermAttribute termAttr;
 
        LowercaseTokenizer() {
            termAttr = addAttribute(CharTermAttribute.class);
        }
 
        @Override
        public boolean incrementToken() throws IOException {
            clearAttributes();
            if (input.incrementToken()) {
                String term = termAttr.toString().toLowerCase();
                termAttr.setEmpty().append(term);
                return true;
            } else {
                return false;
            }
        }
    }
}
