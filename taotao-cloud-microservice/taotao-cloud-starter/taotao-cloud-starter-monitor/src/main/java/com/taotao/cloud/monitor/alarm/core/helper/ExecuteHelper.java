package com.taotao.cloud.monitor.alarm.core.helper;

import com.taotao.cloud.monitor.alarm.core.execut.SimpleExecuteFactory;
import com.taotao.cloud.monitor.alarm.core.execut.api.IExecute;
import com.taotao.cloud.monitor.alarm.core.execut.spi.NoneExecute;
import java.util.Collections;
import java.util.List;

public class ExecuteHelper {

    public static ExecuteHelper DEFAULT_EXECUTE = new ExecuteHelper(SimpleExecuteFactory.getExecute(
	    NoneExecute.NAME), Collections.emptyList());

    private IExecute iExecute;

    private List<String> users;

    public ExecuteHelper(IExecute iExecute, List<String> users) {
        this.iExecute = iExecute;
        this.users = users;
    }

	public static ExecuteHelper getDefaultExecute() {
		return DEFAULT_EXECUTE;
	}

	public static void setDefaultExecute(
		ExecuteHelper defaultExecute) {
		DEFAULT_EXECUTE = defaultExecute;
	}

	public IExecute getIExecute() {
		return iExecute;
	}

	public void setiExecute(IExecute iExecute) {
		this.iExecute = iExecute;
	}

	public List<String> getUsers() {
		return users;
	}

	public void setUsers(List<String> users) {
		this.users = users;
	}
}
