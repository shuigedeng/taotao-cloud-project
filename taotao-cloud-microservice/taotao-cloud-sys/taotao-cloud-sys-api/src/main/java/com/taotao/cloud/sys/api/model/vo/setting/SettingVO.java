package com.taotao.cloud.sys.api.model.vo.setting;

import java.time.LocalDateTime;

import lombok.*;

/**
 * 配置表
 */
@Data
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class SettingVO {

	private String settingValue;
	private String id;
	private LocalDateTime createTime;
	private Long createdBy;
	private LocalDateTime updateTime;
	private Long updateBy;
	private Integer version;
	private Boolean delFlag;
}
