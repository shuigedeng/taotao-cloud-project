package com.taotao.cloud.flink.doe.custom;


import com.taotao.cloud.flink.doe.beans.OrdersBean;
import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.triggers.TriggerResult;
import org.apache.flink.streaming.api.windowing.windows.Window;

/**
 * @Date: 2024/1/3
 * @Author: Hang.Nian.YY
 * @WX: 17710299606
 * @Tips: 学大数据 ,到多易教育
 * @DOC: https://blog.csdn.net/qq_37933018?spm=1000.2115.3001.5343
 * @Description:
 */
public class MyTrigger extends Trigger {
    @Override
    public TriggerResult onElement(Object element, long timestamp, Window window, TriggerContext ctx) throws Exception {

        OrdersBean bean =  (OrdersBean)element ;
            if (window.maxTimestamp() <= ctx.getCurrentWatermark() || bean.getCity().equals("bj")) {
                // if the watermark is already past the window fire immediately
                return TriggerResult.FIRE;
            } else {
                ctx.registerEventTimeTimer(window.maxTimestamp());
                return TriggerResult.CONTINUE;
            }
    }

    @Override
    public TriggerResult onProcessingTime(long time, Window window, TriggerContext ctx) throws Exception {
        return TriggerResult.CONTINUE;
    }

    @Override
    public TriggerResult onEventTime(long time, Window window, TriggerContext ctx) throws Exception {
        return time == window.maxTimestamp() ? TriggerResult.FIRE : TriggerResult.CONTINUE;
    }

    @Override
    public void clear(Window window, TriggerContext ctx) throws Exception {
        ctx.deleteEventTimeTimer(window.maxTimestamp());
    }

    @Override
	public boolean canMerge() {
        return true;
    }


    public static MyTrigger create() {
        return new MyTrigger();
    }
}
