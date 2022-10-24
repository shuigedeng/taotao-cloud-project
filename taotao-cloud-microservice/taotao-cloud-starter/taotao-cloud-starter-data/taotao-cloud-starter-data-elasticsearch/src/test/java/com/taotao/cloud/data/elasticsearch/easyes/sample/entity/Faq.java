package com.taotao.cloud.data.elasticsearch.easyes.sample.entity;

import com.xpc.easyes.core.anno.TableField;
import com.xpc.easyes.core.anno.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件描述
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("faq")
public class Faq {
    @TableField("faq_name")
    private String faqName;
    private String faqAnswer;
}
