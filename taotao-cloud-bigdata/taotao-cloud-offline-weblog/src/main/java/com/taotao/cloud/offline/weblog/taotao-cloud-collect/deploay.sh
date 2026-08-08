#/bin/bash

# filename: deploy.sh
# date: 2020-11-23

mkdir -pv /opt/taotao-cloud-collect/{conf,vhost,lua,logs}

cp /usr/local/openresty/nginx/conf/mime.type /opt/taotao-cloud-collect/conf
cp /usr/local/openresty/nginx/conf/nginx.conf /opt/access-log-collect/conf

/usr/local/openresty/nginx/sbin/nginx -p /opt/app/taotao-cloud-collect -c conf/nginx.conf -t
/usr/local/openresty/nginx/sbin/nginx -p /opt/app/taotao-cloud-collect -c conf/nginx.conf
/usr/local/openresty/nginx/sbin/nginx -p /opt/app/taotao-cloud-collect -c conf/nginx.conf -s reload
