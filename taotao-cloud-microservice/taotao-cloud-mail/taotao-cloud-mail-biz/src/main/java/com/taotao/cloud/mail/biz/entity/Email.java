package com.taotao.cloud.mail.biz.entity;

import com.taotao.cloud.data.jpa.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 邮件信息表
 *
 * @author dengtao
 * @date 2020/11/13 09:46
 * @since v1.0
 */
@Data
@SuperBuilder
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tt_email")
@org.hibernate.annotations.Table(appliesTo = "tt_email", comment = "邮件信息表")
public class Email extends BaseEntity {

	private static final long serialVersionUID = 6887296988458221221L;

	/**
	 * 接收人邮箱(多个逗号分开)
	 */
	@Column(name = "receive_email", nullable = false, columnDefinition = "varchar(255) not null COMMENT '接收人邮箱(多个逗号分开)'")
	private String receiveEmail;

	/**
	 * 主题
	 */
	@Column(name = "subject", nullable = false, columnDefinition = "varchar(64) not null COMMENT '主题'")
	private String subject;

	/**
	 * 发送内容
	 */
	@Column(name = "content", nullable = false, columnDefinition = "varchar(2000) not null COMMENT '发送内容'")
	private String content;

	/**
	 * 模板
	 */
	@Column(name = "template", nullable = false, columnDefinition = "varchar(32) not null COMMENT '模板'")
	private String template;

	/**
	 * 发送时间
	 */
	@Column(name = "send_time", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP comment '发送时间'")
	private LocalDateTime sendTime;

}
