package com.taotao.cloud.spark.atguigu.core.framework.util

import org.apache.spark.SparkContext

object EnvUtil {

    private val scLocal = new ThreadLocal[SparkContext]()

    def put( sc : SparkContext ): Unit = {
        scLocal.set(sc)
    }

    def take(): SparkContext = {
        scLocal.get()
    }

    def clear(): Unit = {
        scLocal.remove()
    }
}
