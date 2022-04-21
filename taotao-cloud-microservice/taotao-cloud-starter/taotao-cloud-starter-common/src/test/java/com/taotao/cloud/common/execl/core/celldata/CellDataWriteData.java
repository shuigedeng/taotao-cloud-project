package com.taotao.cloud.common.execl.core.celldata;

import java.util.Date;

import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.metadata.data.WriteCellData;


public class CellDataWriteData {
    @DateTimeFormat("yyyy年MM月dd日")
    private WriteCellData<Date> date;
    private WriteCellData<Integer> integer1;
    private Integer integer2;
    private WriteCellData<?> formulaValue;
}
