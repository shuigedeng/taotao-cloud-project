package com.taotao.cloud.sys.api.vo.setting;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 配置表
 */
@Data
@Builder
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
