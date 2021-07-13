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
  "experimental": false
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
