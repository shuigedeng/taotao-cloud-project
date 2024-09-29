###########################################
cd /root/taotao-cloud/elasticsearch_exporter

wget https://github.com/prometheus-community/elasticsearch_exporter/releases/download/v1.2.1/elasticsearch_exporter-1.2.1.linux-arm64.tar.gz

tar -zxvf elasticsearch_exporter-1.2.1.linux-arm64.tar.gz

mv elasticsearch_exporter-1.2.1.linux-arm64  elasticsearch_exporter

# es集群1：10.xxx.xxx.10:9200
nohup ./elasticsearch_exporter
--es.all
--es.indices
--es.cluster_settings
--es.indices_settings
 --es.shards
 --es.snapshots
 --es.timeout=10s
 --web.listen-address=":9114"
 --web.telemetry-path="/metrics"
 --es.uri http://192.168.10.220:9200 &

curl "http://127.0.0.1:9114/metrics

- job_name: 'elasticsearch'
    scrape_interval: 60s
    scrape_timeout:  30s
    metrics_path: "/metrics"
    static_configs:
    - targets: ['10.144.1.121:9114']

grafana/dashboards/2322
