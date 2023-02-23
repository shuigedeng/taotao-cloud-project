package com.taotao.cloud.stock.api.common.util;

import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * Assembler class for the page.
 */
public class PageAssembler {
    public static Page toPage(IPage iPage) {
        Page page = new Page(iPage.getRecords(), iPage.getTotal(), iPage.getSize(), iPage.getCurrent());
        return page;
    }
}
