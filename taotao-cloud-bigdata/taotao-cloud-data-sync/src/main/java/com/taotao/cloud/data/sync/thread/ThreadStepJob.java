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

package com.taotao.cloud.data.sync.thread;

import com.taotao.boot.common.utils.log.LogUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

/**
 * 默认的情况下，步骤基本上在单线程中执行，那能不能在多线程环境执行呢？答案肯定是yes，但是也要注意，多线程环境步骤执行一定要慎重。原因：多线程环境下，步骤是要设置不可重启。
 * <p>
 * Spring Batch 的多线程步骤是使用Spring 的 TaskExecutor(任务执行器)实现的。约定每一个块开启一个线程独立执行。
 * <p>
 * 分5个块处理user-thread.txt文件
 * <p>
 * 1：userItemReader() 加上saveState(false) Spring Batch
 * 提供大部分的ItemReader是有状态的，作业重启基本通过状态来确定作业停止位置，而在多线程环境中，如果对象维护状态被多个线程访问，可能存在线程间状态相互覆盖问题。所以设置为false表示关闭状态，但这也意味着作业不能重启了。
 * <p>
 * 2：step() 方法加上 .taskExecutor(new SimpleAsyncTaskExecutor())
 * 为作业步骤添加了多线程处理能力，以块为单位，一个块一个线程，观察上面的结果，很明显能看出输出的顺序是乱序的。改变 job 的名字再执行，会发现输出数据每次都不一样。
 */
@SpringBootApplication
@EnableBatchProcessing
public class ThreadStepJob {

    @Autowired private JobBuilderFactory jobBuilderFactory;
    @Autowired private StepBuilderFactory stepBuilderFactory;

    @Bean
    public FlatFileItemReader<User> userItemReader() {

        LogUtils.info(String.valueOf(Thread.currentThread()));

        FlatFileItemReader<User> reader =
                new FlatFileItemReaderBuilder<User>()
                        .name("userItemReader")
                        .saveState(false) // 防止状态被覆盖
                        .resource(new ClassPathResource("user-thread.txt"))
                        .delimited()
                        .delimiter("#")
                        .names("id", "name", "age")
                        .targetType(User.class)
                        .build();

        return reader;
    }

    @Bean
    public ItemWriter<User> itemWriter() {
        return new ItemWriter<User>() {
            @Override
            public void write(Chunk<? extends User> items) throws Exception {
                items.forEach(System.err::println);
            }
        };
    }

    @Bean
    public Step step() {
        return stepBuilderFactory
                .get("step1")
                .<User, User>chunk(2)
                .reader(userItemReader())
                .writer(itemWriter())
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get("thread-step-job").start(step()).build();
    }

    public static void main(String[] args) {
        SpringApplication.run(ThreadStepJob.class, args);
    }
}
