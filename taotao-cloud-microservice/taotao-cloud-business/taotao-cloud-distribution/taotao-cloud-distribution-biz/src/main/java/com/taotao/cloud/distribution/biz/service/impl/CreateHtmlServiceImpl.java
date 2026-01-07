/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.distribution.biz.service.impl;

import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.cloud.distribution.biz.service.CreateHtmlService;
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
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Service
public class CreateHtmlServiceImpl implements CreateHtmlService {

    private static int corePoolSize = Runtime.getRuntime().availableProcessors();
    // 多线程生成静态页面
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(
            corePoolSize, corePoolSize + 1, 10l, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(1000));

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
        for (Seckill seckill : list) {
            resultList.add(executor.submit(new createhtml(seckill)));
        }
        for (Future<String> fs : resultList) {
            try {
                LogUtils.info(fs.get()); // 打印各个线任务执行的结果，调用future.get() 阻塞主线程，获取异步任务的返回结果
            } catch (InterruptedException e) {
                LogUtils.error(e);
            } catch (ExecutionException e) {
                LogUtils.error(e);
            }
        }
        return Result.ok();
    }

    class createhtml implements Callable<String> {

        Seckill seckill;

        public createhtml(Seckill seckill) {
            this.distribution = seckill;
        }

        @Override
        public String call() throws Exception {
            Template template = configuration.getTemplate("goods.flt");
            File file = new File(path + seckill.getSeckillId() + ".html");
            Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            template.process(seckill, writer);
            return "success";
        }
    }
}
