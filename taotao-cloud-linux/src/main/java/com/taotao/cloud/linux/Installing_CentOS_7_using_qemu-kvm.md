## Installing CentOS 7 using qemu-kvm

- 系统环境：
```
    ~]# lsb_release -a
    LSB Version:    :base-4.0-amd64:base-4.0-noarch:core-4.0-amd64:core-4.0-noarch
    Distributor ID: CentOS
    Description:    CentOS release 6.7 (Final)
    Release:        6.7
    Codename:       Final

    ~]# qemu-kvm --version
    QEMU PC emulator version 0.12.1 (qemu-kvm-0.12.1.2-2.503.el6_9.4), Copyright (c) 2003-2008 Fabrice Bellard


```

- 加载 kvm 模块及安装 qemu-kvm
```
    # modprobe kvm
    # modprobe kvm-intel
    # lsmod | grep --color=auto kvm
    kvm_intel              55624  0 
    kvm                   341551  1 kvm_intel

    # yum install qemu-kvm -y
```

- 创建磁盘
```
    # mkdir images/centos7 -pv
    # qemu-img create -f qcow2 -o size=20G,preallocation=metadata /root/images/centos7/centos7.img    
    Formatting 'images/centos7/centos7.img', fmt=qcow2 size=21474836480 encryption=off cluster_size=65536 preallocation='metadata'    
```

- 创建虚拟机
```
    # qemu-kvm -name "CentOS7" -smp 2 -m 2048 -drive file=/root/images/centos7/centos7.img,if=virtio,media=disk,cache=writeback,format=qcow2 -drive file=/var/www/html/pub/iso/centos7/CentOS-7-x86_64-Everything-1708.iso,media=cdrom -net nic,model=virtio,macaddr=52:54:00:12:34:57 -net tap,ifname=centos7.0,script=/etc/qemu-ifup,downscript=/etc/qemu-ifdown -boot order=dc,once=d -usbdevice tablet
```

- 后续启动命令
```
    # qemu-kvm -name "CentOS7" -smp 2 -m 2048 -drive file=/root/images/centos7/centos7.img,if=virtio,media=disk,cache=writeback,format=qcow2 -net nic,model=virtio,macaddr=52:54:00:12:34:57 -net tap,ifname=centos7.0,script=/etc/qemu-ifup,downscript=/etc/qemu-ifdown  -usbdevice tablet -daemonize
```

