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
package com.taotao.cloud.disruptor.configuration;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.EventTranslatorThreeArg;
import com.lmax.disruptor.EventTranslatorTwoArg;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;
import com.taotao.cloud.common.constant.StarterNameConstant;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.disruptor.annotation.EventRule;
import com.taotao.cloud.disruptor.context.DisruptorEventAwareProcessor;
import com.taotao.cloud.disruptor.context.DisruptorTemplate;
import com.taotao.cloud.disruptor.context.EventHandlerDefinition;
import com.taotao.cloud.disruptor.context.Ini;
import com.taotao.cloud.disruptor.event.DisruptorApplicationEvent;
import com.taotao.cloud.disruptor.event.DisruptorBindEventFactory;
import com.taotao.cloud.disruptor.event.DisruptorEvent;
import com.taotao.cloud.disruptor.event.DisruptorEventThreadFactory;
import com.taotao.cloud.disruptor.handler.DefaultHandlerChainManager;
import com.taotao.cloud.disruptor.handler.DisruptorEventDispatcher;
import com.taotao.cloud.disruptor.handler.DisruptorHandler;
import com.taotao.cloud.disruptor.handler.HandlerChainManager;
import com.taotao.cloud.disruptor.handler.Nameable;
import com.taotao.cloud.disruptor.handler.PathMatchingHandlerChainResolver;
import com.taotao.cloud.disruptor.hooks.DisruptorShutdownHook;
import com.taotao.cloud.disruptor.properties.DisruptorProperties;
import com.taotao.cloud.disruptor.translator.DisruptorEventOneArgTranslator;
import com.taotao.cloud.disruptor.translator.DisruptorEventThreeArgTranslator;
import com.taotao.cloud.disruptor.translator.DisruptorEventTwoArgTranslator;
import com.taotao.cloud.disruptor.util.StringUtils;
import com.taotao.cloud.disruptor.util.WaitStrategys;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.OrderComparator;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

/**
 * DisruptorAutoConfiguration
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-03 19:58:30
 */
@Configuration
@EnableConfigurationProperties({DisruptorProperties.class})
@ConditionalOnClass({Disruptor.class})
@ConditionalOnProperty(prefix = DisruptorProperties.PREFIX, value = "enabled", havingValue = "true")
public class DisruptorAutoConfiguration implements ApplicationContextAware, InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(DisruptorAutoConfiguration.class, StarterNameConstant.DISRUPTOR_STARTER);
	}

	/**
	 * applicationContext
	 */
	private ApplicationContext applicationContext;
	/**
	 * 处理器链定义
	 */
	private Map<String, String> handlerChainDefinitionMap = new HashMap<>();

	/**
	 * 决定一个消费者将如何等待生产者将Event置入Disruptor的策略。 用来权衡当生产者无法将新的事件放进RingBuffer时的处理策略。
	 * （例如：当生产者太快，消费者太慢，会导致生成者获取不到新的事件槽来插入新事件， 则会根据该策略进行处理，默认会堵塞）
	 */
	@Bean
	@ConditionalOnMissingBean
	public WaitStrategy waitStrategy() {
		LogUtil.started(WaitStrategy.class, StarterNameConstant.DISRUPTOR_STARTER);
		return WaitStrategys.YIELDING_WAIT;
	}

	//@Bean("disruptorThreadFactory")
	//@ConditionalOnMissingBean(name = "disruptorThreadFactory")
	//public ThreadFactory threadFactory() {
	//	LogUtil.started(DisruptorEventThreadFactory.class, StarterNameConstant.DISRUPTOR_STARTER);
	//	return new DisruptorEventThreadFactory();
	//}

	@Bean
	@ConditionalOnMissingBean
	public EventFactory<DisruptorEvent> eventFactory() {
		LogUtil.started(DisruptorBindEventFactory.class, StarterNameConstant.DISRUPTOR_STARTER);
		return new DisruptorBindEventFactory();
	}

	/*
	 * Handler实现集合
	 */
	@Bean("disruptorHandlers")
	public Map<String, DisruptorHandler<DisruptorEvent>> disruptorHandlers() {
		LogUtil.started(DisruptorEvent.class, StarterNameConstant.DISRUPTOR_STARTER);

		Map<String, DisruptorHandler<DisruptorEvent>> disruptorPreHandlers = new LinkedHashMap<>();
		Map<String, DisruptorHandler> beansOfType = getApplicationContext().getBeansOfType(
			DisruptorHandler.class);

		if (!ObjectUtils.isEmpty(beansOfType)) {
			for (Entry<String, DisruptorHandler> entry : beansOfType.entrySet()) {
				if (entry.getValue() instanceof DisruptorEventDispatcher) {
					// 跳过入口实现类
					continue;
				}

				EventRule annotationType = getApplicationContext().findAnnotationOnBean(
					entry.getKey(), EventRule.class);
				if (annotationType == null) {
					// 注解为空，则打印错误信息
					LogUtil.error("Not Found AnnotationType {} on Bean {} Whith Name {}",
						EventRule.class, entry.getValue().getClass(), entry.getKey());
				} else {
					handlerChainDefinitionMap.put(annotationType.value(), entry.getKey());
				}

				disruptorPreHandlers.put(entry.getKey(), entry.getValue());
			}
		}
		// BeanFactoryUtils.beansOfTypeIncludingAncestors(getApplicationContext(),
		// EventHandler.class);

		return disruptorPreHandlers;
	}

	/*
	 * 处理器链集合
	 */
	@Bean("disruptorEventHandlers")
	public List<DisruptorEventDispatcher> disruptorEventHandlers(DisruptorProperties properties,
		@Qualifier("disruptorHandlers") Map<String, DisruptorHandler<DisruptorEvent>> eventHandlers) {
		LogUtil.started(DisruptorEventDispatcher.class, StarterNameConstant.DISRUPTOR_STARTER);

		// 获取定义 拦截链规则
		List<EventHandlerDefinition> handlerDefinitions = properties.getHandlerDefinitions();
		// 拦截器集合
		List<DisruptorEventDispatcher> disruptorEventHandlers = new ArrayList<>();

		// 未定义，则使用默认规则
		if (CollectionUtils.isEmpty(handlerDefinitions)) {
			EventHandlerDefinition definition = new EventHandlerDefinition();

			definition.setOrder(0);
			definition.setDefinitionMap(handlerChainDefinitionMap);

			// 构造DisruptorEventHandler
			disruptorEventHandlers.add(this.createDisruptorEventHandler(definition, eventHandlers));
		} else {
			// 迭代拦截器规则
			for (EventHandlerDefinition handlerDefinition : handlerDefinitions) {
				// 构造DisruptorEventHandler
				disruptorEventHandlers.add(
					this.createDisruptorEventHandler(handlerDefinition, eventHandlers));
			}
		}
		// 进行排序
		disruptorEventHandlers.sort(new OrderComparator());

		return disruptorEventHandlers;
	}

	/**
	 * 构造DisruptorEventHandler
	 *
	 * @param handlerDefinition handlerDefinition
	 * @param eventHandlers     eventHandlers
	 * @return {@link com.taotao.cloud.disruptor.handler.DisruptorEventDispatcher }
	 * @author shuigedeng
	 * @since 2021-09-03 19:13:19
	 */
	protected DisruptorEventDispatcher createDisruptorEventHandler(
		EventHandlerDefinition handlerDefinition,
		Map<String, DisruptorHandler<DisruptorEvent>> eventHandlers) {

		if (StringUtils.isNotEmpty(handlerDefinition.getDefinitions())) {
			handlerChainDefinitionMap.putAll(
				this.parseHandlerChainDefinitions(handlerDefinition.getDefinitions()));
		} else if (!CollectionUtils.isEmpty(handlerDefinition.getDefinitionMap())) {
			handlerChainDefinitionMap.putAll(handlerDefinition.getDefinitionMap());
		}

		HandlerChainManager<DisruptorEvent> manager = createHandlerChainManager(eventHandlers,
			handlerChainDefinitionMap);
		PathMatchingHandlerChainResolver chainResolver = new PathMatchingHandlerChainResolver();
		chainResolver.setHandlerChainManager(manager);
		return new DisruptorEventDispatcher(chainResolver, handlerDefinition.getOrder());
	}

	/**
	 * parseHandlerChainDefinitions
	 *
	 * @param definitions definitions
	 * @return {@link java.util.Map }
	 * @author shuigedeng
	 * @since 2021-09-03 19:13:24
	 */
	protected Map<String, String> parseHandlerChainDefinitions(String definitions) {
		Ini ini = new Ini();
		ini.load(definitions);
		Ini.Section section = ini.getSection("urls");
		if (CollectionUtils.isEmpty(section)) {
			section = ini.getSection(Ini.DEFAULT_SECTION_NAME);
		}
		return section;
	}

	/**
	 * createHandlerChainManager
	 *
	 * @param eventHandlers             eventHandlers
	 * @param handlerChainDefinitionMap handlerChainDefinitionMap
	 * @return {@link com.taotao.cloud.disruptor.handler.HandlerChainManager }
	 * @author shuigedeng
	 * @since 2021-09-03 19:13:27
	 */
	protected HandlerChainManager<DisruptorEvent> createHandlerChainManager(
		Map<String, DisruptorHandler<DisruptorEvent>> eventHandlers,
		Map<String, String> handlerChainDefinitionMap) {

		HandlerChainManager<DisruptorEvent> manager = new DefaultHandlerChainManager();
		if (!CollectionUtils.isEmpty(eventHandlers)) {
			for (Entry<String, DisruptorHandler<DisruptorEvent>> entry : eventHandlers.entrySet()) {
				String name = entry.getKey();
				DisruptorHandler<DisruptorEvent> handler = entry.getValue();
				if (handler instanceof Nameable) {
					((Nameable) handler).setName(name);
				}
				manager.addHandler(name, handler);
			}
		}

		if (!CollectionUtils.isEmpty(handlerChainDefinitionMap)) {
			for (Entry<String, String> entry : handlerChainDefinitionMap.entrySet()) {
				// ant匹配规则
				String rule = entry.getKey();
				String chainDefinition = entry.getValue();
				manager.createChain(rule, chainDefinition);
			}
		}

		return manager;
	}

	/**
	 * <p>
	 * 创建Disruptor
	 * </p>
	 * <p>
	 * 1 eventFactory 为
	 * <p>
	 * 2 ringBufferSize为RingBuffer缓冲区大小，最好是2的指数倍
	 * </p>
	 *
	 * @param properties             : 配置参数
	 * @param waitStrategy           : 一种策略，用来均衡数据生产者和消费者之间的处理效率，默认提供了3个实现类
	 * @param threadFactory          : 线程工厂
	 * @param eventFactory           : 工厂类对象，用于创建一个个的LongEvent， LongEvent是实际的消费数据，初始化启动Disruptor的时候，Disruptor会调用该工厂方法创建一个个的消费数据实例存放到RingBuffer缓冲区里面去，创建的对象个数为ringBufferSize指定的
	 * @param disruptorEventHandlers : 事件分发器
	 * @return {@link Disruptor} instance
	 */
	@Bean
	@ConditionalOnClass({Disruptor.class})
	@ConditionalOnProperty(prefix = DisruptorProperties.PREFIX, value = "enabled", havingValue = "true")
	public Disruptor<DisruptorEvent> disruptor(
		DisruptorProperties properties,
		WaitStrategy waitStrategy,
		EventFactory<DisruptorEvent> eventFactory,
		@Qualifier("disruptorEventHandlers") List<DisruptorEventDispatcher> disruptorEventHandlers) {

		// http://blog.csdn.net/a314368439/article/details/72642653?utm_source=itdadao&utm_medium=referral
		LogUtil.started(Disruptor.class, StarterNameConstant.DISRUPTOR_STARTER);

		DisruptorEventThreadFactory threadFactory = new DisruptorEventThreadFactory();

		Disruptor<DisruptorEvent> disruptor;
		if (properties.getMultiProducer()) {
			disruptor = new Disruptor<>(eventFactory, properties.getRingBufferSize(), threadFactory,
				ProducerType.MULTI, waitStrategy);
		} else {
			disruptor = new Disruptor<>(eventFactory, properties.getRingBufferSize(), threadFactory,
				ProducerType.SINGLE, waitStrategy);
		}

		if (!ObjectUtils.isEmpty(disruptorEventHandlers)) {
			// 进行排序
			disruptorEventHandlers.sort(new OrderComparator());

			// 使用disruptor创建消费者组
			EventHandlerGroup<DisruptorEvent> handlerGroup = null;
			for (int i = 0; i < disruptorEventHandlers.size(); i++) {
				// 连接消费事件方法，其中EventHandler的是为消费者消费消息的实现类
				DisruptorEventDispatcher eventHandler = disruptorEventHandlers.get(i);
				if (i < 1) {
					handlerGroup = disruptor.handleEventsWith(eventHandler);
				} else {
					// 完成前置事件处理之后执行后置事件处理
					handlerGroup.then(eventHandler);
				}
			}
		}

		// 启动
		disruptor.start();

		// 应用退出时，要调用shutdown来清理资源，关闭网络连接，从MetaQ服务器上注销自己
		// 建议应用在JBOSS、Tomcat等容器的退出钩子里调用shutdown方法
		Runtime.getRuntime().addShutdownHook(new DisruptorShutdownHook(disruptor));
		return disruptor;
	}

	@Bean
	@ConditionalOnMissingBean
	public EventTranslatorOneArg<DisruptorEvent, DisruptorEvent> oneArgEventTranslator() {
		LogUtil.started(EventTranslatorOneArg.class, StarterNameConstant.DISRUPTOR_STARTER);
		return new DisruptorEventOneArgTranslator();
	}

	@Bean
	@ConditionalOnMissingBean
	public EventTranslatorTwoArg<DisruptorEvent, String, String> twoArgEventTranslator() {
		LogUtil.started(EventTranslatorTwoArg.class, StarterNameConstant.DISRUPTOR_STARTER);
		return new DisruptorEventTwoArgTranslator();
	}

	@Bean
	@ConditionalOnMissingBean
	public EventTranslatorThreeArg<DisruptorEvent, String, String, String> threeArgEventTranslator() {
		LogUtil.started(EventTranslatorThreeArg.class, StarterNameConstant.DISRUPTOR_STARTER);
		return new DisruptorEventThreeArgTranslator();
	}

	@Bean
	public DisruptorTemplate disruptorTemplate(Disruptor<DisruptorEvent> disruptor,
		EventTranslatorOneArg<DisruptorEvent, DisruptorEvent> oneArgEventTranslator) {
		LogUtil.started(DisruptorTemplate.class, StarterNameConstant.DISRUPTOR_STARTER);
		return new DisruptorTemplate(disruptor, oneArgEventTranslator);
	}

	@Bean
	public ApplicationListener<DisruptorApplicationEvent> disruptorEventListener(
		Disruptor<DisruptorEvent> disruptor,
		EventTranslatorOneArg<DisruptorEvent, DisruptorEvent> oneArgEventTranslator) {
		LogUtil.started(DisruptorApplicationEvent.class, StarterNameConstant.DISRUPTOR_STARTER);

		return appEvent -> {
			DisruptorEvent event = (DisruptorEvent) appEvent.getSource();
			disruptor.publishEvent(oneArgEventTranslator, event);
		};
	}

	@Bean
	public DisruptorEventAwareProcessor disruptorEventAwareProcessor() {
		LogUtil.started(DisruptorEventAwareProcessor.class, StarterNameConstant.DISRUPTOR_STARTER);
		return new DisruptorEventAwareProcessor();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}
