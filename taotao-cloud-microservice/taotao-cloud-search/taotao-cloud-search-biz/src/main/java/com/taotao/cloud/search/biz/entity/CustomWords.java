package com.taotao.cloud.search.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

/**
 * 自定义分词
 *
 * 
 * @since 2020/10/15
 **/
@Data
@TableName("li_custom_words")
@ApiModel(value = "自定义分词")
public class CustomWords extends BaseEntity {

    private static final long serialVersionUID = 650889506808657977L;

    /**
     * 名称
     */
    @Schema(description =  "名称")
    @NotEmpty(message = "分词名称必填")
    @Length(max = 20, message = "分词名称长度不能大于20")
    private String name;


    @Schema(description =  "是否禁用")
    private Integer disabled;


}
