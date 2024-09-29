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

kubeadm reset
kubeadm init  \
--apiserver-advertise-address=0.0.0.0  \
--apiserver-cert-extra-sans=127.0.0.1  \
--image-repository=registry.aliyuncs.com/google_containers  \
--ignore-preflight-errors=all \
--kubernetes-version=v1.24.1  \
--service-cidr=10.1.0.0/16  \
--pod-network-cidr=10.222.0.0/16

mkdir -p $HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config

kubectl apply -f https://docs.projectcalico.org/manifests/calico.yaml

# 当创建单机版的 k8s 时，这个时候 master 节点是默认不允许调度 pod   将master标记为可调度
kubectl taint nodes --all node-role.kubernetes.io/master-

kubectl get node
kubectl get pod --all-namespaces
kubectl describe pod coredns-7f6cbbb7b8-h55vx
kubectl describe pod coredns-7f6cbbb7b8-6rc9d  -n kube-system

#校验集群
kubectl create deployment nginx --image=nginx
kubectl expose deployment nginx --port=80 --type=NodePort
kubectl get pod,svc

# 部署Dashboard
https://github.com/kubernetes/dashboard/blob/master/aio/deploy/recommended.yaml

# 编辑文件
# 暴露端口的修改如下：
kind: Service
apiVersion: v1
metadata:
  labels:
    k8s-app: kubernetes-dashboard
  name: kubernetes-dashboard
  namespace: kubernetes-dashboard
spec:
  #添加
  type: NodePort
  ports:
    - port: 443
      targetPort: 8443
      # 添加
      nodePort: 30001
  selector:
    k8s-app: kubernetes-dashboard

docker pull kubernetesui/dashboard:v2.3.1
docker pull kubernetesui/metrics-scraper:v1.0.6

kubectl apply -f recommended.yaml

kubectl get pod --all-namespaces
kubectl get pods -n kube-system -o wide
kubectl get services -n kube-system
netstat -ntlp|grep 30001

kubectl create serviceaccount  dashboard-admin -n kube-system
kubectl create clusterrolebinding  dashboard-admin --clusterrole=cluster-admin --serviceaccount=kube-system:dashboard-admin
kubectl describe secrets -n kube-system $(kubectl -n kube-system get secret | awk '/dashboard-admin/{print $1}')

```
Name:         dashboard-admin-token-4xpzq
Namespace:    kube-system
Labels:       <none>
Annotations:  kubernetes.io/service-account.name: dashboard-admin
              kubernetes.io/service-account.uid: 3d66d156-bdae-480b-bcdf-0915b2802877

Type:  kubernetes.io/service-account-token

Data
====
ca.crt:     1099 bytes
namespace:  11 bytes
token:      eyJhbGciOiJSUzI1NiIsImtpZCI6IncycWZ1aTk1RnBPRHBXeXFXNlRzRWFja2lKUnpNMkE4MWNHMERqZi1UWEkifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJrdWJlLXN5c3RlbSIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJkYXNoYm9hcmQtYWRtaW4tdG9rZW4tNHhwenEiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC5uYW1lIjoiZGFzaGJvYXJkLWFkbWluIiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQudWlkIjoiM2Q2NmQxNTYtYmRhZS00ODBiLWJjZGYtMDkxNWIyODAyODc3Iiwic3ViIjoic3lzdGVtOnNlcnZpY2VhY2NvdW50Omt1YmUtc3lzdGVtOmRhc2hib2FyZC1hZG1pbiJ9.bKM-c4wKlxFwTcEREoxgHnabCCRAVHw_0T02KaM_-xfORY4MtMEudiMavPv9n2jPFzo2UYSppmYM5R4Q_HuPLDf6MVG500VBknzVW2UftTk_Rd-gpNTXHDtbReJYSeR-MPWpWbZ5OfNGp5puAOxqcPBhYNvo2qzlOR5Qsp9SgNONgA3wHr5bguFlC6eiw-mpoqiLZiTbjpYK8o5q6STx23v_TBcgImJ0P6FK2yxmbvC0OpS-QGxnfCOvYVZ1DkPf0MILmBr22JtcPN1BoIbtQeLOi00sLx0Wn01DpEyMygFcU96au_pMD2hqn05Rjwrp5juPxUBLAQ3ri8LLF4uvZQ
```

# error execution phase upload-config/kubelet: Error writing Crisocket information for the control-...
swapoff -a
kubeadm reset
systemctl daemon-reload
systemctl restart kubelet
iptables -F && iptables -t nat -F && iptables -t mangle -F && iptables -X


docker pull registry.aliyuncs.com/google_containers/coredns:1.8.0
docker tag registry.aliyuncs.com/google_containers/coredns:1.8.0 registry.aliyuncs.com/google_containers/coredns:v1.8.4
docker rmi registry.aliyuncs.com/google_containers/coredns:1.8.0
