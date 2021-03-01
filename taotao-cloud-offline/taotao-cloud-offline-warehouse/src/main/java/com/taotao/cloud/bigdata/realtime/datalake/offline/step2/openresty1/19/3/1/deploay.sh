cd /opt/openresty

openresty -p /opt/openresty -c conf/nginx.conf -t
openresty -p /opt/openresty -c conf/nginx.conf
openresty -p /opt/openresty -c conf/nginx.conf -s stop
openresty -p /opt/openresty -c conf/nginx.conf -s reload

echo "0 23 * * * sh /opt/openresty/script/spilt-access-log.sh >> /opt/openresty/script/logs/spilt-access-log.log 2>&1" > /opt/openresty/script/taotao-cloud-access-log.cron

crontab /opt/openresty/script/taotao-cloud-access-log.cron

crontab -l
crontab -e
