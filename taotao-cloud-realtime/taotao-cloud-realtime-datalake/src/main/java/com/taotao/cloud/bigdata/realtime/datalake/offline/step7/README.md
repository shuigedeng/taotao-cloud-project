### 监控作业

### 1.prometheus安装

### 2.supervisor安装
```
supervisor 集成 prometheus

vim /etc/supervisor.d/prometheus.conf

; prometheus启动配置文件

;[program:prometheus]
directory=/root/taotao-common/prometheus2.23.0 
command=/root/taotao-common/prometheus2.23.0/prometheus \
        --storage.tsdb.path="/root/taotao-common/prometheus2.23.0/data" \
        --log.level=debug \
        --web.enable-lifecycle \
        --web.enable-admin-api \
        --config.file="/root/taotao-common/prometheus2.23.0/prometheus.yml"
stderr_logfile=/root/taotao-common/prometheus2.23.0/supervisor/logs/prometheus.error
stdout_logfile=/root/taotao-common/prometheus2.23.0/supervisor/logs/prometheus.log
stderr_logfile_maxbytes=10MB
stderr_logfile_backups=10
user=root
autostart=true
autorestart=true
startsecs=10
startretries=3
redirect_stderr=false
killasgroup=true
stopasgroup=true

supervisorctl reload
```

### 3.grafana的安装
```
supervisor 集成 grafana

vim /etc/supervisor.d/grafana.conf

; grafana启动配置文件

;[program:grafana]
directory=/root/taotao-common/grafana7.3.7
command=sh -c "bin/grafana-server -config config/grafana.ini"
stderr_logfile=/root/taotao-common/grafana7.3.7/supervisor/logs/grafana.error
stdout_logfile=/root/taotao-common/grafana7.3.7/supervisor/logs/grafana.log
stderr_logfile_maxbytes=10MB
stderr_logfile_backups=10
user=root
autostart=true
autorestart=true
startsecs=10
startretries=3
redirect_stderr=false
killasgroup=true
stopasgroup=true

supervisorctl reload

#################################################
prometheus 集成 grafana

进入到页面 http://taotao-cloud:3000  admin/admin(grafana)

add datasource  http://taotao-cloud:9090

```

### 4.nginx监控
```
prometheus 集成 openresty

-- nginx 监控 使用 nginx-lua-prometheus

https://github.com/knyar/nginx-lua-prometheus/blob/master/prometheus.lua

# nginx-lua-prometheus
wget https://github.com/knyar/nginx-lua-prometheus/archive/0.20201218.zip

unzip 0.20201218.zip
mv nginx-lua-prometheus-0.20201218/ nginx-lua-prometheus
cp nginx-lua-prometheus /usr/local/openresty/lualib/resty/

mv /usr/local/openresty/lualib/resty/nginx-lua-prometheus  /usr/local/openresty/lualib/resty/prometheus

nginx.conf http 模块 添加如下配置

lua_package_path "/usr/local/openresty/lualib/resty/prometheus/?.lua;/usr/local/openresty/lualib/resty/kafka/?.lua;;";
lua_package_cpath "/usr/local/openresty/lualib/?.so;;";

添加metrics.conf 文件  @see step2 -> openresty1.19.3.1-> metrics.conf

include metrics.conf;


vim /root/taotao-common/prometheus2.23.0/prometheus.yml
scrape_configs:
  # The job name is added as a label `job=<job_name>` to any timeseries scraped from this config.
  - job_name: 'prometheus'
    static_configs:
    - targets: ['localhost:9090']

  - job_name: 'taotao-cloud-weblog-collect'
    scrape_interval: 30s
    static_configs:
    - targets: ['host:9527']

```

### 5.flume监控
```
prometheus 集成 flume

-- flume监控 使用 flume-exporter

git clone https://github.com/woozhijun/flume_exporter.git

cd flume_exporter 

dnf install go

make build


cd /root/taotao-bigdata/flume_exporter
vim  config.yml

agents:
- name: "flume-agents"
  enabled: true
# multiple urls can be separated by ,
  urls: ["http://localhost:31001/metrics","http://localhost:31002/metrics]


vim /root/taotao-common/prometheus2.23.0/prometheus.yml
scrape_configs:
  # The job name is added as a label `job=<job_name>` to any timeseries scraped from this config.
  - job_name: 'prometheus'
    static_configs:
    - targets: ['localhost:9090']

  - job_name: 'taotao-cloud-weblog-collect'
    scrape_interval: 30s
    static_configs:
    - targets: ['host:9527']

  - job_name: 'flume-exporter'
    scrape_interval: 30s
    static_configs:
    - targets: ['127.0.0.1:9360']
```


**目的: 通过prometheus grafana 实行监控作业**

**目的: 监控nginx 使用nginx-lua-prometheus**

**目的: 监控flume 使用 flume-exporter**












