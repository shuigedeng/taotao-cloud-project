package com.taotao.cloud.java.javase.day18.chap18_2;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 线程安全问题
 * @author shuigedeng
 *
 */
public class Demo1 {
	public static void main(String[] args) throws Exception{
		//SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		DateTimeFormatter dtf=DateTimeFormatter.ofPattern("yyyyMMdd");
		ExecutorService pool = Executors.newFixedThreadPool(10);
		Callable<LocalDate> callable=new Callable<LocalDate>() {
			@Override
			public LocalDate call() throws Exception {
				return LocalDate.parse("20200525",dtf);
			}
		};
		List<Future<LocalDate>> list=new ArrayList<>();
		for(int i=0;i<10;i++) {
			Future<LocalDate> future=pool.submit(callable);
			list.add(future);
		}
		
		for (Future<LocalDate> future : list) {
			System.out.println(future.get());
		}
		pool.shutdown();
		
	}
}
