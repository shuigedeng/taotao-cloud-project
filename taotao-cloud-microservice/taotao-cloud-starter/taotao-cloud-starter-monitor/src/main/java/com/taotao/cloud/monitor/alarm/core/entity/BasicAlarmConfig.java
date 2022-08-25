package com.taotao.cloud.monitor.alarm.core.entity;

import com.taotao.cloud.monitor.alarm.core.execut.spi.NoneExecute;

import java.util.Collections;
import java.util.List;

public class BasicAlarmConfig {

    /**
     * 报警用户
     */
    private List<String> users;


    /**
     * 报警的阀值, 当autoIncEmergency设置为false时, 此处可以不配置
     */
    private List<BasicAlarmThreshold> threshold;


    /**
     * 最小的报警数
     */
    private Integer min = AlarmConfig.DEFAULT_MIN_NUM;


    /**
     * 最大的报警数
     */
    private Integer max = AlarmConfig.DEFAULT_MAX_NUM;


    /**
     * 报警类型
     */
    private String level = NoneExecute.NAME;


    /**
     * true 表示当报警超过当前的阀值之后, 将提升报警的程度
     */
    private boolean autoIncEmergency = false;


    public List<BasicAlarmThreshold> getThreshold() {
        if(threshold == null) {
            return Collections.emptyList();
        }

        return threshold;
    }

	public List<String> getUsers() {
		return users;
	}

	public void setUsers(List<String> users) {
		this.users = users;
	}

	public void setThreshold(
		List<BasicAlarmThreshold> threshold) {
		this.threshold = threshold;
	}

	public Integer getMin() {
		return min;
	}

	public void setMin(Integer min) {
		this.min = min;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public boolean isAutoIncEmergency() {
		return autoIncEmergency;
	}

	public void setAutoIncEmergency(boolean autoIncEmergency) {
		this.autoIncEmergency = autoIncEmergency;
	}
}
