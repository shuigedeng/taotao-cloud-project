package com.taotao.cloud.spark.atguigu.core.framework.application

import com.taotao.cloud.bigdata.spark.atguigu.core.framework.common.TApplication
import com.taotao.cloud.bigdata.spark.atguigu.core.framework.controller.WordCountController


object WordCountApplication extends App with TApplication{

    // 启动应用程序
    start(){
        val controller = new WordCountController()
        controller.dispatch()
    }

}
