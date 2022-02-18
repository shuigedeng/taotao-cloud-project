package com.taotao.cloud.web.docx4j.input.builder.sheet;


import com.taotao.cloud.web.docx4j.input.InputConstants;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * excel单元格导入注解
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CellMeta {
    /**
     * 单元格名称
     * @return 列名称
     */
    String name();
    /**
     * 单元格索引 从0开始
     * @return 列索引
     */
    int index();
    /**
     * 非法格式提示信息 会自动拼接{@link #name()}为前缀
     * @return 非法格式提示信息
     */
    String message() default InputConstants.EMPTY;
    /**
     * 日期时间格式化
     * @return 格式化模式
     */
    String dateTimeFormat() default InputConstants.EMPTY;
}
