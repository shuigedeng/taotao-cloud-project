package com.taotao.cloud.doris.udf;

import org.apache.hadoop.hive.ql.exec.UDF;

public class AddOne extends UDF {
    public Integer evaluate(Integer value) {
        return value == null? null: value + 1;
    }
}
