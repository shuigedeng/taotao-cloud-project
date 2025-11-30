package com.taotao.cloud.xxljob.scheduler.route.strategy;

import com.taotao.cloud.xxljob.scheduler.scheduler.XxlJobScheduler;
import com.taotao.cloud.xxljob.scheduler.route.ExecutorRouter;
import com.taotao.cloud.xxljob.util.I18nUtil;
import com.xxl.job.core.biz.ExecutorBiz;
import com.xxl.tool.response.Response;
import com.xxl.job.core.biz.model.TriggerParam;

import java.util.List;

/**
 * Created by xuxueli on 17/3/10.
 */
public class ExecutorRouteFailover extends ExecutorRouter {

    @Override
    public Response<String> route(TriggerParam triggerParam, List<String> addressList) {

        StringBuffer beatResultSB = new StringBuffer();
        for (String address : addressList) {
            // beat
            Response<String> beatResult = null;
            try {
                ExecutorBiz executorBiz = XxlJobScheduler.getExecutorBiz(address);
                beatResult = executorBiz.beat();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                beatResult = Response.ofFail(e.getMessage() );
            }
            beatResultSB.append( (beatResultSB.length()>0)?"<br><br>":"")
                    .append(I18nUtil.getString("jobconf_beat") + "：")
                    .append("<br>address：").append(address)
                    .append("<br>code：").append(beatResult.getCode())
                    .append("<br>msg：").append(beatResult.getMsg());

            // beat success
            if (beatResult.isSuccess()) {

                beatResult.setMsg(beatResultSB.toString());
                beatResult.setContent(address);
                return beatResult;
            }
        }
        return Response.ofFail( beatResultSB.toString());

    }
}
