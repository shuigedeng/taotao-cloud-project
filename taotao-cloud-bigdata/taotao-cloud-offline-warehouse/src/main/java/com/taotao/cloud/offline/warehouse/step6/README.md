### 作业调度

### 1.azkaban安装

### 2.创建作业流

### 3.上传zip包

将news.project和ods_flow.yaml压缩成zip包

http://127.0.0.1:8081 创建项目上传zip包

### 4.azkaban添加定时作业调度


### 5.钉钉sdk安装
```
下载钉钉sdk 安装到本地maven仓库

wget https://open-dev.dingtalk.com/download/openSDK/dingtalk-sdk-java.zip

unzip dingtalk-sdk-java.zip

mvn install:install-file -Dfile=/root/soft/dingtalk-sdk-java/taobao-sdk-java-auto_1479188381469-20210120.jar 
-DgroupId=com.dingtalk -DartifactId=dingtalk-api -Dversion=1.0.0 -Dpackaging=jar

之后就可以再pom中引用了
```

### 6.钉钉报警插件开发 

@see taotao-cloud-bigdat:taotao-cloud-azkaban

```
打包部署

mkdir -p /root/taotao-bigdata/azkaban3.90.0/plugins/alerter/azkaban-dingtalk{conf, lib}

cp taotao-cloud-azkaban-1.0-all.jar /root/taotao-bigdata/azkaban3.90.0/plugins/alerter/azkaban-dingtalk/lib

cd /root/taotao-bigdata/azkaban3.90.0/plugins/alerter/azkaban-dingtalk/conf

vim plugin.properties

alerter.name=dingtalk
alerter.class=com.taotao.cloud.bigdata.azkaban.DingtalkAlert
ding.token=0121434223423425345234234
ding.auth.word=taotaocloud
ding.link.azkaban.host=http://taotao-cloud:8081
ding.alert.on.success=false
ding.alert.on.error=true
ding.alert.on.first.error=false

直接启动 会自动加载jia包 和 配置文件
```

**目的: 通过azkaban创建作业调度 每天调度一次**












