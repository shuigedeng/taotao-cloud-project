package com.taotao.cloud.canal.client.abstracts.option;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.taotao.cloud.canal.client.interfaces.IDBOption;

/**
 * 数据库操作抽象类
 *
 * @author 阿导
 * @CopyRight 万物皆导
 * @created 2018年05月29日 14:42:00
 * @Modified_By 阿导 2018/5/29 14:42
 */
public abstract class AbstractDBOption implements IDBOption {
	
	/**
	 * 操作类型
	 */
	protected CanalEntry.EventType eventType;
	/**
	 * 下一个节点
	 */
	protected AbstractDBOption next;
	
	
	
	/**
	 * 默认构造方法
	 *
	 * @return
	 * @author 阿导
	 * @time 2018/5/29 09:06
	 * @CopyRight 万物皆导
	 */
	public AbstractDBOption() {
		this.setEventType();
	}
	
	/**
	 * 进行类型设置
	 *
	 * @author 阿导
	 * @CopyRight 萬物皆導
	 * @created 2018/5/29 09:21
	 */
	protected abstract void setEventType();
	
	
	/**
	 * 设置下一个节点
	 *
	 * @param next
	 * @return
	 * @author 阿导
	 * @time 2018/5/29 09:39
	 * @CopyRight 万物皆导
	 */
	public void setNext(AbstractDBOption next) {
		this.next = next;
	}
	
	
	
	/**
	 * 责任链处理
	 *
	 * @param destination
	 * @param schemaName
	 * @param tableName
	 * @param rowChange
	 * @return
	 * @author 阿导
	 * @time 2018/5/29 09:20
	 * @CopyRight 万物皆导
	 */
	public void doChain(String destination, String schemaName, String tableName, CanalEntry.RowChange rowChange) {
		if (this.eventType.equals(rowChange.getEventType())) {
			this.doOption(destination, schemaName, tableName, rowChange);
		} else {
			if(this.next==null){
				return;
			}
			this.next.doChain(destination, schemaName, tableName,rowChange);
		}
	}
	
	
}
