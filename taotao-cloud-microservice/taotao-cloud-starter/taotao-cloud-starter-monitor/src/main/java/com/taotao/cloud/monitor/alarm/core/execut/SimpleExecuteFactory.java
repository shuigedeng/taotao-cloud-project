package com.taotao.cloud.monitor.alarm.core.execut;


import com.taotao.cloud.monitor.alarm.core.exception.DuplicatedAlarmExecuteDefinedException;
import com.taotao.cloud.monitor.alarm.core.execut.api.IExecute;
import com.taotao.cloud.monitor.alarm.core.execut.spi.LogExecute;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

public class SimpleExecuteFactory {

	private static Map<String, IExecute> cacheMap;


	private static void loadAlarmExecute() {
		Map<String, IExecute> map = new HashMap<>();
		Iterator<IExecute> iExecutes = ServiceLoader.load(IExecute.class).iterator();
		IExecute tmp;
		while (iExecutes.hasNext()) {
			tmp = iExecutes.next();
			if (!map.containsKey(tmp.getName())) {
				map.put(tmp.getName(), tmp);
			} else {
				throw new DuplicatedAlarmExecuteDefinedException(
					"duplicated alarm executor defined!" +
						"\n" +
						">>name:" +
						tmp.getName() +
						">>>clz:" +
						tmp.getClass() +
						">>>clz:" +
						map.get(tmp.getName())
				);
			}
		}

		cacheMap = map;
	}

	public static IExecute getExecute(String execute) {
		if (cacheMap == null) {
			synchronized (SimpleExecuteFactory.class) {
				if (cacheMap == null) {
					loadAlarmExecute();
				}
			}
		}

		// 如果不存在，则降级为 LogExecute
		IExecute e = cacheMap.get(execute);
		return e == null ? cacheMap.get(LogExecute.NAME) : e;

	}
}
