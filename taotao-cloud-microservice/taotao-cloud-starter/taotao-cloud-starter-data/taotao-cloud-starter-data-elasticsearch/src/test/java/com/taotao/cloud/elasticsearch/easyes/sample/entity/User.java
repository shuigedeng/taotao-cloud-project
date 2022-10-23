package com.taotao.cloud.elasticsearch.easyes.sample.entity;

import com.xpc.easyes.core.anno.TableField;
import com.xpc.easyes.core.enums.FieldType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * es 嵌套类型
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @TableField("user_name")
    private String username;
    @TableField(exist = false)
    private Integer age;
    /**
     * 多级嵌套
     */
    @TableField(fieldType = FieldType.NESTED, nestedClass = Faq.class)
    private Set<Faq> faqs;
}
