package com.taotao.cloud.sys.biz.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * pv 与 ip 统计
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 21:10:22
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = Visits.TABLE_NAME)
@TableName(Visits.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = Visits.TABLE_NAME, comment = "pv与ip统计")
public class Visits extends BaseSuperEntity<Visits, Long> {

	public static final String TABLE_NAME = "tt_visits";

	@Column(name = "date", nullable = false, columnDefinition = "varchar(64) not null comment '日期'")
	private String date;

	@Column(name = "pv_counts", nullable = false, columnDefinition = "bigint not null default 0 comment 'pv'")
	private Long pvCounts;

	@Column(name = "ip_counts", nullable = false, columnDefinition = "bigint not null default 0 comment 'ip'")
	private Long ipCounts;

	@Column(name = "week_day", nullable = false, columnDefinition = "varchar(64) not null comment '天'")
	private String weekDay;
}
