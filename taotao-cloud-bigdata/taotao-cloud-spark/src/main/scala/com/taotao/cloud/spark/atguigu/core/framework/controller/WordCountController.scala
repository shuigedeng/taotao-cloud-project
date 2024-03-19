package com.taotao.cloud.spark.atguigu.core.framework.controller

import com.taotao.cloud.bigdata.spark.atguigu.core.framework.common.TController
import com.taotao.cloud.bigdata.spark.atguigu.core.framework.service.WordCountService


/**
  * 控制层
  */
class WordCountController extends TController {

    private val wordCountService = new WordCountService()

    // 调度
    def dispatch(): Unit = {
        // TODO 执行业务操作
        val array = wordCountService.dataAnalysis()
        array.foreach(println)
    }
}
