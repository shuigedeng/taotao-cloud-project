package com.taotao.cloud.xxljob.scheduler.route.strategy;

import com.taotao.cloud.xxljob.scheduler.route.ExecutorRouter;
import com.xxl.tool.response.Response;
import com.xxl.job.core.biz.model.TriggerParam;

import java.util.List;

/**
 * Created by xuxueli on 17/3/10.
 */
public class ExecutorRouteFirst extends ExecutorRouter {

    @Override
    public Response<String> route(TriggerParam triggerParam, List<String> addressList){
        return Response.ofSuccess(addressList.get(0));
    }

}
