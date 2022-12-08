package com.taotao.cloud.workflow.api.model.app;

import java.util.List;
import lombok.Data;

@Data
public class AppUserVO {

	private AppInfoModel userInfo;
	private List<AppMenuModel> menuList;
	private List<AppFlowFormModel> flowFormList;
	private List<AppDataModel> appDataList;
}
