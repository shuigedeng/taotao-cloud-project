package com.taotao.cloud.spark.atguigu.core.framework.common

import com.taotao.cloud.bigdata.spark.atguigu.core.framework.util.EnvUtil


trait TDao {

    def readFile(path:String) = {
        EnvUtil.take().textFile(path)
    }
}
