package com.taotao.cloud.workflow.api.model.visiual;

import com.taotao.cloud.workflow.api.model.visiual.fields.FieLdsModel;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecursionForm {

	private List<FieLdsModel> list;
	private List<TableModel> tableModelList;

}
