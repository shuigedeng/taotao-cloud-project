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

package com.taotao.cloud.sys.infrastructure.persistent.dict.dataobject;

import io.soabase.recordbuilder.core.RecordBuilder;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 字典查询对象
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-23 08:49:51
 */
@RecordBuilder
public record DictDO(

	/** id */
	Long id,
	/** 字典名称 */
	String dictName,
	/** 字典编码 */
	String dictCode,
	/** 描述 */
	String description,
	/** 排序值 */
	Integer dictSort,
	/** 备注信息 */
	String remark,
	/** 创建时间 */
	LocalDateTime createTime,
	/** 最后修改时间 */
	LocalDateTime lastModifiedTime)
	implements Serializable {

	static final long serialVersionUID = 5126530068827085130L;
}
