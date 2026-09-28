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

package com.taotao.cloud.data.sync.partitioner;

import com.taotao.boot.common.utils.log.LogUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

/**
 * 分区：有划分，区分意思，在SpringBatch 分区步骤讲的是给执行步骤区分上下级。
 * <p>
 * 上级： 主步骤(Master Step)
 * <p>
 * 下级： 从步骤--工作步骤(Work Step)
 * <p>
 * 主步骤是领导，不用干活，负责管理从步骤，从步骤是下属，必须干活。
 * <p>
 * 一个主步骤下辖管理多个从步骤。
 * <p>
 * 注意： 从步骤，不管多小，它也一个完整的Spring Batch 步骤，负责各自的读入、处理、写入等。
 * <p>
 * 分区步骤一般用于海量数据的处理上，其采用是分治思想。主步骤将大的数据划分多个小的数据集，然后开启多个从步骤，要求每个从步骤负责一个数据集。当所有从步骤处理结束，整作业流程才算结束。
 * <p>
 * 1>文件分区器：userPartitioner()， 分别加载5个文件进入到程序
 * <p>
 * 2>文件分区处理器：userPartitionHandler() ，指定要分几个区，由谁来处理
 * <p>
 * 3>分区从步骤：workStep() 指定读逻辑与写逻辑
 * <p>
 * 4>分区文件读取：flatItemReader()，需要传入Resource对象，这个对象在userPartitioner()已经标记为file
 * <p>
 * 5>分区主步骤：masterStep() ，指定分区名称与分区器，指定分区处理器
 */
@EnableBatchProcessing
public class PartStepJob {

    @Autowired private JobBuilderFactory jobBuilderFactory;
    @Autowired private StepBuilderFactory stepBuilderFactory;

    // 每个分区文件读取
    @Bean
    @StepScope
    public FlatFileItemReader<User> flatItemReader(
            @Value("#{stepExecutionContext['file']}") Resource resource) {
        return new FlatFileItemReaderBuilder<User>()
                .name("userItemReader")
                .resource(resource)
                .delimited()
                .delimiter("#")
                .names("id", "name", "age")
                .targetType(User.class)
                .build();
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

    // 文件分区器-设置分区规则
    @Bean
    public UserPartitioner userPartitioner() {
        return new UserPartitioner();
    }

    // 文件分区处理器-处理分区
    @Bean
    public PartitionHandler userPartitionHandler() {
        TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
        handler.setGridSize(5);
        handler.setTaskExecutor(new SimpleAsyncTaskExecutor());
        handler.setStep(workStep());
        try {
            handler.afterPropertiesSet();
        } catch (Exception e) {
            LogUtils.error(e);
        }
        return handler;
    }

    // 每个从分区操作步骤
    @Bean
    public Step workStep() {
        return stepBuilderFactory
                .get("workStep")
                .<User, User>chunk(10)
                .reader(flatItemReader(null))
                .writer(itemWriter())
                .build();
    }

    // 主分区操作步骤
    @Bean
    public Step masterStep() {
        return stepBuilderFactory
                .get("masterStep")
                .partitioner(workStep().getName(), userPartitioner())
                .partitionHandler(userPartitionHandler())
                .build();
    }

    @Bean
    public Job partJob() {
        return jobBuilderFactory.get("part-step-job").start(masterStep()).build();
    }

    public static void main(String[] args) {
        SpringApplication.run(PartStepJob.class, args);
    }
}
