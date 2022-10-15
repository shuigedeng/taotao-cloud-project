### NPS

一款轻量级、高性能、功能强大的内网穿透代理服务器。支持tcp、udp、socks5、http等几乎所有流量转发，可用来访问内网网站、本地支付接口调试、ssh访问、远程桌面，内网dns解析、内网socks5代理等等……，并带有功能强大的web管理端。

```shell
# 运行服务端
docker-compose -f docker-compose-nps.yml -p nps up -d
```

访问地址：[`http://ip地址:8080`](http://www.zhengqingya.com:8080)
默认登录账号密码：`admin/123`

#### 服务端配置

新增客户端

![nps-客户端配置.png](./../../image/nps-客户端配置.png)

新增TCP隧道

![nps-tcp隧道配置.png](./../../image/nps-tcp隧道配置.png)

#### 客户端

```shell
docker run -d --name npc --net=host ffdfgdfg/npc:v0.26.10 -server=服务端ip地址:8024 -vkey=唯一验证密钥 -type=tcp
```
