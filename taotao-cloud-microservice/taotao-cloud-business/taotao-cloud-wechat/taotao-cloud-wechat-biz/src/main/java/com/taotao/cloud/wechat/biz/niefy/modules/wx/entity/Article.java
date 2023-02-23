package com.taotao.cloud.wechat.biz.niefy.modules.wx.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.niefy.common.utils.Json;
import jakarta.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * cms文章
 */
@Data
@TableName("cms_article")
public class Article implements Serializable {

	private static final long serialVersionUID = 1L;
	@TableId(type = IdType.AUTO)
	private Long id;
	private int type;
	@TableField(insertStrategy = FieldStrategy.IGNORED)//title重复则不插入
	@NotEmpty(message = "标题不得为空")
	private String title;
	private String tags;
	private String summary;
	private String content;
	private String category;
	private String subCategory;
	private Date createTime;
	private Date updateTime;
	private int openCount;
	private String targetLink;
	private String image;

	@Override
	public String toString() {
		return Json.toJsonString(this);
	}
}