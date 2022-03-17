//package com.taotao.cloud.sys.biz.timetask.xxljob.hotwords;
//
//import com.taotao.cloud.web.timetask.EveryDayExecute;
//import groovy.util.logging.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// **/
//@Slf4j
//@Component
//public class HotWordsEveryDayTaskExecute implements EveryDayExecute {
//
//    @Autowired
//    private Cache cache;
//
//    /**
//     * 执行每日任务
//     */
//    @Override
//    public void execute() {
//        //移除昨日的热搜词
//        cache.remove(CachePrefix.HOT_WORD.getPrefix());
//    }
//
//}
