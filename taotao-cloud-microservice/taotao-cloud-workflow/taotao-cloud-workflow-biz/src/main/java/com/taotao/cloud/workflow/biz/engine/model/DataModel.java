package com.taotao.cloud.workflow.biz.engine.model;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.swing.table.TableModel;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataModel {
    private Map<String,Object> dataNewMap;
    private List<FieLdsModel> fieLdsModelList;
    private List<TableModel> tableModelList;
    private String mainId;
    private DbLinkEntity link;
    private Boolean convert;

}
