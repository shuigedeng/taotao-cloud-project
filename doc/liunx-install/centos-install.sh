cd /etc/yum.repos.d/
sed -i 's/mirrorlist/#mirrorlist/g' /etc/yum.repos.d/CentOS-*
sed -i 's|#baseurl=http://mirror.centos.org|baseurl=http://vault.centos.org|g' /etc/yum.repos.d/CentOS-*

dnf update

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
IPADDR=192.168.10.220
NETMASK=255.255.255.0
GATEWAY=172.16.6.254
DNS1=172.16.60.200
DNS2=61.128.128.68

# 安装系统性能分析工具及其他
dnf install gcc make autoconf vim sysstat net-tools git wget vim langpacks-en glibc-all-langpacks -y

# 禁用selinux
sed -i '/SELINUX/{s/permissive/disabled/}' /etc/selinux/config
sed -i 's/enforcing/disabled/g' /etc/selinux/config; setenforce 0

# 设置最大打开文件数
if ! grep "* soft nofile 65535" /etc/security/limits.conf &>/dev/null; then
    cat >> /etc/security/limits.conf << EOF
    * soft nofile 65535
    * hard nofile 65535
EOF
fi

# 系统内核优化
cat >> /etc/sysctl.conf << EOF
net.ipv4.tcp_syncookies = 1
net.ipv4.tcp_max_tw_buckets = 20480
net.ipv4.tcp_max_syn_backlog = 20480
net.core.netdev_max_backlog = 262144
net.ipv4.tcp_fin_timeout = 20
EOF

# 减少SWAP使用
echo "0" > /proc/sys/vm/swappiness

