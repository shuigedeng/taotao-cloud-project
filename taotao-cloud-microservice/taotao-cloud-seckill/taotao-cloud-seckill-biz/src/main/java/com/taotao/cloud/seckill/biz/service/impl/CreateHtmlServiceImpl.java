package com.taotao.cloud.seckill.biz.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.itstyle.seckill.common.entity.Result;
import com.itstyle.seckill.common.entity.Seckill;
import com.itstyle.seckill.repository.SeckillRepository;
import com.itstyle.seckill.service.ICreateHtmlService;

import freemarker.template.Configuration;
import freemarker.template.Template;
@Service
public class CreateHtmlServiceImpl implements ICreateHtmlService {
	
	private static int corePoolSize = Runtime.getRuntime().availableProcessors();
	//多线程生成静态页面
	private static ThreadPoolExecutor executor  = new ThreadPoolExecutor(corePoolSize, corePoolSize+1, 10l, TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>(1000));
	
	@Autowired
	public Configuration configuration;
	@Autowired
	private SeckillRepository seckillRepository;
	@Value("${spring.freemarker.html.path}")
	private String path;
	
	@Override
	public Result createAllHtml() {
		List<Seckill> list = seckillRepository.findAll();
		final List<Future<String>> resultList = new ArrayList<Future<String>>(); 
		for(Seckill seckill:list){
			resultList.add(executor.submit(new createhtml(seckill)));
		}
	   for (Future<String> fs : resultList) { 
           try {
				System.out.println(fs.get());//打印各个线任务执行的结果，调用future.get() 阻塞主线程，获取异步任务的返回结果
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
       } 
		return Result.ok();
	}
	class createhtml implements Callable<String>  {
		Seckill seckill;

		public createhtml(Seckill seckill) {
			this.seckill = seckill;
		}
		@Override
		public String call() throws Exception {
			Template template = configuration.getTemplate("goods.flt");
			File file= new File(path+seckill.getSeckillId()+".html");
			Writer  writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
			template.process(seckill, writer);
			return "success";
		}
	}
}
