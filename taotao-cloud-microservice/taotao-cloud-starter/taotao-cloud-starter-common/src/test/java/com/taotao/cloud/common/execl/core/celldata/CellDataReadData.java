package com.taotao.cloud.common.execl.core.celldata;

import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.metadata.data.ReadCellData;


public class CellDataReadData {
    @DateTimeFormat("yyyy年MM月dd日")
    private ReadCellData<String> date;
    private ReadCellData<Integer> integer1;
    private Integer integer2;
    private ReadCellData<?> formulaValue;
}
