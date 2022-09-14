package com.taotao.cloud.sensitive.sensitive.sensitive.model.sensitive.entry;



import com.taotao.cloud.sensitive.sensitive.sensitive.annotation.SensitiveEntry;
import com.taotao.cloud.sensitive.sensitive.sensitive.core.api.strategory.StrategyChineseName;

import java.util.Arrays;
import java.util.List;

/**
 * 属性为列表，列表中放置的为基础属性
 */
public class UserEntryBaseType {


    @SensitiveEntry
    @Sensitive(strategy = StrategyChineseName.class)
    private List<String> chineseNameList;

    @SensitiveEntry
    @Sensitive(strategy = StrategyChineseName.class)
    private String[] chineseNameArray;

    public List<String> getChineseNameList() {
        return chineseNameList;
    }

    public void setChineseNameList(List<String> chineseNameList) {
        this.chineseNameList = chineseNameList;
    }

    public String[] getChineseNameArray() {
        return chineseNameArray;
    }

    public void setChineseNameArray(String[] chineseNameArray) {
        this.chineseNameArray = chineseNameArray;
    }

    @Override
    public String toString() {
        return "UserEntryBaseType{" +
                "chineseNameList=" + chineseNameList +
                ", chineseNameArray=" + Arrays.toString(chineseNameArray) +
                '}';
    }
}
