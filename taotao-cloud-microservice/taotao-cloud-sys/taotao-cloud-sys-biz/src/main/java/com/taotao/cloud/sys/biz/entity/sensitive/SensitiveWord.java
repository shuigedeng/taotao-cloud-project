package com.taotao.cloud.sys.biz.entity.sensitive;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 敏感词实体
 */
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

	public String getSensitiveWord() {
		return sensitiveWord;
	}

	public void setSensitiveWord(String sensitiveWord) {
		this.sensitiveWord = sensitiveWord;
	}
}
