# csx-bsf-health
## 简介
csx-bsf-health 是BSF重要组成部分，自研框架，用来检测业务系统各个方面的性能参数及异常告警。

## 依赖POM

```
<dependency>
	<artifactId>csx-bsf-health</artifactId>
	<groupId>com.yh.csx.bsf</groupId>
	<version>1.7.1-SNAPSHOT</version>
</dependency>
```
## 使用介绍
web项目依赖并开启健康检查,项目启动后访问http://{ip}:{port}/{context}/bsf/health/即可查看项目实时各项监测参数
[样例](doc/health.html)


## 配置参数介绍

```shell 
# =========将康检查相关开关=================================
# 健康检查模块开关
bsf.health.enabled=true

#==========报警查相关参数===================================
# 告警开关
bsf.health.warn.enabled=true
# 自动健康检查时间间隔
bsf.health.timespan=10
# 报警消息缓存数量
bsf.health.warn.cachecount=3
# 报警消息循环间隔时间（秒）
bsf.health.warn.timespan=10 
# 报警重复过滤时间间隔 分钟
bsf.health.warn.duplicate.timespan=2
# 钉钉报警系统token
bsf.health.warn.dingding.system.access_token=
# 钉钉报警项目token
bsf.health.warn.dingding.project.access_token=
# 钉钉报警过滤ip
bsf.health.warn.dingding.filter.ip

    
#==========健康检查相关报警餐宿==================================
# 自动健康检查服务开关
bsf.health.check.enabled=true
# 自动上报健康状态开关
bsf.health.export.enabled=true
# 自动上报ELK
bsf.health.export.elk.enabled = false
# 自动上报cat 
bsf.health.export.cat.enabled = true
# 异常捕获告警
bsf.health.uncatch.enabled = true


#==========应用的中间件使用参数监控================================
bsf.health.jedis.enabled=true
bsf.health.dataSource.enabled=true
bsf.health.xxljob.enabled=true
bsf.health.rocketmq.enabled=true
bsf.health.elk.enabled=true
bsf.health.mybatis.enabled=true
bsf.health.file.enabled=true
bsf.health.elasticSearch.enabled=true

#==========系统参数监控===========================================
# CPU&IO&Memery报警配置参数
bsf.health.strategy.cpu.process = [>0.8]
bsf.health.strategy.cpu.system = [>0.8]
bsf.health.strategy.io.current.dir.usable.size = [<500]
bsf.health.strategy.memery.system.free = [<1024]
bsf.health.strategy.memery.jvm.max = [<350]
# tomcat 报警参数
bsf.health.strategy.tomcat.threadPool.queue.size = [>20]
bsf.health.strategy.tomcat.threadPool.active.count = [>100]
# 自动清理日志；磁盘不足，告警后自动清理日志
bsf.health.io.autoClear = true

#===========其他监控配置============================================
# dump功能开启
bsf.health.dump.enabled=true
# HTTP将康检查
bsf.health.ping.enabled=true
# 内存泄漏可疑接口检查
bsf.health.doubtapi.enabled=true

# 日志报警开关及规则：每分钟60条错误日志，每分钟日志增量3000(防止爆刷日志)
bsf.health.log.statistic.enabled = true
bsf.health.strategy.log.error.count = [>60] 
bsf.health.strategy.log.incre.count = [>3000]

# 可疑接口（内存泄漏检查）
bsf.health.doubtapi.enabled = true
bsf.health.doubtapi.threshold = 3145728

# 文件上传检查
bsf.health.file.enabled = true
bsf.health.file.timeSpan = 20


```


