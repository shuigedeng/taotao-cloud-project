package com.taotao.cloud.operation.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.common.enums.ClientTypeEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 专题活动
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = Special.TABLE_NAME)
@TableName(Special.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = Special.TABLE_NAME, comment = "专题活动表")
public class Special extends BaseSuperEntity<Special, Long> {

	public static final String TABLE_NAME = "li_special";

	@Schema(description = "专题活动名称")
	private String specialName;

	/**
	 * @see ClientTypeEnum
	 */
	@Schema(description = "楼层对应连接端类型", allowableValues = "PC,H5,WECHAT_MP,APP")
	private String clientType;

	@Schema(description = "页面ID")
	private String pageDataId;
}
