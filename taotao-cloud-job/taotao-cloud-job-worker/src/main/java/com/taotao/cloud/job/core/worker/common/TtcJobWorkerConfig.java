package com.taotao.cloud.job.core.worker.common;

import com.taotao.cloud.job.common.constant.RemoteConstant;
import com.taotao.cloud.job.core.worker.processor.ProcessResult;
import com.taotao.cloud.job.core.worker.processor.ProcessorLoader;
import com.taotao.cloud.job.core.worker.processor.factory.ProcessorFactory;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class TtcJobWorkerConfig {
    /**
     * AppName, recommend to use the name of this project
     * Applications should be registered by powerjob-console in advance to prevent error.
     */
    private String appName;
    /**
     * Worker port
     * Random port is enabled when port is set with non-positive number.
     */
    private int port = RemoteConstant.DEFAULT_WORKER_GRPC_PORT;

    private int serverPort = RemoteConstant.DEFAULT_SERVER_GRPC_PORT;

    private String nameServerAddress;
    /**
     * Address of powerjob-server node(s)
     * Do not mistake for ActorSystem port. Do not add any prefix, i.e. http://.
     */
    private List<String> serverAddress = new ArrayList<>();

    /**
     * Max length of response result. Result that is longer than the value will be truncated.
     * {@link ProcessResult} max length for #msg
     */
    private int maxResultLength = 8096;
//    /**
//     * User-defined context object, which is passed through to the TaskContext#userContext property
//     * Usage Scenarios: The container Java processor needs to use the Spring bean of the host application, where you can pass in the ApplicationContext and get the bean in the Processor
//     */
//    private Object userContext;

//    /**
//     * Max length of appended workflow context value length. Appended workflow context value that is longer than the value will be ignore.
//     * {@link WorkflowContext} max length for #appendedContextData
//     */
//    private int maxAppendedWfContextLength = 8192;

//    /**
//     * Processor factory for custom logic, generally used for IOC framework processor bean injection that is not officially supported by PowerJob
//     */
    private List<ProcessorFactory> processorFactoryList;

//    private String tag;
    /**
     * Max numbers of LightTaskTacker
     */
    private Integer maxLightweightTaskNum = 1024;
    /**
     * Max numbers of HeavyTaskTacker
     */
    private Integer maxHeavyweightTaskNum = 64;
    /**
     * Interval(s) of worker health report
     */
    private Integer healthReportInterval = 10;

    @Getter
    @Setter
    private static ProcessorLoader processorLoader;



}
