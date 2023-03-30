/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.sys.biz.aop.execl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在service
 *
 * <pre class="code">
 * &#064;ExcelUpload(type  = UploadType.类型1)
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
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ExcelUpload {
    // 记录上传类型
    ExcelUploadType type() default ExcelUploadType.未知;
}
