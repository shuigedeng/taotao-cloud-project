package com.taotao.cloud.sys.biz.modules.database.service.dtos.data;

import com.sanri.tools.modules.database.service.dtos.meta.TableRelation;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DirtyDataInfo {
    private TableRelation tableRelation;
    private List<DeleteItem> deleteItems = new ArrayList<>();

    public DirtyDataInfo() {
    }

    public DirtyDataInfo(TableRelation tableRelation) {
        this.tableRelation = tableRelation;
    }

    public void addDeleteItem(DeleteItem deleteItem){
        deleteItems.add(deleteItem);
    }

    @Data
    public static final class DeleteItem{
        private String querySql;
        private String deleteSql;
        private Long total;

        public DeleteItem(String querySql) {
            this.querySql = querySql;
        }

        public DeleteItem(String querySql, String deleteSql) {
            this.querySql = querySql;
            this.deleteSql = deleteSql;
        }
    }
}
