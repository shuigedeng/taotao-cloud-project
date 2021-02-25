#/bin/bash

# filename: deploy.sh
# date: 2020-11-23

mkdir -pv /opt/access-log-collect/{conf/vhost,logs/data,script/logs}

cp /usr/local/openresty/nginx/conf/mime.types /opt/access-log-collect/conf
cp /usr/local/openresty/nginx/conf/nginx.conf /opt/access-log-collect/conf

/usr/local/openresty/nginx/sbin/nginx -p /opt/access-log-collect -c conf/nginx.conf -t
/usr/local/openresty/nginx/sbin/nginx -p /opt/access-log-collect -c conf/nginx.conf
/usr/local/openresty/nginx/sbin/nginx -p /opt/access-log-collect -c conf/nginx.conf -s reload

echo "0 23 * * * sh /opt/access-log-collect/script/spilt-access-log.sh >> /opt/access-log-collect/script/logs/spilt-access-log.log 2>&1" > /opt/access-log-collect/script/access-log-collect.cron

crontab /opt/access-log-collect/script/access-log-collect.cron

crontab -l
crontab -e

echo "
-- opt
`-- access-log-collect
    |-- client_body_temp
    |-- conf
    |   |-- mime.types
    |   |-- nginx.conf
    |   `-- vhost
    |       `-- collect_app.conf
    |-- fastcgi_temp
    |-- logs
    |   |-- access.log
    |   |-- collect-app.access.log
    |   |-- data
    |   |   |-- access-log-collect.access.log.access.1606118347.log
    |   |   `-- access-log-collect.access.log.access.1606119001.log
    |   |-- error.log
    |   |-- nginx.pid
    |   `-- nginx_error.log
    |-- proxy_temp
    |-- scgi_temp
    |-- script
    |   |-- collect-app-log.cron
    |   |-- logs
    |   |   `-- spilt-access-log.log
    |   `-- spilt-access-log.sh
    `-- uwsgi_temp
"

