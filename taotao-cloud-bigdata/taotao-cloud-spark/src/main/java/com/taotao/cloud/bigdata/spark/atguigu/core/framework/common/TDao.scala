package com.taotao.cloud.bigdata.spark.atguigu.core.framework.common

import com.atguigu.bigdata.spark.core.framework.util.EnvUtil

trait TDao {

    def readFile(path:String) = {
        EnvUtil.take().textFile(path)
    }
}
