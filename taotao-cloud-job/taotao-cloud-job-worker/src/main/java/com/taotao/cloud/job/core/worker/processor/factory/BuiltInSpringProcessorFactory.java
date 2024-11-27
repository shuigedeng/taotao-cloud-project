package com.taotao.cloud.job.core.worker.processor.factory;

import lombok.extern.slf4j.Slf4j;
import com.taotao.cloud.worker.processor.ProcessorBean;
import com.taotao.cloud.worker.processor.ProcessorDefinition;
import com.taotao.cloud.worker.processor.type.BasicProcessor;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

/**
 * 内建的 SpringBean 处理器工厂，用于加载 Spring 相关的Bean，非核心依赖
 *
 * @author shuigedeng
 * @since 2023/1/17
 */
@Slf4j
public class BuiltInSpringProcessorFactory extends AbstractBuildInSpringProcessorFactory {


    public BuiltInSpringProcessorFactory(ApplicationContext applicationContext) {
        super(applicationContext);
    }

    @Override
    public ProcessorBean build(ProcessorDefinition processorDefinition) {

        try {
            boolean canLoad = checkCanLoad();
            if (!canLoad) {
                log.info("[ProcessorFactory] can't find Spring env, this processor can't load by 'BuiltInSpringProcessorFactory'");
                return null;
            }
            String processorInfo = processorDefinition.getProcessorInfo();
            //用于区分方法级别的参数
            if (processorInfo.contains("#")) {
                return null;
            }
            BasicProcessor basicProcessor = getBean(processorInfo, applicationContext);
            return new ProcessorBean()
                    .setProcessor(basicProcessor)
                    .setClassLoader(basicProcessor.getClass().getClassLoader());
        } catch (NoSuchBeanDefinitionException ignore) {
            log.warn("[ProcessorFactory] can't find the processor in SPRING");
        } catch (Throwable t) {
            log.warn("[ProcessorFactory] load by BuiltInSpringProcessorFactory failed. If you are using Spring, make sure this bean was managed by Spring", t);
        }

        return null;
    }


}
