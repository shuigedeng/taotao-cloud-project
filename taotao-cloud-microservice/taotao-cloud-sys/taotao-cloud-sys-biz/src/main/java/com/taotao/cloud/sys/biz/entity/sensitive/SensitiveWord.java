package com.taotao.cloud.sys.biz.entity.sensitive;

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
 * 敏感词实体
 * @author dengtao
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = SensitiveWord.TABLE_NAME)
@TableName(SensitiveWord.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = SensitiveWord.TABLE_NAME, comment = "敏感词表")
public class SensitiveWord extends BaseSuperEntity<SensitiveWord, Long> {

	public static final String TABLE_NAME = "tt_sensitive_words";

	/**
	 * 敏感词名称
	 */
	@Column(name = "sensitive_word", nullable = false, columnDefinition = "varchar(255) not null default '' comment '敏感词名称'")
	private String sensitiveWord;
}
