########################################
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
#  "exec-opts": ["native.cgroupdriver=systemd"]

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

docker run -d --privileged -p 28080:80 -p 2443:443  \
-v /root/rancher/rancher:/var/lib/rancher   \
-v /root/rancher/auditlog:/var/log/auditlog  \
--name rancher2 rancher/rancher

https://127.0.0.1:2443


### docker rm `docker ps -a -q`
###  docker rmi $(docker images -q)

swapoff -a
kubeadm reset
systemctl daemon-reload
systemctl restart kubelet
iptables -F && iptables -t nat -F && iptables -t mangle -F && iptables -X
