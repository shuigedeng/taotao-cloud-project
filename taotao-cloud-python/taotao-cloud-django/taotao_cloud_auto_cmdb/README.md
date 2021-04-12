# AutoCMDB
# 架构
## 资产采集
### 兼容当前通用的3种方式进行资产采集：
* Agent型，将资产采集插件部署在服务器上
* SSH型，利用中控机结合paramiko执行采集命令
* RPC型，利用中控机结合SaltStack执行采集命令，工作方式同SSH，但是通信原理为RPC，速度较SSH快

![](https://github.com/jackupdown/AutoCMDB/raw/master/mdPics/CMDB_method.png)
### 可插拔式插件（借鉴Django中间件）
## API
* API验证（借鉴Tornado加密cookie）
## 后台管理
* CURD组件
