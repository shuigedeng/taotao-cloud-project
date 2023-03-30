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

package com.taotao.cloud.workflow.biz.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 任务调度注解 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface WorkflowTask {

    /** id */
    String id() default "";

    /**
     * 方法名（必填）
     *
     * @return
     */
    String fullName() default "";

    /**
     * 任务说明
     *
     * @return
     */
    String description() default "";

    /**
     * 表达式（必填）
     *
     * @return
     */
    String cron() default "";

    /**
     * 开始时间
     *
     * @return
     */
    String startDate() default "";

    /**
     * 结束时间
     *
     * @return
     */
    String endDate() default "";
}
