### 新闻资讯数据采集

### 1.flume配置文件

1.执行flume命令 就可以从http 端口中采集数据到hadoop中

nohup ${FLUME_HOME}/bin/flume-ng agent -n taotao-cloud-article-log -c $FLUME_HOME/conf \
-f ${FLUME_HOME}/conf/taotao-cloud-article-log -Dflume.root.logger=INFO,console \
-Dflume.monitoring.type=http -Dflume.monitoring.port=31001

**目的: 通过flume source类型为http 采集新闻资讯数据到hadoop中 (以天分区 json格式存储)**
