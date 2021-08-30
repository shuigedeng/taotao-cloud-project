package com.taotao.cloud.canal.abstracts;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.taotao.cloud.canal.annotation.ListenPoint;
import com.taotao.cloud.canal.core.CanalMsg;
import com.taotao.cloud.canal.core.ListenerPoint;
import com.taotao.cloud.canal.exception.CanalClientException;
import com.taotao.cloud.canal.interfaces.CanalEventListener;
import com.taotao.cloud.canal.properties.CanalProperties;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

/**
 * 消息处理转换类
 *
 * @author 阿导
 * @CopyRight 萬物皆導
 * @created 2018/5/28 16:27
 * @Modified_By 阿导 2018/5/28 16:27
 */
public abstract class AbstractBasicMessageTransponder extends AbstractMessageTransponder {

	/**
	 * 日志记录
	 */
	private final static Logger logger = LoggerFactory.getLogger(
		AbstractBasicMessageTransponder.class);

	/**
	 * @param connector     canal 连接器
	 * @param config        canal 连接配置
	 * @param listeners     实现接口层的 canal 监听器
	 * @param annoListeners 通过注解方式的 canal 监听器
	 * @return
	 * @author 阿导
	 * @time 2018/5/28 16:27
	 * @CopyRight 万物皆导
	 */
	public AbstractBasicMessageTransponder(CanalConnector connector,
		Map.Entry<String, CanalProperties.Instance> config, List<CanalEventListener> listeners,
		List<ListenerPoint> annoListeners) {
		super(connector, config, listeners, annoListeners);
	}

	/**
	 * 处理消息
	 *
	 * @param message
	 * @return
	 * @author 阿导
	 * @time 2018/5/28 16:50
	 * @CopyRight 万物皆导
	 */
	@Override
	protected void distributeEvent(Message message) {
		//获取操作实体
		List<CanalEntry.Entry> entries = message.getEntries();
		//遍历实体
		for (CanalEntry.Entry entry : entries) {
			//忽略实体类的类型
			List<CanalEntry.EntryType> ignoreEntryTypes = getIgnoreEntryTypes();
			if (ignoreEntryTypes != null
				&& ignoreEntryTypes.stream().anyMatch(t -> entry.getEntryType() == t)) {
				continue;
			}
			//canal 改变信息
			CanalEntry.RowChange rowChange;
			try {
				//获取信息改变
				rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());

			} catch (Exception e) {
				throw new CanalClientException("错误 ##转换错误 , 数据信息:" + entry.toString(),
					e);
			}

			distributeByAnnotation(destination,
				entry.getHeader().getSchemaName(),
				entry.getHeader().getTableName(), rowChange);
			distributeByImpl(destination,
				entry.getHeader().getSchemaName(),
				entry.getHeader().getTableName(), rowChange);

		}
	}

	/**
	 * 处理注解方式的 canal 监听器
	 *
	 * @param destination canal 指令
	 * @param schemaName  实例名称
	 * @param tableName   表名称
	 * @param rowChange   数据
	 * @return
	 * @author 阿导
	 * @time 2018/5/28 16:35
	 * @CopyRight 万物皆导
	 */
	protected void distributeByAnnotation(String destination,
		String schemaName,
		String tableName,
		CanalEntry.RowChange rowChange) {

		//对注解的监听器进行事件委托
		if (!CollectionUtils.isEmpty(annoListeners)) {
			annoListeners.forEach(point -> point
				.getInvokeMap()
				.entrySet()
				.stream()
				.filter(getAnnotationFilter(destination, schemaName, tableName,
					rowChange.getEventType()))
				.forEach(entry -> {
					Method method = entry.getKey();
					method.setAccessible(true);
					try {
						CanalMsg canalMsg = new CanalMsg();
						canalMsg.setDestination(destination);
						canalMsg.setSchemaName(schemaName);
						canalMsg.setTableName(tableName);

						Object[] args = getInvokeArgs(method, canalMsg, rowChange);
						method.invoke(point.getTarget(), args);
					} catch (Exception e) {
						logger.error("{}: 委托 canal 监听器发生错误! 错误类:{}, 方法名:{}",
							Thread.currentThread().getName(),
							point.getTarget().getClass().getName(), method.getName());
					}
				}));
		}
	}


	/**
	 * 处理监听信息
	 *
	 * @param destination 指令
	 * @param schemaName  库实例
	 * @param tableName   表名
	 * @param rowChange   參數
	 * @return
	 * @author 阿导
	 * @time 2018/5/28 16:46
	 * @CopyRight 万物皆导
	 */
	protected void distributeByImpl(String destination,
		String schemaName,
		String tableName,
		CanalEntry.RowChange rowChange) {
		if (listeners != null) {
			for (CanalEventListener listener : listeners) {
				listener.onEvent(destination, schemaName, tableName, rowChange);
			}
		}
	}


	/**
	 * 断言注解方式的监听过滤规则
	 *
	 * @param destination 指定
	 * @param schemaName  数据库实例
	 * @param tableName   表名称
	 * @param eventType   事件类型
	 * @return
	 * @author 阿导
	 * @time 2018/5/28 16:00
	 * @CopyRight 万物皆导
	 */
	protected abstract Predicate<Map.Entry<Method, ListenPoint>> getAnnotationFilter(
		String destination, String schemaName, String tableName, CanalEntry.EventType eventType);


	/**
	 * 获取参数
	 *
	 * @param method    委托处理的方法
	 * @param canalMsg  其他信息
	 * @param rowChange 处理的数据
	 * @return
	 * @author 阿导
	 * @time 2018/5/28 16:30
	 * @CopyRight 万物皆导
	 */
	protected abstract Object[] getInvokeArgs(Method method, CanalMsg canalMsg,
		CanalEntry.RowChange rowChange);


	/**
	 * 返回一个空集合
	 *
	 * @param
	 * @return
	 * @author 阿导
	 * @time 2018/5/28 16:29
	 * @CopyRight 万物皆导
	 */
	protected List<CanalEntry.EntryType> getIgnoreEntryTypes() {
		return Collections.emptyList();
	}

}
