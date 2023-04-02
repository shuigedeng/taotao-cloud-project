package com.taotao.cloud.sys.biz.sensitive.controller.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;

/**
 * 
 * @author yweijian
 * @date 2022年9月19日
 * @version 2.0.0
 * @description
 */
@ApiModel(value = "敏感词参数")
public class FilterWords implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(name = "isMatched", value = "是否匹配到敏感词", accessMode = AccessMode.READ_ONLY)
    private Boolean isMatched = false;

    @ApiModelProperty(name = "words", value = "匹配到的词语", accessMode = AccessMode.READ_ONLY)
    private List<String> words;
    
    @ApiModelProperty(name = "total", value = "匹配到的词语总数", accessMode = AccessMode.READ_ONLY)
    private Integer total = 0;
    
    public Boolean getIsMatched() {
        return isMatched;
    }

    public void setIsMatched(Boolean isMatched) {
        this.isMatched = isMatched;
    }

    public List<String> getWords() {
        return words;
    }

    public void setWords(List<String> words) {
        this.words = words;
        if(this.words != null) {
            this.total = this.words.size();
            this.isMatched = this.words.size() > 0;
        }
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public void addWord(String word) {
        if(this.words == null) {
            this.words = new ArrayList<String>();
        }
        this.words.add(word);
        this.total = this.words.size();
        if(this.words.size() > 0) {
            this.isMatched = true;
        }
    }
    
    public String getWord(Integer index) {
        if(index >= 0 && index < this.total) {
            return this.words.get(index);
        }
        return "";
    }
}
