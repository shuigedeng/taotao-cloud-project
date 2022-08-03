package com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta;

import com.taotao.cloud.sys.biz.api.controller.tools.database.service.connect.ConnDatasourceAdapter;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.impl.DefaultDatabaseMetaDataload;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 元数据存储适配
 */
@Component
@Slf4j
public class MetaDataLoadManager {
	@Autowired
	private Map<String, DatabaseMetaDataLoad> metaDataLoadMap = new HashMap<>();
	@Autowired
	private ConnDatasourceAdapter connDatasourceAdapter;
	@Autowired
	private DefaultDatabaseMetaDataload defaultDatabaseMetaDataload;

	/**
	 * 是否取缓存中的元数据
	 */
	public static final ThreadLocal<Boolean> metaCache = new ThreadLocal<Boolean>() {
		@Override
		protected Boolean initialValue() {
			return true;
		}
	};

	/**
	 * 是否本地缓存元数据信息
	 */
	private boolean cache = false;

	/**
	 * 通过连接获取数据源加载器
	 * @param connName
	 * @return
	 * @throws IOException
	 */
	public DatabaseMetaDataLoad databaseMetaDataLoad(String connName) throws IOException {
		final String dbType = connDatasourceAdapter.dbType(connName);
		DatabaseMetaDataLoad databaseMetaDataLoad = metaDataLoadMap
				.get(dbType + DatabaseMetaDataLoad.class.getSimpleName());
		if (databaseMetaDataLoad == null) {
			log.warn("未找到当前数据库类型[{}]的元数据实现, 将使用默认 jdbc 元数据实现");
			databaseMetaDataLoad = defaultDatabaseMetaDataload;
		}

		if (!cache) {
			return databaseMetaDataLoad;
		}

		final ClassLoader classLoader = MetaDataLoadManager.class.getClassLoader();
		return (DatabaseMetaDataLoad) Proxy.newProxyInstance(classLoader, new Class[] { DatabaseMetaDataLoad.class },
				new MetaCacheInvoicationHandler(databaseMetaDataLoad));
	}

	/**
	 * 14 天后第一次执行, 然后每一天执行一次清除
	 */
	@Scheduled(fixedRate = 86400000,initialDelay = 1209600000)
	public void cleanCacheTask(){
		final Iterator<Map.Entry<String, MetaCacheInvoicationHandler.CacheObject>> iterator = MetaCacheInvoicationHandler.CACHE.entrySet().iterator();
		while (iterator.hasNext()){
			final Map.Entry<String, MetaCacheInvoicationHandler.CacheObject> cacheObjectEntry = iterator.next();
			final MetaCacheInvoicationHandler.CacheObject cacheObject = cacheObjectEntry.getValue();
			if (System.currentTimeMillis() - cacheObject.getCacheTime() > MetaCacheInvoicationHandler.CACHE_TIME){
				log.info("清除过期缓存数据: {}",cacheObjectEntry.getKey());
				iterator.remove();
			}
		}
	}

	/**
	 * 元数据缓存处理
	 */
	public static class MetaCacheInvoicationHandler implements InvocationHandler {
		/**
		 * 元数据加载器
		 */
		private DatabaseMetaDataLoad databaseMetaDataLoad;

		/**
		 * 数据缓存时间 15 天
		 */
		private static final long CACHE_TIME = 15 * 24 * 60 * 60 * 1000;

		/**
		 * method signature => CacheObject
		 */
		private static final Map<String,CacheObject> CACHE = new ConcurrentHashMap<>();

		public MetaCacheInvoicationHandler(DatabaseMetaDataLoad databaseMetaDataLoad) {
			this.databaseMetaDataLoad = databaseMetaDataLoad;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			if (Object.class.equals(method.getDeclaringClass())) {
				return method.invoke(this, args);
			}

			final Boolean loadCache = metaCache.get();

			final String methodSignature = methodSignature(method,args);
			if (CACHE.containsKey(methodSignature) && loadCache){
				log.info("缓存命中: {}",methodSignature);
				final CacheObject cacheObject = CACHE.get(methodSignature);
				// 检查数据是否过期
				final long cacheTime = cacheObject.getCacheTime();
				if (System.currentTimeMillis() - cacheTime <= CACHE_TIME) {
					return cacheObject.getData();
				}
				log.info("缓存数据[{}]过期, 将重新获取",methodSignature);
			}

			// 调用数据, 然后缓存
			final Object invoke = method.invoke(databaseMetaDataLoad, args);
			CACHE.put(methodSignature,new CacheObject(invoke,System.currentTimeMillis()));

			return invoke;
		}

		/**
		 * 获取方法信息
		 * @param method
		 * @return
		 */
		private String methodSignature(Method method,Object[] args) {
			final Parameter[] parameters = method.getParameters();
			StringBuffer key = new StringBuffer();
			key.append(method.getDeclaringClass().getName()).append(method.getName()).append("(");
			for (int i = 0; i < parameters.length; i++) {
				final Parameter parameter = parameters[i];
				final String arg = Objects.toString(args[i]);
				key.append(parameter.getType().getSimpleName()).append(arg).append(" ");
			}
			key.append(")");
			return key.toString();
		}

		/**
		 * 缓存中存放的数据对象
		 */
		@Data
		public static final class CacheObject{
			private Object data;
			private long cacheTime;

			public CacheObject(Object data, long cacheTime) {
				this.data = data;
				this.cacheTime = cacheTime;
			}
		}
	}
}
