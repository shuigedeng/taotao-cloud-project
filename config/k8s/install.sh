########################################
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

# 2.安装k8s

vim /etc/yum.repos.d/kubernetes.repo
[kubernetes]
name=Kubernetes
baseurl=https://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-x86_64/
enabled=1
gpgcheck=1
repo_gpgcheck=1
gpgkey=https://mirrors.aliyun.com/kubernetes/yum/doc/yum-key.gpg https://mirrors.aliyun.com/kubernetes/yum/doc/rpm-package-key.gpg

yum install -y kubectl kubelet kubeadm
sudo systemctl enable kubelet
sudo systemctl start kubelet

kubeadm version
kubectl version --client
kubelet --version

kubeadm init  \
--apiserver-advertise-address=0.0.0.0  \
--apiserver-cert-extra-sans=127.0.0.1  \
--image-repository=registry.aliyuncs.com/google_containers  \
--ignore-preflight-errors=all \
--kubernetes-version=v1.22.1  \
--service-cidr=10.1.0.0/16  \
--pod-network-cidr=10.222.0.0/16

mkdir -p $HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config

kubectl get node
kubectl get pod --all-namespaces

kubectl apply -f https://docs.projectcalico.org/manifests/calico.yaml

# error execution phase upload-config/kubelet: Error writing Crisocket information for the control-...
swapoff -a
kubeadm reset
systemctl daemon-reload
systemctl restart kubelet
iptables -F && iptables -t nat -F && iptables -t mangle -F && iptables -X


