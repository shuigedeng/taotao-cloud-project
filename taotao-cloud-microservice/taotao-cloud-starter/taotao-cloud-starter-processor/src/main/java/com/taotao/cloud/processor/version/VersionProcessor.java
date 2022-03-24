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
package com.taotao.cloud.processor.version;

import com.google.auto.service.AutoService;
import com.taotao.cloud.processor.annotation.Version;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import net.ltgt.gradle.incap.IncrementalAnnotationProcessor;
import net.ltgt.gradle.incap.IncrementalAnnotationProcessorType;

/**
 * VersionProcessor
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2021/03/11 14:11
 */
@SupportedAnnotationTypes("*")
@AutoService(Processor.class)
@IncrementalAnnotationProcessor(IncrementalAnnotationProcessorType.AGGREGATING)
public class VersionProcessor extends AbstractProcessor {

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latestSupported();
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		for (TypeElement currentAnnotation : annotations) {
			Name qualifiedName = currentAnnotation.getQualifiedName();
			if (qualifiedName.contentEquals("com.taotao.cloud.processor.annotation.Version")) {
				Set<? extends Element> annotatedElements = roundEnv
					.getElementsAnnotatedWith(currentAnnotation);
				for (Element element : annotatedElements) {
					Version v = element.getAnnotation(Version.class);
					int major = v.major();
					int minor = v.minor();
					if (major < 0 || minor < 0) {
						String errMsg =
							"Version cannot be negative. major = " + major + " minor = " + minor;
						Messager messager = this.processingEnv.getMessager();
						messager.printMessage(Diagnostic.Kind.ERROR, errMsg, element);
					}
				}
			}
		}
		return Boolean.TRUE;
	}
}
