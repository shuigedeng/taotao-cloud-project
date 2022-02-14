package com.taotao.cloud.sys.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * pv 与 ip 统计
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 21:10:22
 */
@Entity
@Table(name = Visits.TABLE_NAME)
@TableName(Visits.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = Visits.TABLE_NAME, comment = "pv与ip统计")
public class Visits extends BaseSuperEntity<Visits, Long> {

	public static final String TABLE_NAME = "tt_sys_visits";

	@Column(name = "date", nullable = false, columnDefinition = "varchar(64) not null comment '日期'")
	private String date;

	@Column(name = "pv_counts", nullable = false, columnDefinition = "bigint not null default 0 comment 'pv'")
	private Long pvCounts;

	@Column(name = "ip_counts", nullable = false, columnDefinition = "bigint not null default 0 comment 'ip'")
	private Long ipCounts;

	@Column(name = "week_day", nullable = false, columnDefinition = "varchar(64) not null comment '天'")
	private String weekDay;

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

	public String getWeekDay() {
		return weekDay;
	}

	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}
}
