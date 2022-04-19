package com.taotao.cloud.sys.biz.entity.file;


import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

/**
 * 文件表
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/11/12 15:33
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = File.TABLE_NAME)
@TableName(File.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = File.TABLE_NAME, comment = "文件表")
public class File extends BaseSuperEntity<File, Long> {

	public static final String TABLE_NAME = "tt_file";

	/**
	 * 创建人
	 */
	@Column(name = "create_name", columnDefinition = "varchar(255) not null comment '创建人'")
	private String createName;

	/**
	 * 业务类型 供应商上传图片 供应商上传附件 商品上传图片
	 */
	@Column(name = "biz_type", columnDefinition = "varchar(255) not null comment '业务类型 '")
	private String bizType;

	/**
	 * 数据类型 {IMAGE:图片;VIDEO:视频;AUDIO:音频;DOC:文档;OTHER:其他}
	 */
	@Column(name = "data_type", columnDefinition = "varchar(255) not null comment '数据类型'")
	private String dataType;

	/**
	 * 原始文件名
	 */
	@Column(name = "original", columnDefinition = "varchar(255) not null comment '原始文件名'")
	private String original;

	/**
	 * 文件访问链接
	 */
	@Column(name = "url", columnDefinition = "varchar(255) not null comment '文件访问链接'")
	private String url;

	/**
	 * 文件md5值
	 */
	@Column(name = "md5", columnDefinition = "varchar(255) not null comment '文件md5值'")
	private String md5;

	/**
	 * 文件上传类型 取上传文件的值
	 */
	@Column(name = "type", columnDefinition = "varchar(255) not null comment '文件上传类型'")
	private String type;

	/**
	 * 文件上传类型 取上传文件的值
	 */
	@Column(name = "context_type", columnDefinition = "varchar(255) not null comment '文件上传类型'")
	private String contextType;

	/**
	 * 唯一文件名
	 */
	@Column(name = "name", columnDefinition = "varchar(255) not null comment '唯一文件名'")
	private String name;

	/**
	 * 后缀(没有.)
	 */
	@Column(name = "ext", columnDefinition = "varchar(255) not null comment '后缀'")
	private String ext;

	/**
	 * 大小
	 */
	@Column(name = "length", columnDefinition = "bigint null comment '大小'")
	private Long length;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(
			o)) {
			return false;
		}
		File file = (File) o;
		return getId() != null && Objects.equals(getId(), file.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
