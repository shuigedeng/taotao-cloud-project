package com.taotao.cloud.design.patterns.pipeline;

import com.taotao.cloud.design.patterns.pipeline.demo.DemoReq;
import com.taotao.cloud.design.patterns.pipeline.demo.DemoResp;
import com.taotao.cloud.design.patterns.pipeline.demo.PipelineForManagerSubmit;
import lombok.Data;
import org.springframework.stereotype.Service;


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






