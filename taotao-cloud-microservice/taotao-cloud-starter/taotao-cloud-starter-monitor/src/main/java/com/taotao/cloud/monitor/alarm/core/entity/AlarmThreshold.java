package com.taotao.cloud.monitor.alarm.core.entity;

import com.taotao.cloud.monitor.alarm.core.execut.api.IExecute;

import java.util.List;

public class AlarmThreshold implements Comparable<AlarmThreshold> {

    /**
     * 报警类型
     */
    private IExecute executor;


    /**
     * 晋升此报警的阀值
     *
     * 报警计数count >= min && count < max, 则选择
     */
    private int min;


    private int max;


    /**
     * 对应的报警用户
     */
    private List<String> users;


    @Override
    public int compareTo(AlarmThreshold o) {
        if (o == null) {
            return -1;
        }

        return min - o.getMin();
    }

	public IExecute getExecutor() {
		return executor;
	}

	public void setExecutor(IExecute executor) {
		this.executor = executor;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public List<String> getUsers() {
		return users;
	}

	public void setUsers(List<String> users) {
		this.users = users;
	}
}
