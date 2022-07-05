package com.taotao.cloud.sys.biz.model.entity.sensitive;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/**
 * 敏感词实体
 *
 * @author shuigedeng
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = SensitiveWord.TABLE_NAME)
@TableName(SensitiveWord.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = SensitiveWord.TABLE_NAME, comment = "敏感词表")
public class SensitiveWord extends BaseSuperEntity<SensitiveWord, Long> {

	public static final String TABLE_NAME = "tt_sensitive_words";

	/**
	 * 敏感词名称
	 */
	@Column(name = "sensitive_word", columnDefinition = "varchar(255) not null default '' comment '敏感词名称'")
	private String sensitiveWord;
	@Builder
	public SensitiveWord(Long id, LocalDateTime createTime, Long createBy,
		LocalDateTime updateTime, Long updateBy, Integer version, Boolean delFlag,
		String sensitiveWord) {
		super(id, createTime, createBy, updateTime, updateBy, version, delFlag);
		this.sensitiveWord = sensitiveWord;
	}

	@Override
	public boolean equals(Object o) {
				if (this == o) {
			return true;
		}
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
			return false;
		}
		SensitiveWord that = (SensitiveWord) o;
		return getId() != null && Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
