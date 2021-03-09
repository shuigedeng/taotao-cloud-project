#!/bin/sh

#/usr/sbin/keepalived -n -l -D -f /etc/keepalived/keepalived.conf --dont-fork --log-console &
/usr/sbin/keepalived -D -f /etc/keepalived/keepalived.conf


nginx -g "daemon off;"
