### Grafana Loki - 一个水平可扩展，高可用性，多租户的日志聚合系统

```shell
# 先授权，否则会报错：`cannot create directory '/var/lib/grafana/plugins': Permission denied`
chmod 777 $PWD/grafana_promtail_loki/grafana/data
chmod 777 $PWD/grafana_promtail_loki/grafana/log

# 运行
docker-compose -f docker-compose-grafana-promtail-loki.yml -p grafana_promtail_loki up -d
```

访问地址：[`http://ip地址:3000`](http://www.zhengqingya.com:3000)
默认登录账号密码：`admin/admin`
