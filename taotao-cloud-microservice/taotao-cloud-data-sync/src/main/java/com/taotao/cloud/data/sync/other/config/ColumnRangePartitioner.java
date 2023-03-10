package com.taotao.cloud.data.sync.other.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

 
/**
 * @create 2019年4月2日
 * Content :分区读取算法
 * mod(字段,total) = current - 1
 */

public class ColumnRangePartitioner implements Partitioner {


   @Override
   public Map<String, ExecutionContext> partition(int gridSize) {
      
       Map<String, ExecutionContext> result = new LinkedHashMap<String, ExecutionContext>();
       int current_thread = 1 ;
       int total_thread     = gridSize ;
       while (current_thread <=  total_thread) {
           ExecutionContext value = new ExecutionContext();
           result.put("partition" + current_thread, value);

           value.putInt("current_thread", current_thread);
           value.putInt("total_thread", total_thread);

           current_thread++;
       }

       return result;
   	
   }
}
