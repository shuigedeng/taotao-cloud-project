package com.taotao.cloud.data.sync.other.item;

import java.util.List;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

/**
 * @create 2019年4月2日
 * Content :数据输出item
 */
@Component
@StepScope
public class DeliverPostWriterItem<T> implements ItemWriter<T> {
	
	 
    @Override
    public void write(List<? extends T> list) throws Exception {
    	
    	System.out.println(list);
    }
}
