package com.taotao.cloud.job.worker.processor.factory;

import com.google.common.collect.Sets;
import com.taotao.cloud.job.common.enums.ProcessorType;
import com.taotao.cloud.job.worker.processor.ProcessorBean;
import com.taotao.cloud.job.worker.processor.ProcessorDefinition;
import com.taotao.cloud.job.worker.processor.type.BasicProcessor;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * 内建的默认处理器工厂，通过全限定类名加载处理器，但无法享受 IOC 框架的 DI 功能
 *
 * @author tjq
 * @since 2023/1/17
 */
@Slf4j
public class BuiltInDefaultProcessorFactory implements ProcessorFactory {

	@Override
	public Set<String> supportTypes() {
		return Sets.newHashSet(ProcessorType.BUILT_IN.name());
	}

	@Override
	public ProcessorBean build(ProcessorDefinition processorDefinition) {

		String className = processorDefinition.getProcessorInfo();

		try {
			Class<?> clz = Class.forName(className);
			BasicProcessor basicProcessor = (BasicProcessor) clz.getDeclaredConstructor().newInstance();
			return new ProcessorBean()
				.setProcessor(basicProcessor)
				.setClassLoader(basicProcessor.getClass().getClassLoader());
		} catch (Exception e) {
			log.warn("[ProcessorFactory] load local Processor(className = {}) failed.", className, e);
		}
		return null;
	}
}
