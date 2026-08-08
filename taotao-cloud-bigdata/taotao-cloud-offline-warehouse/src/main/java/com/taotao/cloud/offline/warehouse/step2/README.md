### 行为数据采集

### 1. openresty安装
主要包括

1.安装

2.配置文件

3.日志切分

### 2.hadoop安装

### 3.flume安装

1.主要包括编写flume拦截器(解析、合并日志)taotao-cloud-bigdata -> taotao-cloud-flume 模块

2.部署拦截器(打包 上传jar到flume安装路径lib目录下)

3.执行flume命令 就可以从openresty中采集数据到hadoop中

**目的: 通过openresty进行数据采集到磁盘目录上 (按天分目录)**

**目的: 通过flume进行数据采集到hadoop (批量处理数据  flume拦截器(解析、合并日志))**

**目的: 最终数据在hadoop上存储son格式存储 (称之为源文件) (以天分区 json格式存储)**



