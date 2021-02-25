## kvm 及 libvirt

    第 52 天 【kvm 及 libvirt（01）】

### 回顾
---
- kvm：qemu-kvm
    + 显示选项：
        * 非图形界面：
            - `-nographic`
            - `-curses`
        * 图形界面：
            - `-sdl`
            - `spice`
            - `-vnc`
        * `-vga`
        * `-monitor stdio`
    + 网络选项：
        * `-net nic`：定义 net frontend
        * `-net tap`：定义 net backend
            - `script=`：启动脚本；
            - `downscript=`：停止脚本；

        * 虚拟化网络：
            - 隔离模型：
                + 激活tap，并将其加入到指定的bridge；
            - 路由模型：
                + 激活tap， 并将其加入到指定的bridge；
                + 额外：给虚拟的bridge添加地址，打开核心转发；
            - NAT模型：
                + 激活tap，并将其加入到指定的bridge；
                + 额外：打开核心转发，并添加nat规则；
            - 桥接模型：
                + 激活tap，并将其加入到指定的bridge；

```
补充资料：nat模型网络脚本示例：

    /etc/qemu-natup
        #!/bin/bash
        #
        bridge="isbr"
        net="10.0.0.0/8"
        ifaddr=10.0.10.1

        checkbr() {
            if brctl show | grep -i "^$1"; then
                return 0
            else
                return 1
            fi
        }

        initbr() {
            brctl addbr $bridge
            ip link set $bridge up
            ip addr add $ifaddr dev $bridge
        }

        enable_ip_forward() {
            sysctl -w net.ipv4.ip_forward=1
        }

        setup_nat() {
            checkbr $bridge
            if [ $? -eq 1 ]; then
                initbr
                enable_ip_forward
                iptables -t nat -A POSTROUTING -s $net ! -d $net -j MASQUERADE
            fi
        }

        if [ -n "$1" ]; then
            setup_nat
            ip link set $1 up
            brctl addif $bridge $1
            exit 0
        else
            echo "Error: no interface specified."
            exit 1
        fi
        
    /etc/qemu-natdown
        #!/bin/bash
        #
        bridge="isbr"
        net="10.0.0.0/8"

        remove_rule() {
            iptables -t nat -D POSTROUTING -s $net ! -d $net
        }

        isalone_bridge() {
            if ! brctl show | awk "/^$bridge/{print \$4}" | grep "[^[:space:]]" &> /dev/null; then
                ip link set $bridge down
                brctl delbr $bridge
                remove_rule
            fi
        }

        if [ -n "$1" ];then
            ip link set $1 down
            brctl delif $bridge $1
            isalone_bridge
            exit 0
        else
            echo "Error: no interface specified."
            exit 1
        fi
```

### libvirt
---
- libvirt工具栈：
    + 支持的虚拟化技术：KVM, XEN, VMWARE， Qemu, LXC, OpenVZ; 

- libvirt中的术语：
    + node: 指物理节点
    + hypervisor：
    + domain: vm instances
- 安装：
```
    CentOS 6:
        # yum install libvirt libvirt-client python-virtinst virt-manager

    CentOS 7: 
        # yum install libvirt libvirt-client virt-install virt-manager
```

- libvirt和libvirtd的配置文件：
    + libvirt配置文件：/etc/libvirt/libvirt.conf
    + 守护进程配置文件：/etc/libvirt/libvirtd.conf

- 启动 libvirtd 服务器：
```
    # systemctl start libvirtd 
```

- virt-manager：GUI工具

- 域配置文件：xml格式
```
    <vcpu placement='static'>2</vcpu>
    <features>
    </features>
    <domain>
    </domain>
```

（完）
