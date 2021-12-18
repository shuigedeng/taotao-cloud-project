package com.taotao.cloud.system.biz.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.taotao.cloud.web.base.entity.SuperEntity;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * pv 与 ip 统计
 *
 */
@TableName("visits")
public class Visits extends SuperEntity<Visits, Long> implements Serializable {

    @TableId
    private Long id;

    private String date;

    private Long pvCounts;

    private Long ipCounts;

    @TableField(fill = FieldFill.INSERT)
    private Timestamp createTime;

    private String weekDay;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Long getPvCounts() {
		return pvCounts;
	}

	public void setPvCounts(Long pvCounts) {
		this.pvCounts = pvCounts;
	}

	public Long getIpCounts() {
		return ipCounts;
	}

	public void setIpCounts(Long ipCounts) {
		this.ipCounts = ipCounts;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getWeekDay() {
		return weekDay;
	}

	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}
}
