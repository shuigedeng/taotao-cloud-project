### Rancher - 开源容器管理平台

```shell
# 运行
docker-compose -f docker-compose-rancher.yml -p rancher up -d
# 查看密码
docker logs rancher 2>&1 | grep "Bootstrap Password:"
# 2022/04/04 11:39:52 [INFO] Bootstrap Password: 4jtd9fw2dt9t9qs5ffrs4srvf5xl95z6jbgm2qqpb97276qg8jgkgl
# Server URL
https://www.zhengqingya.com
```

访问地址：[`http://ip地址:80`](http://www.zhengqingya.com:80)