package com.taotao.cloud.operation.biz.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.taotao.cloud.operation.api.enums.FeedbackTypeEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import com.taotao.cloud.web.enums.SensitiveStrategy;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 意见反馈
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = Feedback.TABLE_NAME)
@TableName(Feedback.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = Feedback.TABLE_NAME, comment = "意见反馈表")
public class Feedback extends BaseSuperEntity<Feedback, Long> {

	public static final String TABLE_NAME = "li_feedback";

	@CreatedDate
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "创建时间", hidden = true)
	private Date createTime;

	@Schema(description = "会员名称", hidden = true)
	private String userName;

	@Schema(description = "反馈内容")
	@NotEmpty(message = "反馈内容不能为空")
	@Length(max = 500, message = "反馈内容不能超过500个字符")
	private String context;

	@Schema(description = "手机号")
	@Length(max = 11, message = "手机号不能超过11位")
	@Sensitive(strategy = SensitiveStrategy.PHONE)
	private String mobile;

	@Schema(description = "图片，多个图片使用：(，)分割")
	@Length(max = 255, message = "图片上传太多啦，请选择删除掉")
	private String images;

	/**
	 * 类型
	 *
	 * @see FeedbackTypeEnum
	 */
	@Schema(description = "类型", allowableValues = "FUNCTION,OPTIMIZE,OTHER")
	private String type;

}
