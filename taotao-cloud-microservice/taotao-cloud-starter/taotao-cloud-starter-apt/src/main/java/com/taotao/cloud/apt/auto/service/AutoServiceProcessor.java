/*
 * Copyright (c) 2019-2029, Dreamlu 卢春梦 (596392912@qq.com & www.dreamlu.net).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.apt.auto.service;

import com.google.auto.service.AutoService;
import com.taotao.cloud.apt.auto.common.AbstractMicaProcessor;
import com.taotao.cloud.apt.auto.common.MultiSetMap;
import net.ltgt.gradle.incap.IncrementalAnnotationProcessor;
import net.ltgt.gradle.incap.IncrementalAnnotationProcessorType;

import javax.annotation.processing.*;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.SimpleAnnotationValueVisitor8;
import javax.lang.model.util.Types;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * java spi 服务自动处理器 参考：google auto
 *
 * 
 */
@SupportedOptions("debug")
@AutoService(Processor.class)
@IncrementalAnnotationProcessor(IncrementalAnnotationProcessorType.AGGREGATING)
public class AutoServiceProcessor extends AbstractMicaProcessor {
	/**
	 * AutoService 注解名
	 */
	private static final String AUTO_SERVICE_NAME = com.taotao.cloud.apt.auto.annotation.AutoService.class.getName();
	/**
	 * spi 服务集合，key 接口 -> value 实现列表
	 */
	private final MultiSetMap<String, String> providers = new MultiSetMap<>();
	/**
	 * 元素辅助类
	 */
	private Elements elementUtils;

	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		super.init(processingEnv);
		elementUtils = processingEnv.getElementUtils();
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return Collections.singleton(AUTO_SERVICE_NAME);
	}

	@Override
	protected boolean processImpl(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		if (roundEnv.processingOver()) {
			generateConfigFiles();
		} else {
			processAnnotations(annotations, roundEnv);
		}
		return false;
	}

	private void processAnnotations(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		TypeElement autoService = elementUtils.getTypeElement(AUTO_SERVICE_NAME);
		Set<? extends Element> elementSet = roundEnv.getElementsAnnotatedWith(autoService);
		// 过滤 TypeElement
		Set<TypeElement> typeElementSet = elementSet.stream()
			.filter(this::isClass)
			.filter(e -> e instanceof TypeElement)
			.map(e -> (TypeElement) e)
			.collect(Collectors.toSet());

		// 如果为空直接跳出
		if (typeElementSet.isEmpty()) {
			log("Annotations elementSet is isEmpty");
			return;
		}

		log(annotations.toString());
		log(typeElementSet.toString());

		for (TypeElement typeElement : typeElementSet) {
			AnnotationMirror annotationMirror = getAnnotation(elementUtils, typeElement, AUTO_SERVICE_NAME);
			if (annotationMirror == null) {
				continue;
			}
			Set<TypeMirror> typeMirrors = getValueFieldOfClasses(annotationMirror);
			if (typeMirrors.isEmpty()) {
				error("No service interfaces provided for element!", typeElement, annotationMirror);
				continue;
			}
			// 接口的名称
			String providerImplementerName = getQualifiedName(typeElement);
			for (TypeMirror typeMirror : typeMirrors) {
				String providerInterfaceName = getType(typeMirror);
				log("provider interface: " + providerInterfaceName);
				log("provider implementer: " + providerImplementerName);

				if (checkImplementer(typeElement, typeMirror)) {
					providers.put(providerInterfaceName, getQualifiedName(typeElement));
				} else {
					String message = "ServiceProviders must implement their service provider interface. "
						+ providerImplementerName + " does not implement " + providerInterfaceName;
					error(message, typeElement, annotationMirror);
				}
			}
		}
	}

	private void generateConfigFiles() {
		Filer filer = processingEnv.getFiler();
		for (String providerInterface : providers.keySet()) {
			String resourceFile = "META-INF/services/" + providerInterface;
			log("Working on resource file: " + resourceFile);
			try {
				SortedSet<String> allServices = new TreeSet<>();
				// 1. 存在用户手动编写的配置
				try {
					FileObject existingFile = filer.getResource(StandardLocation.SOURCE_OUTPUT, "", resourceFile);
					log("Looking for existing resource file at " + existingFile.toUri());
					Set<String> oldServices = ServicesFiles.readServiceFile(existingFile, elementUtils);
					log("Existing service entries: " + oldServices);
					allServices.addAll(oldServices);
				} catch (IOException e) {
					log("Resource file did not already exist.");
				}
				// 2. 增量编译
				try {
					FileObject existingFile = filer.getResource(StandardLocation.CLASS_OUTPUT, "", resourceFile);
					log("Looking for existing resource file at " + existingFile.toUri());
					Set<String> oldServices = ServicesFiles.readServiceFile(existingFile, elementUtils);
					log("Existing service entries: " + oldServices);
					allServices.addAll(oldServices);
				} catch (IOException e) {
					log("Resource file did not already exist.");
				}
				Set<String> newServices = new HashSet<>(providers.get(providerInterface));
				if (allServices.containsAll(newServices)) {
					log("No new service entries being added.");
					return;
				}
				// 3. 注解处理器新扫描出来的
				allServices.addAll(newServices);
				log("New service file contents: " + allServices);
				FileObject fileObject = filer.createResource(StandardLocation.CLASS_OUTPUT, "", resourceFile);
				try (OutputStream out = fileObject.openOutputStream()) {
					ServicesFiles.writeServiceFile(allServices, out);
				}
				log("Wrote to: " + fileObject.toUri());
			} catch (IOException e) {
				fatalError("Unable to create " + resourceFile + ", " + e);
				return;
			}
		}
	}

	/**
	 * Verifies {@link java.util.spi.LocaleServiceProvider} constraints on the concrete provider class.
	 * Note that these constraints are enforced at runtime via the ServiceLoader,
	 * we're just checking them at compile time to be extra nice to our users.
	 */
	private boolean checkImplementer(Element providerImplementer, TypeMirror providerType) {
		// TODO: We're currently only enforcing the subtype relationship
		// constraint. It would be nice to enforce them all.
		Types types = processingEnv.getTypeUtils();
		return types.isSubtype(providerImplementer.asType(), providerType);
	}

	/**
	 * 读取 AutoService 上的 value 值
	 *
	 * @param annotationMirror AnnotationMirror
	 * @return value 集合
	 */
	private Set<TypeMirror> getValueFieldOfClasses(AnnotationMirror annotationMirror) {
		return getAnnotationValue(annotationMirror, "value")
			.accept(new SimpleAnnotationValueVisitor8<Set<TypeMirror>, Void>() {
				@Override
				public Set<TypeMirror> visitType(TypeMirror typeMirror, Void v) {
					return Collections.singleton(typeMirror);
				}

				@Override
				public Set<TypeMirror> visitArray(
					List<? extends AnnotationValue> values, Void v) {
					return values.stream()
						.flatMap(value -> value.accept(this, null).stream())
						.collect(Collectors.toSet());
				}
			}, null);
	}


	public AnnotationValue getAnnotationValue(AnnotationMirror annotationMirror, String elementName) {
		Objects.requireNonNull(annotationMirror);
		Objects.requireNonNull(elementName);
		for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : elementUtils.getElementValuesWithDefaults(annotationMirror).entrySet()) {
			if (entry.getKey().getSimpleName().contentEquals(elementName)) {
				return entry.getValue();
			}
		}
		String annotationName = annotationMirror.getAnnotationType().toString();
		throw new IllegalArgumentException(String.format("@%s does not define an element %s()", annotationName, elementName));
	}

	public String getType(TypeMirror type) {
		if (type == null) {
			return null;
		}
		if (type instanceof DeclaredType) {
			DeclaredType declaredType = (DeclaredType) type;
			Element enclosingElement = declaredType.asElement().getEnclosingElement();
			if (enclosingElement instanceof TypeElement) {
				return getQualifiedName(enclosingElement) + "$" + declaredType.asElement().getSimpleName().toString();
			} else {
				return getQualifiedName(declaredType.asElement());
			}
		}
		return type.toString();
	}

}
