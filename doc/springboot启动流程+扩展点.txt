springboot前阶段：

	1.创建DefaultBootstrapContext(创建DefaultBootstrapContext时) 回调BootstrapRegistryInitializer -- initialize
	2.回调SpringApplicationRunListener -- starting
			1.发布ApplicationStartingEvent事件
	3.创建ConfigurableEnvironment  回调SpringApplicationRunListener -- environmentPrepared
			1.发布ApplicationEnvironmentPreparedEvent事件
			2.在事件里回调EnvironmentPostProcessor(Environment准备完成后) -- postProcessEnvironment
	4.打印banner
	5.创建ConfigurableEnvironment
	6.创建ConfigurableApplicationContext
	7.回调ApplicationContextInitializer(ApplicationContext创建后，refresh前) -- initialize
	    注: 容器刷新之前调用此类的initialize方法 用户可以在整个spring容器还没被初始化之前做一些事情。 可以想到的场景可能为，在最开始激活一些配置，
	        或者利用这时候class还没被类加载器加载的时机，进行动态字节码注入等操作
	        在启动类中用springApplication.addInitializers(new TestApplicationContextInitializer())语句加入
	        配置文件配置context.initializer.classes=com.example.demo.TestApplicationContextInitializerSpring SPI扩展，
	        在spring.factories中加入org.springframework.context.ApplicationContextInitializer=com.example.demo.TestApplicationContextInitializer
	8.回调SpringApplicationRunListener -- contextPrepared
	9.DefaultBootstrapContext生命周期完成
	10.回调SpringApplicationRunListener -- contextLoaded
	11.调用refreshContext方法进入到spring的refresh方法

		12.spring阶段 AbstractApplicationContext -- refresh：
			1.prepareRefresh 初始化initPropertySources
			2.obtainFreshBeanFactory 刷新BeanFactory 加载loadBeanDefinitions到BeanFactory
			3.prepareBeanFactory 添加内部的ClassLoader 或者 post-processors

		阶段1-2：准备和加载 BeanDefinition
			4.postProcessBeanFactory
					//4.1 this.scanner.scan(this.basePackages); 扫描类路径注册BeanDefinitions
					//4.2 this.reader.register(this.annotatedClasses); 根据注解注册BeanDefinitions
			5.invokeBeanFactoryPostProcessors
					5.1 回调BeanDefinitionRegistryPostProcessor -- postProcessBeanDefinitionRegistry (BeanDefinition注册后实例化前执行)
					      注：可以动态注册自己的beanDefinition，可以加载classpath之外的bean
						  1.重点回调ConfigurationClassPostProcessor
								处理@Component @PropertySources PropertySourceFactory @Configuration
								处理@ComponentScan @ComponentScans @ComponentScan(nameGenerator = ..., scopeResolver = ...) 使用了ClassPathBeanDefinitionScanner
								处理@Import @ImportResource @Configuration @Bean
								处理Condition ConditionEvaluator  BeanNameGenerator ScopeMetadataResolver
								回调ImportSelector -- selectImports
								** 涉及到重要类 ImportAutoConfigurationImportSelector AutoConfigurationImportSelector
                  ** 回调DeferredImportSelector ImportSelector 的延迟版本，在所有 @Configuration 类解析完成后才执行，用于解决依赖顺序问题
								** 涉及到BeanClassLoaerAware BeaFatoryAware EnvironmentAware ResourceLoaderAware这4个回调
								** 把这些BeanDefinition注册到BeanFactory
								回调ImportBeanDefinitionRegistrar -- registerBeanDefinitions (配合@Import注解 在解析配置类时动态注册 BeanDefinition)
					5.2 回调BeanFactoryPostProcessor -- postProcessBeanFactory (BeanDefinition注册后实例化前执行)
					      注:调用时机在spring在读取beanDefinition信息之后，实例化bean之前。
					        在这个时机，用户可以通过实现这个扩展接口来自行处理一些东西，
					        比如修改已经注册的beanDefinition的元信息。

			6.registerBeanPostProcessors到BeanFactory
					6.1 注册所有的 BeanPostProcessor (仅注册，不回调) 在getBean(BeanPostProcessor.class)会走getBean的生命周期
							6.1.1 内部的MergedBeanDefinitionPostProcessor
							6.1.2 排序的BeanPostProcessor
			7.initMessageSource 初始化国际化
			8.initApplicationEventMulticaster 初始化事件分发器
			9.onRefresh
				9.1子类创建createWebServer 会提前创建filter servlet等bean 会走getBean的生命周期
						扩展点ServletContextInitializer
			10.registerListeners
				10.添加ApplicationListener到事件分发器中 回调早期的事件

		阶段3：实例化Bean
			11.finishBeanFactoryInitialization
				1.主要方法为getBean
					1.实例化Bean Instantiation
						1.回调FactoryBean -- getObject
						    注：一般情况下，Spring通过反射机制利用bean的class属性指定支线类去实例化bean，在某些情况下，实例化Bean过程比较复杂，
						       如果按照传统的方式，则需要在bean中提供大量的配置信息。配置方式的灵活性是受限的，这时采用编码的方式可能会得到一个简单的方案。
						       Spring为此提供了一个org.springframework.bean.factory.FactoryBean的工厂类接口，用户可以通过实现该接口定制实例化Bean的逻辑。
						       FactoryBean接口对于Spring框架来说占用重要的地位，Spring自身就提供了70多个FactoryBean的实现。它们隐藏了实例化一些复杂bean的细节，
						       给上层应用带来了便利。从Spring3.0开始，FactoryBean开始支持泛型，即接口声明改为FactoryBean<T>的形式使用场景：用户可以扩展这个类，
						       来为要实例化的bean作一个代理，比如为该对象的所有的方法作一个拦截，在调用前后输出一行log，模仿ProxyFactoryBean的功能。
						2.回调SmartFactoryBean -- isEagerInit getObject
						3.getBean大头+重点
						  InstantiationAwareBeanPostProcessor:
						    注:BeanPostProcess接口只在bean的初始化阶段进行扩展（注入spring上下文前后），
						      而InstantiationAwareBeanPostProcessor接口在此基础上增加了3个方法，
						      把可扩展的范围增加了实例化阶段和属性注入阶段。
						      这个扩展点非常有用 ，无论是写中间件和业务中，都能利用这个特性。比如对实现了某一类接口的bean在各个生命期间进行收集，或者对某个类型的bean进行统一的设值等等。
							回调InstantiationAwareBeanPostProcessor -- postProcessBeforeInstantiation (Bean实例化前)
							  注：实例化bean之前，相当于new这个bean之前
							回调SmartInstantiationAwareBeanPostProcessor -- predictBeanType 处理循环依赖
							  注：该触发点发生在postProcessBeforeInstantiation之前(在图上并没有标明，因为一般不太需要扩展这个点)，这个方法用于预测Bean的类型，
							     返回第一个预测成功的Class类型，如果不能预测返回null；
							     当你调用BeanFactory.getType(name)时当通过bean的名字无法得到bean类型信息时就调用该回调方法来决定类型信息。
							回调SmartInstantiationAwareBeanPostProcessor -- determineCandidateConstructors 选择构造函数
							  注：该触发点发生在postProcessBeforeInstantiation之后，用于确定该bean的构造函数之用，
							     返回的是该bean的所有构造函数列表。用户可以扩展这个点，来自定义选择相应的构造器来实例化这个bean。
							执行反射创建对象
							回调MergedBeanDefinitionPostProcessor -- postProcessMergedBeanDefinition
							回调SmartInstantiationAwareBeanPostProcessor -- getEarlyBeanReference
							  注：该触发点发生在postProcessAfterInstantiation之后，当有循环依赖的场景，当bean实例化好之后，为了防止有循环依赖，会提前暴露回调方法，
							     用于bean实例化的后置处理。这个方法就是在提前暴露的回调方法中触发。
							执行populateBean
							回调InstantiationAwareBeanPostProcessor -- postProcessAfterInstantiation	(Bean实例化后)
							  注：实例化bean之后，相当于new这个bean之后
							执行属性装配前
							回调InstantiationAwareBeanPostProcessor -- postProcessProperties
							回调InstantiationAwareBeanPostProcessor -- postProcessPropertyValues
							  注：bean已经实例化完成，在属性注入时阶段触发，@Autowired,@Resource等注解原理基于此方法实现
							执行属性装配完成

					2.初始化Bean Initializing
							回调各种Aware BeanNameAware 触发点在bean的初始化之前，也就是postProcessBeforeInitialization之前
							                          在初始化bean之前拿到spring容器中注册的的beanName，来自行修改这个beanName的值。
							          BeanClassLoaerAware
							          BeanFatoryAware
												ApplicationContextAware
												EnvironmentAware 用于获取EnvironmentAware的一个扩展类，这个变量非常有用， 可以获得系统内的所有参数
												EmbeddedValueResolverAware 用于获取StringValueResolver的一个扩展类，StringValueResolver用于获取基于String类型的properties的变量，
												                          一般我们都用@Value的方式去获取，如果实现了这个Aware接口，把StringValueResolver缓存起来，
												                          通过这个类去获取String类型的变量，效果是一样的。
												ResourceLoaderAware  用于获取ResourceLoader的一个扩展类，ResourceLoader可以用于获取classpath内所有的资源对象，可以扩展此类来拿到ResourceLoader对象。
												MessageSourceAware   用于获取MessageSource的一个扩展类，MessageSource主要用来做国际化。
												ApplicationEventPublisherAware 用于获取ApplicationEventPublisher的一个扩展类，ApplicationEventPublisher可以用来发布事件，
												                              结合ApplicationListener来共同使用，下文在介绍ApplicationListener时会详细提到。这个对象也可以通过spring注入的方式来获得。
							回调BeanPostProcessor -- postProcessBeforeInitialization （Bean初始化前调用）
							  注：初始化bean之前，相当于把bean注入spring上下文之前
							执行初始化方法
							回调InitializingBean -- afterPropertiesSet
							  注：初始化bean的。InitializingBean接口为bean提供了初始化方法的方式，它只包括afterPropertiesSet方法，凡是继承该接口的类，在初始化bean的时候都会执行该方法。
							     这个扩展点的触发时机在postProcessAfterInitialization之前。使用场景：用户实现此接口，来进行系统启动的时候一些业务指标的初始化工作。
							回调Bean上面的init方法
							回调BeanPostProcessor -- postProcessAfterInitialization  （Bean初始化后调用）
							  注：初始化bean之后，相当于把bean注入spring上下文之后

					3.注册DisposableBean

			  2.回调SmartInitializingSingleton -- afterSingletonsInstantiated 在所有单例Bean初始化完成后执行
			      注：这个接口中只有一个方法afterSingletonsInstantiated，其作用是是 在spring容器管理的所有单例对象（非懒加载对象）初始化完成之后调用的回调接口。
			         其触发时机为postProcessAfterInitialization之后。
			         使用场景：用户可以扩展此接口在对所有单例对象初始化完毕后，做一些后置的业务处理。

		finishRefresh： 回调SmartLifecycle 的start方法

		阶段4：应用运行

springboot后阶段：
	12.回调SpringApplicationRunListener -- started
	13.回调ApplicationRunner -- run
	13.回调CommandLineRunner -- run

在整个springboot启动过程中启动失败处理:
1.FailureAnalyzer  启动失败时提供友好的错误分析报告
1.SpringBootExceptionReporter   报告启动过程中的异常

应用Destory阶段
1.@PreDestroy
2.DisposableBean
3.DestructionAwareBeanPostProcessor.postProcessBeforeDestruction




只执行1次（容器级别）：

ApplicationContextInitializer
BeanDefinitionRegistryPostProcessor
BeanFactoryPostProcessor
SmartInitializingSingleton
ApplicationListener
ApplicationRunner

每个Bean都执行（Bean级别）：

InstantiationAwareBeanPostProcessor
BeanPostProcessor（Before和After）

比如你有10个Bean，BeanPostProcessor的postProcessBeforeInitialization会执行10次。


