package com.taotao.cloud.sensitive.sensitive.word.support.result;


import com.taotao.cloud.sensitive.sensitive.word.api.IWordResult;

/**
 */
public class WordResult implements IWordResult {

    private String word;

    private int startIndex;

    private int endIndex;

    public static WordResult newInstance() {
        return new WordResult();
    }

    @Override
    public String word() {
        return word;
    }

    public WordResult word(String word) {
        this.word = word;
        return this;
    }

    @Override
    public int startIndex() {
        return startIndex;
    }

    public WordResult startIndex(int startIndex) {
        this.startIndex = startIndex;
        return this;
    }

    @Override
    public int endIndex() {
        return endIndex;
    }

    public WordResult endIndex(int endIndex) {
        this.endIndex = endIndex;
        return this;
    }

    @Override
    public String toString() {
        return "WordResult{" +
                "word='" + word + '\'' +
                ", startIndex=" + startIndex +
                ", endIndex=" + endIndex +
                '}';
    }

}
