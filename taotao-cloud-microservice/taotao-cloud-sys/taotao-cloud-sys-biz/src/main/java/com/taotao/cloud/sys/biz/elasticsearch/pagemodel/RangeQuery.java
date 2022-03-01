package com.taotao.cloud.sys.biz.elasticsearch.pagemodel;

import java.util.List;

public class RangeQuery extends QueryCommand{
    private List<RangeValue>  rangeValues;

    public List<RangeValue> getRangeValues() {
        return rangeValues;
    }

    public void setRangeValues(List<RangeValue> rangeValues) {
        this.rangeValues = rangeValues;
    }
}
