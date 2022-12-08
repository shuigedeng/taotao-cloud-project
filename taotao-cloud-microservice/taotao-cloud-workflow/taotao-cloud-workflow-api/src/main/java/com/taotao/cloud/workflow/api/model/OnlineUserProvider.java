package com.taotao.cloud.workflow.api.model;

import java.util.ArrayList;
import java.util.List;

public class OnlineUserProvider {

	/**
	 * 在线用户
	 */
	private static List<OnlineUserModel> onlineUserList = new ArrayList<>();

	public static List<OnlineUserModel> getOnlineUserList() {
		return OnlineUserProvider.onlineUserList;
	}

	public static void addModel(OnlineUserModel model) {
		OnlineUserProvider.onlineUserList.add(model);
	}
}
