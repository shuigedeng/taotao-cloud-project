### Zipkin - 分布式追踪系统

> 它有助于收集对服务架构中的延迟问题进行故障排除所需的计时数据。功能包括收集和查找这些数据。
> 注：记得创建相应zipkin库和表

```shell
docker-compose -f docker-compose-zipkin.yml -p zipkin up -d
```

启动成功日志如下：

```
                  oo
                 oooo
                oooooo
               oooooooo
              oooooooooo
             oooooooooooo
           ooooooo  ooooooo
          oooooo     ooooooo
         oooooo       ooooooo
        oooooo   o  o   oooooo
       oooooo   oo  oo   oooooo
     ooooooo  oooo  oooo  ooooooo
    oooooo   ooooo  ooooo  ooooooo
   oooooo   oooooo  oooooo  ooooooo
  oooooooo      oo  oo      oooooooo
  ooooooooooooo oo  oo ooooooooooooo
      oooooooooooo  oooooooooooo
          oooooooo  oooooooo
              oooo  oooo
     ________ ____  _  _____ _   _
    |__  /_ _|  _ \| |/ /_ _| \ | |
      / / | || |_) | ' / | ||  \| |
     / /_ | ||  __/| . \ | || |\  |
    |____|___|_|   |_|\_\___|_| \_|
:: version 2.23.2 :: commit 7bf3aab ::
2021-08-30 16:33:27.414  INFO [/] 1 --- [oss-http-*:9411] c.l.a.s.Server                           : Serving HTTP at /[0:0:0:0:0:0:0:0%0]:9411 - http://127.0.0.1:9411/
```

可视化界面访问地址：[`http://ip地址:9411/zipkin`](http://www.zhengqingya.com:9411/zipkin)
