package com.taotao.cloud.workflow.biz.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.swing.table.TableModel;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecursionForm {
    private List<FieLdsModel> list;
    private List<TableModel> tableModelList;

}
