package com.taotao.cloud.health.alarm.core.loader;


import com.taotao.cloud.health.alarm.core.exception.NoAlarmLoaderSpecifyException;
import com.taotao.cloud.health.alarm.core.loader.api.IConfLoader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

public class ConfLoaderFactory {

	private static IConfLoader currentAlarmConfLoader;

	public static IConfLoader loader() {
		if (currentAlarmConfLoader == null) {
			synchronized (ConfLoaderFactory.class) {
				if (currentAlarmConfLoader == null) {
					initConfLoader();
				}
			}
		}

		return currentAlarmConfLoader;
	}


	private static void initConfLoader() {
		Iterator<IConfLoader> iterator = ServiceLoader.load(IConfLoader.class).iterator();

		List<IConfLoader> list = new ArrayList<>();

		// 根据优先级进行排序，选择第一个加载成功的Loader
		while (iterator.hasNext()) {
			list.add(iterator.next());
		}
		list.sort(Comparator.comparingInt(IConfLoader::order));

		List<IConfLoader> ans = new ArrayList<>(list.size());

		for (IConfLoader iConfLoader : list) {
			if (iConfLoader.load()) {
				ans.add(iConfLoader);
			}
		}

		if (ans.isEmpty()) {
			throw new NoAlarmLoaderSpecifyException("no special alarmConfLoader selected!");
		}

		// 将currentAlarmConfLoader 设置为代理类，内部实现根据报警类型，选择具体的报警解析规则，执行报警
		currentAlarmConfLoader = new ConfLoaderProxy(ans);
	}


}
