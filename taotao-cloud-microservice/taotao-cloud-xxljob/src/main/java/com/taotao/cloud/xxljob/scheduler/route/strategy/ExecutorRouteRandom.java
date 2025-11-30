package com.taotao.cloud.xxljob.scheduler.route.strategy;

import com.taotao.cloud.xxljob.scheduler.route.ExecutorRouter;
import com.xxl.tool.response.Response;
import com.xxl.job.core.biz.model.TriggerParam;

import java.util.List;
import java.util.Random;

/**
 * Created by xuxueli on 17/3/10.
 */
public class ExecutorRouteRandom extends ExecutorRouter {

    private static Random localRandom = new Random();

    @Override
    public Response<String> route(TriggerParam triggerParam, List<String> addressList) {
        String address = addressList.get(localRandom.nextInt(addressList.size()));
        return Response.ofSuccess(address);
    }

}
