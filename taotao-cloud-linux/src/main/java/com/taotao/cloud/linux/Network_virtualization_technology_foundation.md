## 网络虚拟化技术基础

    第 52 天【网络虚拟化技术基础(03)】

### 回顾
---
- 半虚拟化：
    + CPU：Xen
    + I/O：
        * Xen：net， blk
        * KVM：virtio
- 完全虚拟化：HVM
- Memory：
    + shadow page table
    + EPT，RIVI

- 网络虚拟化：
    + 桥接
    + 隔离
    + 路由
    + NAT

- 虚拟化管理工具：
    + <https://www.linux-kvm.org/page/Management_Tools>

### 网络虚拟化技术

- OpenVSwitch：虚拟交换机

- VLAN, VXLAN
    + 什么是VLAN？
        * Virtual LAN；LAN即为广播帧能够到的节点范围，也即能够直接通信的范围；
        * 基于MAC地址实现
        * 基于交换机Port实现
        * 基于IP地址实现
        * 基于用户相信
    + 交换机接口的类型：
        * 访问链接：access link
        * 汇聚链接：trunk link
    + 汇聚方式：
        * IEEE 802.1q
        * ISL：Inter Switch Link
    + 加载 802.1q 模块：
    ```
        # modinfo 8021q
        filename:       /lib/modules/3.10.0-693.el7.x86_64/kernel/net/8021q/8021q.ko.xz
        version:        1.8
        license:        GPL
        alias:          rtnl-link-vlan
        rhelversion:    7.4
        srcversion:     560BE7718270FE95AE220C6
        depends:        mrp,garp
        intree:         Y
        vermagic:       3.10.0-693.el7.x86_64 SMP mod_unload modversions 
        signer:         CentOS Linux kernel signing key
        sig_key:        DA:18:7D:CA:7D:BE:53:AB:05:BD:13:BD:0C:4E:21:F4:22:B6:A4:9C
        sig_hashalgo:   sha256
        # modprobe 8021q
        # lsmod | grep 802
        8021q                  33159  0 
        garp                   14384  1 8021q
        mrp                    18542  1 8021q

        # ls /proc/net/vlan/
        config
    ```
    + `vconfig` 命令：
    ```
        1、CentOS7 vconfig 命令在 epel 源中，安装阿里云 epel源：
            # cd /etc/yum.repos.d/
            # curl -O https://mirrors.aliyun.com/repo/epel-7.repo
              % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                             Dload  Upload   Total   Spent    Left  Speed
            100  1084  100  1084    0     0   3139      0 --:--:-- --:--:-- --:--:--  3142
        2、安装 vconfig：
            # yum install vconfig

            # vconfig --help
            Expecting argc to be 3-5, inclusive.  Was: 2

            Usage: add             [interface-name] [vlan_id]
                   rem             [vlan-name]
                   set_flag        [interface-name] [flag-num]       [0 | 1]
                   set_egress_map  [vlan-name]      [skb_priority]   [vlan_qos]
                   set_ingress_map [vlan-name]      [skb_priority]   [vlan_qos]
                   set_name_type   [name-type]

            * The [interface-name] is the name of the ethernet card that hosts
              the VLAN you are talking about.
            * The vlan_id is the identifier (0-4095) of the VLAN you are operating on.
            * skb_priority is the priority in the socket buffer (sk_buff).
            * vlan_qos is the 3 bit priority in the VLAN header
            * name-type:  VLAN_PLUS_VID (vlan0005), VLAN_PLUS_VID_NO_PAD (vlan5),
                          DEV_PLUS_VID (eth0.0005), DEV_PLUS_VID_NO_PAD (eth0.5)
            * bind-type:  PER_DEVICE  # Allows vlan 5 on eth0 and eth1 to be unique.
                          PER_KERNEL  # Forces vlan 5 to be unique across all devices.
            * FLAGS:  1 REORDER_HDR  When this is set, the VLAN device will move the
                        ethernet header around to make it look exactly like a real
                        ethernet device.  This may help programs such as DHCPd which
                        read the raw ethernet packet and make assumptions about the
                        location of bytes.  If you don't need it, don't turn it on, because
                        there will be at least a small performance degradation.  Default
                        is OFF.
    ```
    + VLAN间路由：
        * 路由器：
            - 访问链接：router 为每一个 VLAN 提供一个接口
            - 汇聚链接（单臂路由）：router 为每一个 VLAN 提供一个虚拟接口
        * 三层交换机
- 虚拟路由器


>   第 52 天【网络名称空间netns用法详解(04)】

- 虚拟化技术：
    + cpu
    + memory
    + i/0

- IaaS：Infrastructure as a Service
- PaaS：Platform as a Service

- Linux 内核：
    + namespace
        * 文件系统隔离；
            - 挂载点隔离；
        * 网络隔离：主要用于实现网络资源的隔离，包括网络设备、IPv4以及IPv6地址、IP路由表、防火墙、/proc/net、/sys/class/net以及套接字等。
        * IPC隔离；
        * 用户和用户组隔离；
        * PID隔离：对名称空间内的PID重新标号，两个不同的名称空间可以使用相同的PID；
        * UTS隔离：Unix Time-sharing System，提供主机名称和域名的隔离；
    + cgroups
        * 用于完成资源配置：用于实现限制被各 namespace 隔离起来的资源，还可以为资源设置权重、计算使用量、完成各种所需的管理任务等；

Linux Network NameSpace：

注意：netns 在内核实现，其控制功能由 iproute 所提供的 netns 这个 OBJECT 来提供；CentOS6.6 提供的 iproute 不具有此 OBJECT，需要依赖于 OpenStack Icehouse 的 [EPEL 源](https://repos.fedorapeople.org/repos/openstack/EOL/openstack-icehouse/epel-6/)来提供；

- 使用 netns
```
    # ip netns help
    Usage: ip netns list
           ip netns add NAME
           ip netns set NAME NETNSID
           ip [-all] netns delete [NAME]
           ip netns identify [PID]
           ip netns pids NAME
           ip [-all] netns exec [NAME] cmd ...
           ip netns monitor
           ip netns list-id

    # ip netns add r1
    # ip netns add r2
    #ip netns list
    r2
    r1
    # ip netns exec r1 ifconfig 
    # ip netns exec r1 ifconfig lo 127.0.0.1/8 up
    # ip netns exec r1 ifconfig
    lo: flags=73<UP,LOOPBACK,RUNNING>  mtu 65536
            inet 127.0.0.1  netmask 255.0.0.0
            inet6 ::1  prefixlen 128  scopeid 0x10<host>
            loop  txqueuelen 1  (Local Loopback)
            RX packets 0  bytes 0 (0.0 B)
            RX errors 0  dropped 0  overruns 0  frame 0
            TX packets 0  bytes 0 (0.0 B)
            TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0

    # brctl addbr br-ex
    # ip link set br-ex up 
    # ip addr del 192.168.2.139/24 dev ens33; ip addr add 192.168.2.139/24 dev br-ex; brctl addif br-ex ens33

    # brctl addbr br-in
    # ip link set br-in up

    # ip link set veth1.1 netns r1
    # ip netns exec r1 ifconfig -a
    lo: flags=73<UP,LOOPBACK,RUNNING>  mtu 65536
            inet 127.0.0.1  netmask 255.0.0.0
            inet6 ::1  prefixlen 128  scopeid 0x10<host>
            loop  txqueuelen 1  (Local Loopback)
            RX packets 0  bytes 0 (0.0 B)
            RX errors 0  dropped 0  overruns 0  frame 0
            TX packets 0  bytes 0 (0.0 B)
            TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0

    veth1.1: flags=4098<BROADCAST,MULTICAST>  mtu 1500
            ether be:05:8f:c0:58:7f  txqueuelen 1000  (Ethernet)
            RX packets 0  bytes 0 (0.0 B)
            RX errors 0  dropped 0  overruns 0  frame 0
            TX packets 0  bytes 0 (0.0 B)
            TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0

    # ip netns exec r1 ip link set veth1.1 name eth0
    # ip netns exec r1 ifconfig -a
    eth0: flags=4098<BROADCAST,MULTICAST>  mtu 1500
            ether be:05:8f:c0:58:7f  txqueuelen 1000  (Ethernet)
            RX packets 0  bytes 0 (0.0 B)
            RX errors 0  dropped 0  overruns 0  frame 0
            TX packets 0  bytes 0 (0.0 B)
            TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0

    lo: flags=73<UP,LOOPBACK,RUNNING>  mtu 65536
            inet 127.0.0.1  netmask 255.0.0.0
            inet6 ::1  prefixlen 128  scopeid 0x10<host>
            loop  txqueuelen 1  (Local Loopback)
            RX packets 0  bytes 0 (0.0 B)
            RX errors 0  dropped 0  overruns 0  frame 0
            TX packets 0  bytes 0 (0.0 B)
            TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0

    # ip link set veth1.2 netns r2
    # ip netns exec r2 ifconfig -a 
    lo: flags=8<LOOPBACK>  mtu 65536
            loop  txqueuelen 1  (Local Loopback)
            RX packets 0  bytes 0 (0.0 B)
            RX errors 0  dropped 0  overruns 0  frame 0
            TX packets 0  bytes 0 (0.0 B)
            TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0

    veth1.2: flags=4098<BROADCAST,MULTICAST>  mtu 1500
            ether 6a:80:d9:e1:d4:b2  txqueuelen 1000  (Ethernet)
            RX packets 0  bytes 0 (0.0 B)
            RX errors 0  dropped 0  overruns 0  frame 0
            TX packets 0  bytes 0 (0.0 B)
            TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0

    # ip netns exec r2 ip link set veth1.2 name eth0

    # ip netns exec r1 ifconfig eth0 10.0.1.1/24 up
    # ip netns exec r2 ifconfig eth0 10.0.1.2/24 up 
    # ip netns exec r1 ping 10.0.1.2
    PING 10.0.1.2 (10.0.1.2) 56(84) bytes of data.
    64 bytes from 10.0.1.2: icmp_seq=1 ttl=64 time=0.057 ms
    64 bytes from 10.0.1.2: icmp_seq=2 ttl=64 time=0.063 ms

```
- 使用虚拟以太网卡
```
    ip link add FRONTEND-NAME type veth peer name BACKEND-NAME

    # vim /etc/sysctl.conf 
    net.ipv4.ip_forward = 1
    # sysctl -p
    net.ipv4.ip_forward = 1
    # ip link add veth1.1 type veth peer name veth1.2
    # ip link show
    1: lo: <LOOPBACK,UP,LOWER_UP> mtu 65536 qdisc noqueue state UNKNOWN mode DEFAULT qlen 1
        link/loopback 00:00:00:00:00:00 brd 00:00:00:00:00:00
    2: ens33: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc pfifo_fast master br-ex state UP mode DEFAULT qlen 1000
        link/ether 00:0c:29:cb:df:c0 brd ff:ff:ff:ff:ff:ff
    21: br-ex: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue state UP mode DEFAULT qlen 1000
        link/ether 00:0c:29:cb:df:c0 brd ff:ff:ff:ff:ff:ff
    22: br-in: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue state UNKNOWN mode DEFAULT qlen 1000
        link/ether 22:6b:fb:fe:b2:9b brd ff:ff:ff:ff:ff:ff
    23: veth1.2@veth1.1: <BROADCAST,MULTICAST,M-DOWN> mtu 1500 qdisc noop state DOWN mode DEFAULT qlen 1000
        link/ether 6a:80:d9:e1:d4:b2 brd ff:ff:ff:ff:ff:ff
    24: veth1.1@veth1.2: <BROADCAST,MULTICAST,M-DOWN> mtu 1500 qdisc noop state DOWN mode DEFAULT qlen 1000
        link/ether be:05:8f:c0:58:7f brd ff:ff:ff:ff:ff:ff
```

- dnsmasq：
```
    - A lightweight DHCP and caching DNS server.

    # dnsmasq --help

    # ip netns exec r1 dnsmasq --dhcp-range 10.0.1.100,10.0.1.120,86400 -i eth0

```

**作业**：实现如下图虚拟机网络架构。  
要求：  
1、创建两个虚拟机；  
2、创建一个内部网桥（br-in）供两台虚拟使用；  
3、创建一个外部网桥（br-ex）并且桥接到外部物理网络；  
4、创建一个网络名称空间（Netns1），开启网络内核转发功能，并为其添加两对儿网卡，一对儿链接内部网桥，一对儿链接外部网桥；  
5、配置虚拟机 IP 地址，要求虚拟机网关为 Netns1 eth0 ，并能够 `ping` 通外部网络。  

注意：网络核心转发功能要在创建网络名称空间之前就要打开，不然创建的网络名称空间将不支持此功能。

<div align=center>
<img src="./images/homework_netns.png" width = "500" height = "500" alt="homework_netns.png" align=center />
</div>

1、创建网桥：
```
    安装 bridge-utils：
        # yum install bridge-utils -y

    创建 br-in 网桥：
        # brctl addbr br-in
        # ip link set br-in up

    创建 br-ex 网桥，并设置桥接网络：
        # brctl addbr br-ex
        # ip link set br-ex up 
        # ip addr del 192.168.2.139/24 dev ens33; ip addr add 192.168.2.139/24 dev br-ex; brctl addif br-ex ens33
```

2、创建网络名称空间：
```
    开启核心路由转发功能：
        # vim /etc/sysctl.conf
        net.ipv4.ip_forward = 1
        # sysctl -p
        net.ipv4.ip_forward = 1

    创建名称空间 Netns1：
        # ip netns add Netns1
        # ip netns list
        Netns1
        # ip netns exec Netns1 ifconfig -a
        lo: flags=8<LOOPBACK>  mtu 65536
                loop  txqueuelen 1  (Local Loopback)
                RX packets 0  bytes 0 (0.0 B)
                RX errors 0  dropped 0  overruns 0  frame 0
                TX packets 0  bytes 0 (0.0 B)
                TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0

    创建两对儿网卡：
        # ip link add veth1.1 type veth peer name veth1.2    // Netns1 链接内部网桥使用
        # ip link set veth1.1 up
        # ip link set veth1.2 up
        # brctl addif br-in veth1.1
        # brctl show
        bridge name     bridge id               STP enabled     interfaces
        br-ex           8000.000c29cbdfc0       no              ens33
        br-in           8000.16cb0c385975       no              veth1.1

        # ip link set veth1.2 netns Netns1 
        # ip netns exec Netns1 ifconfig -a
        lo: flags=8<LOOPBACK>  mtu 65536
                loop  txqueuelen 1  (Local Loopback)
                RX packets 0  bytes 0 (0.0 B)
                RX errors 0  dropped 0  overruns 0  frame 0
                TX packets 0  bytes 0 (0.0 B)
                TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0

        veth1.2: flags=4098<BROADCAST,MULTICAST>  mtu 1500
                ether c2:21:07:e2:74:b5  txqueuelen 1000  (Ethernet)
                RX packets 5  bytes 438 (438.0 B)
                RX errors 0  dropped 0  overruns 0  frame 0
                TX packets 5  bytes 438 (438.0 B)
                TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0

        # ip netns exec Netns1 ip link set veth1.2 name eth0
        # ip netns exec Netns1 ifconfig eth0 10.0.1.254 netmask 255.255.255.0 up
        # ip netns exec Netns1 ifconfig
        eth0: flags=4163<UP,BROADCAST,RUNNING,MULTICAST>  mtu 1500
                inet 10.0.1.254  netmask 255.255.255.0  broadcast 10.0.1.255
                inet6 fe80::c021:7ff:fee2:74b5  prefixlen 64  scopeid 0x20<link>
                ether c2:21:07:e2:74:b5  txqueuelen 1000  (Ethernet)
                RX packets 5  bytes 438 (438.0 B)
                RX errors 0  dropped 0  overruns 0  frame 0
                TX packets 13  bytes 1086 (1.0 KiB)
                TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0

        # ip link add veth2.1 type veth peer name veth2.2    // Netns1 链接外部网桥使用
        # ip link set veth2.1 up
        # ip link set veth2.2 up
        # brctl addif br-ex veth2.1
        # brctl show
        bridge name     bridge id               STP enabled     interfaces
        br-ex           8000.000c29cbdfc0       no              ens33
                                                                veth2.1

        # ip link set veth2.2 netns Netns1
        # ip netns exec Netns1 ip link set veth2.2 name eth1
        # ip netns exec Netns1 ifconfig eth1 192.168.2.140/24 up
        # ip netns exec Netns1 ifconfig
        eth0: flags=4163<UP,BROADCAST,RUNNING,MULTICAST>  mtu 1500
                inet 10.0.1.254  netmask 255.255.255.0  broadcast 10.0.1.255
                inet6 fe80::c021:7ff:fee2:74b5  prefixlen 64  scopeid 0x20<link>
                ether c2:21:07:e2:74:b5  txqueuelen 1000  (Ethernet)
                RX packets 5  bytes 438 (438.0 B)
                RX errors 0  dropped 0  overruns 0  frame 0
                TX packets 13  bytes 1086 (1.0 KiB)
                TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0

        eth1: flags=4163<UP,BROADCAST,RUNNING,MULTICAST>  mtu 1500
                inet 192.168.2.140  netmask 255.255.255.0  broadcast 192.168.2.255
                inet6 fe80::508e:a4ff:fedc:67e7  prefixlen 64  scopeid 0x20<link>
                ether 52:8e:a4:dc:67:e7  txqueuelen 1000  (Ethernet)
                RX packets 5  bytes 438 (438.0 B)
                RX errors 0  dropped 0  overruns 0  frame 0
                TX packets 13  bytes 1086 (1.0 KiB)
                TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0

    Netns1 iptables NAT 设置：
        # ip netns exec Netns1 iptables -t nat -A POSTROUTING -s 10.0.1.0/24 ! -d 10.0.1.0/24 -o eth1 -j MASQUERADE

        默认路由：
            # ip netns exec Netns1 route add default gw 192.168.2.2
```

3、创建两个虚拟机：  
```
    镜像文件：
        # cd /images/kvm/cirros/
        # cp cirros-0.3.4-x86_64-disk.img vm1.qcow2 
        # cp cirros-0.3.4-x86_64-disk.img vm2.qcow2

    安装 qemu-kvm：
        # yum install qemu-kvm -y

    加载 kvm 内核：
        # modprobe kvm
        # modprobe kvm-intel

    qemu-kvm 网卡脚本：
        # vim /etc/qemu-ifup
        #!/bin/bash
        #
        bridge=br-in

        if [ -n "$1" ];then
            ip link set $1 up
            sleep 1
            brctl addif $bridge $1
            [ $? -eq 0 ] && exit 0 || exit 1
        else
            echo "Error: no interface specified."
            exit 1
        fi

        # vim /etc/qemu-ifdown
        #!/bin/bash
        #
        bridge=br-in

        if [ -n "$1" ];then
            brctl delif $bridge $1
            ip linke set $ down
            exit 0
        else
            echo "Error: no interface specified."
            exit 1
        fi

    创建 vm1：
        # qemu-kvm -m 128 -smp 1 -name "vm1" -drive file=/images/kvm/cirros/vm1.qcow2,if=virtio,media=disk,format=qcow2,cache=writeback -net nic,model=virtio,macaddr=52:54:00:aa:bb:cc -net tap,ifname=vm1.0,script=/etc/qemu-ifup,downscript=/etc/qemu-ifdown -daemonize
        VNC server running on `::1:5900'

    创建 vm2：
        # qemu-kvm -m 128 -smp 1 -name "vm2" -drive file=/images/kvm/cirros/vm2.qcow2,if=virtio,media=disk,format=qcow2,cache=writeback -net nic,model=virtio,macaddr=52:54:00:aa:bb:cc -net tap,ifname=vm2.0,script=/etc/qemu-ifup,downscript=/etc/qemu-ifdown -daemonize   
        VNC server running on `::1:5901'

    给虚拟机配置 ip 地址：
        vm1：
            # ifconfig eth0 10.0.1.1 netmask 255.255.255.0 up
            # route add default gw 10.0.1.254
            # ping 10.0.1.254
            # ping 192.168.2.140
            # ping 192.168.2.2
            # ping 8.8.8.8

        vm2：
            # ifconfig eth0 10.0.1.2 netmask 255.255.255.0 up
            # route add default gw 10.0.1.254
            # ping 10.0.1.254
            # ping 192.168.2.140
            # ping 192.168.2.2
            # ping 8.8.8.8
```

（完）