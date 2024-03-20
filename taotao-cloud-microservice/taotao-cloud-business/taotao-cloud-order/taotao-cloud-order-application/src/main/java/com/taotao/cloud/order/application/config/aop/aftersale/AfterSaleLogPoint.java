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

package com.taotao.cloud.order.application.config.aop.aftersale;

import java.lang.annotation.*;

/**
 * 售后日志注解
 * <pre>
 * {@code
 * //使用方式
 * @AfterSaleLogPoint(sn = "#rvt.sn", description = "'售后申请:售后编号['+#rvt.sn+']'")
 * }
 * </pre>
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-07 20:29:56
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AfterSaleLogPoint {

	/**
	 * 日志名称
	 */
	String description();

	/**
	 * 售后SN
	 */
	String sn();
}
