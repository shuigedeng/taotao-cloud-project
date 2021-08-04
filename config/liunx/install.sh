systemctl status firewalld
systemctl stop firewalld
systemctl disable firewalld

vi /etc/sysconfig/network-scripts/ifcfg-eth0
TYPE=Ethernet
PROXY_METHOD=none
BROWSER_ONLY=no
BOOTPROTO=static
DEFROUTE=yes
IPV4_FAILURE_FATAL=no
IPV6INIT=yes
IPV6_AUTOCONF=yes
IPV6_DEFROUTE=yes
IPV6_FAILURE_FATAL=no
NAME=eth0
UUID=eb136374-2762-482e-906c-ec20b19cea1e
DEVICE=eth0
ONBOOT=yes
IPADDR=172.16.3.151
NETMASK=255.255.255.0
GATEWAY=172.16.3.254
DNS1=172.16.60.200
DNS2=61.128.128.68

dnf install vim


