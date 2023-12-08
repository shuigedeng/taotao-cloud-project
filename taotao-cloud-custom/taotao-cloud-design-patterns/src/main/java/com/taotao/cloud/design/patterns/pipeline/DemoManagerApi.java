package com.taotao.cloud.design.patterns.pipeline;

import com.example.demo.pipeline.factory.PipelineForManagerSubmit;
import com.taotao.cloud.design.patterns.pipeline.demo.DemoReq;
import lombok.Data;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 演示-API
 *
 */
@Service
public class DemoManagerApi {

    /**
     * 管道-审核提交
     */
    @Resource
    private PipelineForManagerSubmit pipelineForManagerSubmit;

    /**
     * 审核提交
     *
     * @param requestData 请求数据
     * @return {@link DemoResp}
     */
    public DemoResp managerSubmit(DemoReq requestData) {
        return pipelineForManagerSubmit.managerSubmitCheck(requestData);
    }
}






