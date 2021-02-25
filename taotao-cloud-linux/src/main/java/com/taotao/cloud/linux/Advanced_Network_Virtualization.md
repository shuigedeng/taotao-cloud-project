## 网络虚拟化进阶

    第 53 天【网络虚拟化技术进阶(01)】

### 网络虚拟化
- 复杂的虚拟化网络：
    + netns
    + OpenVSwitch

- OVS：基于C语言研发；
    + 特性：
        * 支持802.1q协议, trunk, access
        * 支持NIC bonding
        * NetFlow, sFlow
        * QoS配置及策略
        * GRE：通用路由封装
        * VxLAN
        * 支持OpenFlow
        * 支持基于Linux内核实现高性能转发

    + 组成部分：
        * ovs-vswitchd：OVS daemon，实现数据报文交换功能，和Linux内核兼容模块一同实现了基于流的交换技术；
        * ovsdb-server：轻量级数据库服务，主要保存了整个OVS的配置信息，例如接口、交换和VLAN等等；ovs-vswitched的交换功能基于此库实现；
        * ovs-dpctl：控制转发规则；
        * ovs-vsctl：用于获取或更改ovs-vswitched的配置信息，其修改操作会保存至ovsdb-server中；
        * ovs-appctl
        * ovsdbmonitor：gui 工具，用来显示 ovsdb 中的信息；
        * ovs-controller
        * ovs-ofctl
        * ovs-pki

    + ovs-vsctl命令的使用：
        * show：ovsdb配置内容查看；
        * add-br NAME：添加桥设备；
        * list-br：显示所有已定义的桥；
        * del-br BRIDGE：删除桥设备；
        * add-port BRIDGE PORT：将PORT添加至指定的BRIDGE；
        * list-ports BRIDGE：显示指定BRIDGE上已经添加的所有PORT；
        * del-port [BRIDGE] PORT：从指定BRIDGE移除指定的PORT；

- 设置 OpenStack yum 源：
```
    # vim /etc/yum.repos.d/openstack.repo
    [openstatck]
    name=OpenStack IceHouse
    baseurl=https://repos.fedorapeople.org/repos/openstack/EOL/openstack-icehouse/epel-7/
    gpgcheck=0
```

- 安装 openvswitch：
```
    # yum install openvswitch -y
```

- 启动 openvswitch 服务：
```
    # systemctl start openvswitch
```

- 虚拟机网卡启动脚本：
```
    # vim /etc/if-up
    #!/bin/bash
    #
    bridge=br-in

    if [ -n "$1" ]; then
        ip link set $1 up
        sleep 1
        ovs-vsctl add-port $bridge $1
        [ $? -eq 0 ] && exit 0 || exit 1
    else
        echo "Error: no port specified."
        exit 2
    fi
    # chmod +x /etc/if-up

    # vim /etc/if-down
    #!/bin/bash
    #
    bridge=br-in

    if [ -n "$1" ]; then
        ovs-vsctl del-port $bridge $1
        sleep 1
        ip link set $1 down
        [ $? -eq 0 ] && exit 0 || exit 1
    else 
        echo "Error: no port specified."
        exit 2
    fi
    # chmod +x /etc/if-down
```

- 给端口设置 VLAN tag：
```
    # ovs-vsctl set port vif0.0 tag=10
```


>   第 53 天【网络虚拟化技术进阶(02)】


- GRE：Generic Routing Encapsulation，通用路由封装；是一种隧道技术；
```
    # ovs-vsctl add-port br-in gre0
    # ovs-vsctl set interface gre0 type=gre options:remote_ip=192.168.20.2

    # ovs-vsctl add-port br-in gre0 -- set interface gre0 type=gre options:remote_ip=192.168.20.1
```

- VxLAN
```
    # ovs-vsctl add-port br-in vx0 -- set interface vx0 type=vxlan options:remote_ip=192.168.20.2

    # ovs-vsctl add-port br-in vx0 -- set interface vx0 type=vxlan options:remote_ip=192.168.20.1
```
