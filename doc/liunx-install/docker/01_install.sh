########################################
# https://docs.docker.com/engine/install/centos/
# https://www.cnblogs.com/myitnews/p/11509546.html

# 1. 安装docker
#关闭防火墙
systemctl stop firewalld
systemctl disable firewalld
# 关闭防火墙
iptables -F
iptables -X
systemctl stop firewalld
systemctl disable firewalld
systemctl status firewalld

#永久关闭交换区
sudo sed -i 's/.*swap.*/#&/' /etc/fstab
# 关闭 swap
# https://www.jianshu.com/p/6dae5c2c4dab
# (x.x.1)删除 swap 区所有内容
swapoff -a
# (x.x.2)删除 swap 挂载，系统下次启动不会挂载 swap
# 注释文件/etc/fstab中的swap行
nano /etc/fstab
#/dev/mapper/cl-swap     swap                    swap    defaults        0 0

# 永久关闭selinux
sudo sed -i "s/^SELINUX=enforcing/SELINUX=disabled/g" /etc/selinux/config
# 关闭selinux
setenforce 0
sed -i 's/^SELINUX=enforcing$/SELINUX=permissive/' /etc/selinux/config
# 查看selinux状态
getenforce

sudo yum remove docker docker-client  docker-client-latest    \
docker-common  docker-latest  docker-latest-logrotate  \
docker-logrotate docker-engine

sudo yum install -y yum-utils

sudo yum-config-manager     --add-repo     https://download.docker.com/linux/centos/docker-ce.repo

sudo yum install docker-ce docker-ce-cli containerd.io

systemctl status docker
systemctl enable docker
systemctl start docker

sudo mkdir -p /etc/docker

sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://sef9c1bz.mirror.aliyuncs.com"],
  "dns": ["8.8.8.8", "8.8.4.4"],
  "debug": true,
  "experimental": false,
  "exec-opts": ["native.cgroupdriver=systemd"]

#    "max-concurrent-downloads": 3,
#    "max-concurrent-uploads": 5,
#    "registry-mirrors": ["https://7bezldxe.mirror.aliyuncs.com/","https://IP:PORT/"],
#    "storage-driver": "overlay2",
#    "storage-opts": ["overlay2.override_kernel_check=true"],
#    "log-driver": "json-file",
#    "log-opts": {
#        "max-size": "100m",
#        "max-file": "3"
#    }
}
EOF

systemctl daemon-reload

systemctl restart docker

### docker rm `docker ps -a -q`
###  docker rmi $(docker images -q)
