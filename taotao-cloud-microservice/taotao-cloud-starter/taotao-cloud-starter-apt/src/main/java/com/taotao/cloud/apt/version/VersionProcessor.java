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
package com.taotao.cloud.apt.version;

import com.google.auto.service.AutoService;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import com.taotao.cloud.apt.lombok.MySetterGetter;
import net.ltgt.gradle.incap.IncrementalAnnotationProcessor;
import net.ltgt.gradle.incap.IncrementalAnnotationProcessorType;

/**
 * VersionProcessor
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2021/03/11 14:11
 */
@SupportedAnnotationTypes("com.taotao.cloud.apt.version.Version")
@AutoService(Processor.class)
@IncrementalAnnotationProcessor(IncrementalAnnotationProcessorType.AGGREGATING)
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class VersionProcessor extends AbstractProcessor {

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		Messager messager = this.processingEnv.getMessager();
		messager.printMessage(Diagnostic.Kind.NOTE,"sldfkasldfkjl;asdkjf;l");

		Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(Version.class);
		for (Element element : annotatedElements) {
			messager.printMessage(Diagnostic.Kind.NOTE,element.getSimpleName());
			Version v = element.getAnnotation(Version.class);
			int major = v.major();
			int minor = v.minor();
			if (major < 0 || minor < 0) {
				String errMsg =
					"Version cannot be negative. major = " + major + " minor = " + minor;

				messager.printMessage(Diagnostic.Kind.ERROR, errMsg, element);
			}
		}

		messager.printMessage(Diagnostic.Kind.NOTE,"1111111111111");
		return true;
	}
}
