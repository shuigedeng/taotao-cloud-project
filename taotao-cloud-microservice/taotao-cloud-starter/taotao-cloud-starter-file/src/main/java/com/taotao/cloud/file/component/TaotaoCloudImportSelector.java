/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.file.component;

import com.taotao.cloud.file.configuration.AliyunOssAutoConfiguration;
import com.taotao.cloud.file.configuration.FdfsDfsAutoConfiguration;
import com.taotao.cloud.file.configuration.LocalAutoConfiguration;
import com.taotao.cloud.file.configuration.NginxAutoConfiguration;
import com.taotao.cloud.file.configuration.QCloudAutoConfiguration;
import com.taotao.cloud.file.configuration.QiniuAutoConfiguration;
import com.taotao.cloud.file.configuration.UpYunAutoConfiguration;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.context.annotation.Primary;
import org.springframework.core.type.AnnotationMetadata;

import java.util.function.Predicate;

/**
 * @author dengtao
 * @since 2020/10/26 12:59
 * @version 1.0.0
 */
public class TaotaoCloudImportSelector implements ImportSelector {

	private static final String[] IMPORTS = {
		AliyunOssAutoConfiguration.class.getName(),
		FdfsDfsAutoConfiguration.class.getName(),
		LocalAutoConfiguration.class.getName(),
		NginxAutoConfiguration.class.getName(),
		QiniuAutoConfiguration.class.getName(),
		QCloudAutoConfiguration.class.getName(),
		QiniuAutoConfiguration.class.getName(),
		UpYunAutoConfiguration.class.getName()
	};


	@Override
	public String[] selectImports(AnnotationMetadata annotationMetadata) {
		return IMPORTS;
	}

	@Override
	public Predicate<String> getExclusionFilter() {
		return null;
	}
}
