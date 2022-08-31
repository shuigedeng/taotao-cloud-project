package com.taotao.cloud.office.easyexecl.temp.simple;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.taotao.cloud.common.utils.log.LogUtils;

/**

 */

public class WriteHandler implements SheetWriteHandler {

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder,
        WriteSheetHolder writeSheetHolder) {
        LogUtils.info("锁住");
        writeSheetHolder.getSheet().protectSheet("edit");
    }
}
