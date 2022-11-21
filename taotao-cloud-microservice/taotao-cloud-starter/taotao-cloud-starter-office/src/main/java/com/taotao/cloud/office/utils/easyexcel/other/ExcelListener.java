package com.taotao.cloud.office.utils.easyexcel.other;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Excel数据解析监听器， 数据解析方法异步执行
 *
 * @param <T> Excel中数据的类型
 */
//@Getter
//@Setter
//@NoArgsConstructor
public class ExcelListener<T> extends AnalysisEventListener<T> {

	// 加入一个判断标签，判断数据是否已经读取完
	private volatile boolean retryLock = false;

	// 解析完成后的数据集合, 监听对象初始化之后，立即初始化集合对象
	private final List<T> dataList = new ArrayList<>();

	// 每次最多导入条数
	private final int batchSize = 2000;


	/**
	 * 获取解析后的数据集合， 如果数据还没有被解析完成，会对读取该集合的线程进行阻塞，直到数据读取完成之后，进行解锁。 如果一次导入数据超过batchSize条，则以抛异常的形式阻止导入数据
	 *
	 * @return 解析后的数据集合
	 */
	public List<T> getDataList() {
		while (true) {
			if (retryLock) {
				if (dataList.size() > batchSize) {
					// 手动清空数据内存数据，减少内存消耗
					dataList.clear();
					throw new RuntimeException("一次最多导入" + batchSize + "条数据");
				} else {
					return dataList;
				}
			}
		}
	}

	/**
	 * Excel每解析一行数据，就会调用一次该方法
	 *
	 * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
	 * @param context analysis context
	 */
	@Override
	public void invoke(T data, AnalysisContext context) {
		dataList.add(data);
	}

	/**
	 * 读取表头内容
	 *
	 * @param headMap 表头部数据
	 * @param context 数据解析上下文
	 */
	@Override
	public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
		//System.out.println("表头：" + headMap);
	}

	/**
	 * 流中的数据解析完成之后，就会调用此方法
	 *
	 * @param context
	 */
	@Override
	public void doAfterAllAnalysed(AnalysisContext context) {
		// 数据解析完成，解锁
		retryLock = true;
	}

	/**
	 * 解析过程如果发生异常，会调用此方法
	 *
	 * @param exception
	 * @param context
	 */
	@Override
	public void onException(Exception exception, AnalysisContext context) {
		throw new RuntimeException("Excel数据异常，请检查或联系管理员！");
	}


	public int getBatchSize() {
		return batchSize;
	}

	public void setRetryLock(boolean retryLock) {
		this.retryLock = retryLock;
	}

	public boolean isRetryLock() {
		return retryLock;
	}
}
