### SonarQube

一款静态代码质量分析工具，支持Java、Python、PHP、JavaScript、CSS等25种以上的语言，而且能够集成在IDE、Jenkins、Git等服务中，方便随时查看代码质量分析报告。

```shell
docker-compose -f docker-compose-sonarqube.yml -p sonarqube up -d
# mysql配置版
# docker-compose -f docker-compose-sonarqube6.7.1.yml -p sonarqube up -d
```

访问地址：[`http://ip地址:9005`](http://www.zhengqingya.com:9005)
默认登录账号密码：`admin/admin`
