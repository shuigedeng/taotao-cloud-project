#!/bin/sh
sh promtail.sh
exec /usr/local/openresty/bin/openresty -g "daemon off;"
