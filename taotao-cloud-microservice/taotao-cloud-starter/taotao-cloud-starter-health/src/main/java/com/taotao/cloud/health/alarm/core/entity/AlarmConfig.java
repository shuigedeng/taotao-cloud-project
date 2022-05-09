package com.taotao.cloud.health.alarm.core.entity;

import com.taotao.cloud.health.alarm.core.execut.api.IExecute;
import java.util.List;

public class AlarmConfig {

    public static final int DEFAULT_MIN_NUM = 0;
    public static final int DEFAULT_MAX_NUM = 30;


    /**
     * 报警用户
     */
    private List<String> users;


    /**
     * 报警的阀值
     */
    private List<AlarmThreshold> alarmThreshold;


    /**
     * 最小的报警数
     */
    private int minLimit;


    /**
     * 最大的报警数
     */
    private int maxLimit;


    /**
     * 报警执行器
     */
    private IExecute executor;


    /**
     * true 表示当报警超过当前的阀值之后, 将提升报警的程度
     */
    private boolean autoIncEmergency;

	public List<String> getUsers() {
		return users;
	}

	public void setUsers(List<String> users) {
		this.users = users;
	}

	public List<AlarmThreshold> getAlarmThreshold() {
		return alarmThreshold;
	}

	public void setAlarmThreshold(
		List<AlarmThreshold> alarmThreshold) {
		this.alarmThreshold = alarmThreshold;
	}

	public int getMinLimit() {
		return minLimit;
	}

	public void setMinLimit(int minLimit) {
		this.minLimit = minLimit;
	}

	public int getMaxLimit() {
		return maxLimit;
	}

	public void setMaxLimit(int maxLimit) {
		this.maxLimit = maxLimit;
	}

	public IExecute getExecutor() {
		return executor;
	}

	public void setExecutor(IExecute executor) {
		this.executor = executor;
	}

	public boolean isAutoIncEmergency() {
		return autoIncEmergency;
	}

	public void setAutoIncEmergency(boolean autoIncEmergency) {
		this.autoIncEmergency = autoIncEmergency;
	}
}
