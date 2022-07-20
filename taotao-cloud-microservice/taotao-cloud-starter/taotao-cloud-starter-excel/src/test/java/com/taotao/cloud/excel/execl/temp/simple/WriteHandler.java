package com.taotao.cloud.excel.execl.temp.simple;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.taotao.cloud.common.utils.log.LogUtil;

/**

 */

public class WriteHandler implements SheetWriteHandler {

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder,
        WriteSheetHolder writeSheetHolder) {
        LogUtil.info("锁住");
        writeSheetHolder.getSheet().protectSheet("edit");
    }
}
