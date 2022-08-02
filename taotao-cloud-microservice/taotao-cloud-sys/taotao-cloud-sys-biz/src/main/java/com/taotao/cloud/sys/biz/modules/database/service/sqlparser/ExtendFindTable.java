package com.taotao.cloud.sys.biz.modules.database.service.sqlparser;

import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 对查找到的数据表进行扩展, 拿出数据表的列信息
 */
@Data
public class ExtendFindTable {
    private FindTable findTable;
    private TableMetaData tableMeta;

    public ExtendFindTable() {
    }

    public ExtendFindTable(FindTable findTable) {
        this.findTable = findTable;
    }

}
