package com.taotao.cloud.sys.biz.execl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * 在service
 *
 * @Upload(type = UploadType.类型1)
 * public String upload(List<ClassOne> items)  {
 *    if (items == null || items.size() == 0) {
 *       return;
 *    }
 *    //校验
 *    String error = uploadCheck(items);
 *    if (StringUtils.isNotEmpty) {
 *        return error;
 *    }
 *    //删除旧的
 *    deleteAll();
 *    //插入新的
 *    batchInsert(items);
 * }
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Upload {
   // 记录上传类型
   UploadType type() default UploadType.未知;
}
