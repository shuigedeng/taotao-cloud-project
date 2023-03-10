package com.taotao.cloud.data.sync.other.support;

import java.util.Date;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;

public class BatchIncrementer implements JobParametersIncrementer {

    @Override
    public JobParameters getNext(JobParameters parameters){
        return new JobParametersBuilder()
                .addLong("batchDate",new Date().getTime())
                .toJobParameters();
    }
}
