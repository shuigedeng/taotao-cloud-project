//package com.taotao.cloud.demo.apollo;
//
//import com.ctrip.framework.apollo.Config;
//import com.ctrip.framework.apollo.ConfigService;
//import com.ctrip.framework.apollo.model.ConfigChange;
//import com.ctrip.framework.apollo.model.ConfigChangeEvent;
//import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
//import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
//import com.ctrip.framework.apollo.spring.annotation.ApolloJsonValue;
//import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
//import com.taotao.cloud.common.utils.common.PropertyUtil;
//import com.taotao.cloud.common.utils.log.LogUtil;
//import java.util.List;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.util.StringUtils;
//
//@Configuration
//@EnableApolloConfig //{"taotao-cloud","application"}
//public class ApolloConfiguration implements InitializingBean {
//
//	@Override
//	public void afterPropertiesSet() {
//		LogUtil.started(ApolloConfiguration.class, "已启动!!!" + " ");
//
//		for (String namespace : PropertyUtil.getProperty("apollo.bootstrap.namespaces",
//			"").split(",")) {
//			if (!StringUtils.isEmpty(namespace)) {
//				Config config = ConfigService.getConfig(namespace);
//				config.addChangeListener(changeEvent -> {
//					for (String key : changeEvent.changedKeys()) {
//						ConfigChange change = changeEvent.getChange(key);
//
//						LogUtil.info(
//							"监听到apollo配置修改,key: {}, oldValue: {}, newValue: {}, changeType: {}, 当前配置值: {}",
//							change.getPropertyName(),
//							change.getOldValue(),
//							change.getNewValue(),
//							change.getChangeType(),
//							PropertyUtil.getProperty(key));
//
//					}
//				});
//			}
//		}
//	}
//
//	@Bean
//	public TestApolloAnnotationBean testApolloAnnotationBean() {
//		return new TestApolloAnnotationBean();
//	}
//
//	public static class TestApolloAnnotationBean {
//
//		@ApolloConfig
//		private Config config; //inject config for namespace application
//		@ApolloConfig("application")
//		private Config anotherConfig; //inject config for namespace application
//		@ApolloConfig("FX.apollo")
//		private Config yetAnotherConfig; //inject config for namespace FX.apollo
//		@ApolloConfig("application.yml")
//		private Config ymlConfig; //inject config for namespace application.yml
//
//		/**
//		 * ApolloJsonValue annotated on fields example, the default value is specified as empty list
//		 * - [] <br /> jsonBeanProperty=[{"someString":"hello","someInt":100},{"someString":"world!","someInt":200}]
//		 */
//		@ApolloJsonValue("${jsonBeanProperty:[]}")
//		private List<JsonBean> anotherJsonBeans;
//
//		@Value("${batch:100}")
//		private int batch;
//
//		//config change listener for namespace application
//		@ApolloConfigChangeListener
//		private void someOnChange(ConfigChangeEvent changeEvent) {
//			//update injected value of batch if it is changed in Apollo
//			if (changeEvent.isChanged("batch")) {
//				batch = config.getIntProperty("batch", 100);
//			}
//		}
//
//		//config change listener for namespace application
//		@ApolloConfigChangeListener("application")
//		private void anotherOnChange(ConfigChangeEvent changeEvent) {
//			//do something
//		}
//
//		//config change listener for namespaces application, FX.apollo and application.yml
//		@ApolloConfigChangeListener({"application", "FX.apollo", "application.yml"})
//		private void yetAnotherOnChange(ConfigChangeEvent changeEvent) {
//			//do something
//		}
//
//		//example of getting config from Apollo directly
//		//this will always return the latest value of timeout
//		public int getTimeout() {
//			return config.getIntProperty("timeout", 200);
//		}
//
//		//example of getting config from injected value
//		//the program needs to update the injected value when batch is changed in Apollo using @ApolloConfigChangeListener shown above
//		public int getBatch() {
//			return this.batch;
//		}
//
//		private static class JsonBean {
//
//			private String someString;
//			private int someInt;
//		}
//	}
//
//}
