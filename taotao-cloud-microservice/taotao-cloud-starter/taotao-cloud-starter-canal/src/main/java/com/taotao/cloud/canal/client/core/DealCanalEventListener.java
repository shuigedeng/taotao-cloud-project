package com.taotao.cloud.canal.client.core;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.wwjd.starter.canal.client.abstracts.option.AbstractDBOption;
import com.wwjd.starter.canal.client.interfaces.CanalEventListener;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 处理 Canal 监听器
 *
 * @author 阿导
 * @CopyRight 万物皆导
 * @created 2018年05月28日 20:13:00
 * @Modified_By 阿导 2018/5/28 20:13
 */
@SuppressWarnings("all")
public class DealCanalEventListener implements CanalEventListener {
	
	/**
	 * 頭結點
	 */
	private AbstractDBOption header;
	
	/**
	 * 默認構造方法，必須傳入鏈路
	 *
	 * @param dbOptions
	 * @return
	 * @author 阿导
	 * @time 2018/5/30 17:46
	 * @CopyRight 杭州弧途科技有限公司（青团社）
	 */
	public DealCanalEventListener(AbstractDBOption... dbOptions) {
		AbstractDBOption tmp = null;
		for (AbstractDBOption dbOption : dbOptions) {
			if (tmp != null) {
				tmp.setNext(dbOption);
			} else {
				this.header = dbOption;
			}
			tmp = dbOption;
		}
		
	}
	
	public DealCanalEventListener(List<AbstractDBOption> dbOptions) {
		if (CollectionUtils.isEmpty(dbOptions)) {
			return;
		}
		AbstractDBOption tmp = null;
		for (AbstractDBOption dbOption : dbOptions) {
			if (tmp != null) {
				tmp.setNext(dbOption);
			} else {
				this.header = dbOption;
			}
			tmp = dbOption;
		}
	}
	
	/**
	 * 处理数据库的操作
	 *
	 * @param destination
	 * @param schemaName
	 * @param tableName
	 * @param rowChange
	 * @return
	 * @author 阿导
	 * @time 2018/5/29 09:43
	 * @CopyRight 万物皆导
	 */
	@Override
	public void onEvent(String destination, String schemaName, String tableName, CanalEntry.RowChange rowChange) {
		this.header.doChain(destination, schemaName, tableName, rowChange);
	}
	
	
}
