package com.taotao.cloud.sys.api.vo.setting;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

/**
 * 配置表
 */
@Data
@Builder
public class SettingVO {

	private String settingValue;
	private String id;
	private LocalDateTime createTime;
	private Long createdBy;
	private LocalDateTime updateTime;
	private Long updateBy;
	private int version;
	private boolean delFlag;
}
