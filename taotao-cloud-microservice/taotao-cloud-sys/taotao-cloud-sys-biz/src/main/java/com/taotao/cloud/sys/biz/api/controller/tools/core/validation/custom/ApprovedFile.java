package com.taotao.cloud.sys.biz.api.controller.tools.core.validation.custom;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 *  文件类型的验证有点像 nginx 的主机过滤规则 allow 和 deny
 *  但这里是先顺序执行 allow ,然后顺序执行 deny 只要有匹配就跳出
 *  支持通配符 * ,例如 * 或者 text/* 或 image/*
 */
@Documented
@Constraint(validatedBy = {ApprovedFileValidator.class})
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApprovedFile {
    String message() default "{sanri.webui.validator.constraints.approvedFile}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    /**
     * 允许的文件类型列表,验证规则见文档注释
     * 这里写的是 mime 类型列表
     * @return
     */
    String [] allowFileTypes() default {"*"};

    /**
     * 拒绝的文件类型列表,验证规则见文档注释
     * 这里写的是 mime 类型列表
     * @return
     */
    String [] denyFileTypes() default {"*"};

    /**
     * 检查魔术数字是否满足
     * @return
     */
    boolean checkMagic() default false;

    /**
     * 允许文件最大大小，-1 表示无限制
     * @return
     */
    long maxFileSize() default -1;
}
