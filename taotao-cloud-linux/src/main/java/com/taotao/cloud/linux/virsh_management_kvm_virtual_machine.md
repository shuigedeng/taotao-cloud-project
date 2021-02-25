## 使用virsh管理kvm虚拟机
    
    第 52 天 【使用virsh管理kvm虚拟机(02)】

- Hypervisor的访问路径：
    + 本地URL：
        * driver[+transport]:///[path][?extral-param]
            - driver: 驱动名称，例如qemu, xen, lxc
            - transport：传输方式

        * kvm使用qemu驱动，使用格式qemu:///system, 例如qemu:///system

    + 远程URL：
        * driver[+transport]://[user@][host][:port]/[path][?extral-param]

        * 例如：qemu://172.16.100.6/system
            - qemu+ssh://root@172.16.100.6/system
            - qemu+tcp://172.16.100.6/system

- 工具的使用：
    + (1) CLI: virt-install, virsh
    + (2) virt-manager

```
补充资料：virt-install使用文档

    2.5.3.2 使用virt-install创建虚拟机并安装GuestOS

    virt-install是一个命令行工具，它能够为KVM、Xen或其它支持libvirt API的hypervisor创建虚拟机并完成GuestOS安装；此外，它能够基于串行控制台、VNC或SDL支持文本或图形安装界面。安装过程可以使用本地的安装介质如CDROM，也可以通过网络方式如NFS、HTTP或FTP服务实现。对于通过网络安装的方式，virt-install可以自动加载必要的文件以启动安装过程而无须额外提供引导工具。当然，virt-install也支持PXE方式的安装过程，也能够直接使用现有的磁盘映像直接启动安装过程。

    virt-install命令有许多选项，这些选项大体可分为下面几大类，同时对每类中的常用选项也做出简单说明。
    ◇   一般选项：指定虚拟机的名称、内存大小、VCPU个数及特性等；
           -n NAME, --name=NAME：虚拟机名称，需全局惟一；
           -r MEMORY, --ram=MEMORY：虚拟机内在大小，单位为MB；
           --vcpus=VCPUS[,maxvcpus=MAX][,sockets=#][,cores=#][,threads=#]：VCPU个数及相关配置；
           --cpu=CPU：CPU模式及特性，如coreduo等；可以使用qemu-kvm -cpu ?来获取支持的CPU模式；
    ◇   安装方法：指定安装方法、GuestOS类型等；
           -c CDROM, --cdrom=CDROM：光盘安装介质；
           -l LOCATION, --location=LOCATION：安装源URL，支持FTP、HTTP及NFS等，如ftp://172.16.0.1/pub；
           --pxe：基于PXE完成安装；
           --livecd: 把光盘当作LiveCD；
           --os-type=DISTRO_TYPE：操作系统类型，如linux、unix或windows等；
           --os-variant=DISTRO_VARIANT：某类型操作系统的变体，如rhel5、fedora8等；
           -x EXTRA, --extra-args=EXTRA：根据--location指定的方式安装GuestOS时，用于传递给内核的额外选项，例如指定kickstart文件的位置，--extra-args "ks=http://172.16.0.1/class.cfg"
           --boot=BOOTOPTS：指定安装过程完成后的配置选项，如指定引导设备次序、使用指定的而非安装的kernel/initrd来引导系统启动等 ；例如：
           --boot  cdrom,hd,network：指定引导次序；
           --boot kernel=KERNEL,initrd=INITRD,kernel_args=”console=/dev/ttyS0”：指定启动系统的内核及initrd文件；
    ◇   存储配置：指定存储类型、位置及属性等；
           --disk=DISKOPTS：指定存储设备及其属性；格式为--disk /some/storage/path,opt1=val1，opt2=val2等；常用的选项有：
               device：设备类型，如cdrom、disk或floppy等，默认为disk；
               bus：磁盘总线类型，其值可以为ide、scsi、usb、virtio或xen；
               perms：访问权限，如rw、ro或sh（共享的可读写），默认为rw；
               size：新建磁盘映像的大小，单位为GB；
               cache：缓存模型，其值有none、writethrouth（缓存读）及writeback（缓存读写）；
               format：磁盘映像格式，如raw、qcow2、vmdk等；
               sparse：磁盘映像使用稀疏格式，即不立即分配指定大小的空间；
            --nodisks：不使用本地磁盘，在LiveCD模式中常用；
    ◇   网络配置：指定网络接口的网络类型及接口属性如MAC地址、驱动模式等；
           -w NETWORK, --network=NETWORK,opt1=val1,opt2=val2：将虚拟机连入宿主机的网络中，其中NETWORK可以为：
               bridge=BRIDGE：连接至名为“BRIDEG”的桥设备；
               network=NAME：连接至名为“NAME”的网络；
                其它常用的选项还有：
                   model：GuestOS中看到的网络设备型号，如e1000、rtl8139或virtio等；
                   mac：固定的MAC地址；省略此选项时将使用随机地址，但无论何种方式，对于KVM来说，其前三段必须为52:54:00；
            --nonetworks：虚拟机不使用网络功能；
    ◇   图形配置：定义虚拟机显示功能相关的配置，如VNC相关配置；
           --graphics TYPE,opt1=val1,opt2=val2：指定图形显示相关的配置，此选项不会配置任何显示硬件（如显卡），而是仅指定虚拟机启动后对其进行访问的接口；
               TYPE：指定显示类型，可以为vnc、sdl、spice或none等，默认为vnc；
               port：TYPE为vnc或spice时其监听的端口；
               listen：TYPE为vnc或spice时所监听的IP地址，默认为127.0.0.1，可以通过修改/etc/libvirt/qemu.conf定义新的默认值；
               password：TYPE为vnc或spice时，为远程访问监听的服务进指定认证密码；
           --noautoconsole：禁止自动连接至虚拟机的控制台；
    ◇   设备选项：指定文本控制台、声音设备、串行接口、并行接口、显示接口等；
           --serial=CHAROPTS：附加一个串行设备至当前虚拟机，根据设备类型的不同，可以使用不同的选项，格式为“--serial type,opt1=val1,opt2=val2,...”，例如：
           --serial pty：创建伪终端；
           --serial dev,path=HOSTPATH：附加主机设备至此虚拟机；
           --video=VIDEO：指定显卡设备模型，可用取值为cirrus、vga、qxl或vmvga；

    ◇   虚拟化平台：虚拟化模型（hvm或paravirt）、模拟的CPU平台类型、模拟的主机类型、hypervisor类型（如kvm、xen或qemu等）以及当前虚拟机的UUID等；
           -v, --hvm：当物理机同时支持完全虚拟化和半虚拟化时，指定使用完全虚拟化；
           -p, --paravirt：指定使用半虚拟化；
           --virt-type：使用的hypervisor，如kvm、qemu、xen等；所有可用值可以使用’virsh capabilities’命令获取；
    ◇   其它：
           --autostart：指定虚拟机是否在物理启动后自动启动；
           --print-xml：如果虚拟机不需要安装过程(--import、--boot)，则显示生成的XML而不是创建此虚拟机；默认情况下，此选项仍会创建磁盘映像；
           --force：禁止命令进入交互式模式，如果有需要回答yes或no选项，则自动回答为yes；
           --dry-run：执行创建虚拟机的整个过程，但不真正创建虚拟机、改变主机上的设备配置信息及将其创建的需求通知给libvirt；
           -d, --debug：显示debug信息；

    尽管virt-install命令有着类似上述的众多选项，但实际使用中，其必须提供的选项仅包括--name、--ram、--disk（也可是--nodisks）及安装过程相关的选项。此外，有时还需要使用括--connect=CONNCT选项来指定连接至一个非默认的hypervisor。

    使用示例：

        (1) # virt-install -n "centos6" -r 512 --vcpus=2 -l http://172.16.0.1/cobbler/ks_mirror/CentOS-6.6-x86_64/ -x "ks=http://172.16.0.1/centos6.x86_64.cfg" --disk path=/images/kvm/centos6.img,size=120,sparse --force -w bridge=br100,model=virtio

        (2) 下面这个示例创建一个名为rhel5的虚拟机，其hypervisor为KVM，内存大小为512MB，磁盘为8G的映像文件/var/lib/libvirt/images/rhel5.8.img，通过boot.iso光盘镜像来引导启动安装过程。

        # virt-install \
           --connect qemu:///system \
           --virt-type kvm \
           --name rhel5 \
           --ram 512 \
           --disk path=/var/lib/libvirt/images/rhel5.img,size=8 \
           --graphics vnc \
           --cdrom /tmp/boot.iso \
           --os-variant rhel5

        (3) 下面的示例将创建一个名为rhel6的虚拟机，其有两个虚拟CPU，安装方法为FTP，并指定了ks文件的位置，磁盘映像文件为稀疏格式，连接至物理主机上的名为brnet0的桥接网络：

        # virt-install \
            --connect qemu:///system \
            --virt-type kvm \
            --name rhel6 \
            --ram 1024 \
            --vcpus 2 \
            --network bridge=brnet0 \
            --disk path=/VMs/images/rhel6.img,size=120,sparse \
            --location ftp://172.16.0.1/rhel6/dvd \
            --extra_args “ks=http://172.16.0.1/rhel6.cfg” \
            --os-variant rhel6 \
            --force 

        (4) 下面的示例将创建一个名为rhel5.8的虚拟机，磁盘映像文件为稀疏模式的格式为qcow2且总线类型为virtio，安装过程不启动图形界面（--nographics），但会启动一个串行终端将安装过程以字符形式显示在当前文本模式下，虚拟机显卡类型为cirrus：

        # virt-install \
        --connect qemu:///system \
        --virt-type kvm \ 
        --name rhel5.8 \ 
        --vcpus 2,maxvcpus=4 \
        --ram 512 \ 
        --disk path=/VMs/images/rhel5.8.img,size=120,format=qcow2,bus=virtio,sparse \ 
        --network bridge=brnet0,model=virtio
        --nographics \
        --location ftp://172.16.0.1/pub \ 
        --extra-args "ks=http://172.16.0.1/class.cfg  console=ttyS0  serial" \
        --os-variant rhel5 \
        --force  \
        --video=cirrus

        (5) 下面的示例则利用已经存在的磁盘映像文件（已经有安装好的系统）创建一个名为rhel5.8的虚拟机：

        # virt-install \
            --name rhel5.8
            --ram 512
            --disk /VMs/rhel5.8.img
            --import

        (6) 创建一个 pxe 启动的 centos6.7 虚拟机：
        # virt-install \
            -n centos6.7 \
            -r 512 \
            --vcpus=2,maxvcpus=4 \
            --pxe \
            --disk path=/images/centos/centos6.7.qcow2,size=120G,format=qcow2,bus=virtio,sparse=yes \
            --network bridge=br0,model=virtio \
            --force \
            --graphics vnc

        (7) 导入一个虚拟机：
        # virt-install -n cirros -r 128 --vcpus=1,maxvcpus=2 --disk path=/images/kvm/cirros/cirros-0.3.4-x86_64-disk.img --network bridge=br0,model=virtio --import --serial=pty --console=pty --nographics

        ^]

        注意：每个虚拟机创建后，其配置信息保存在/etc/libvirt/qemu目录中，文件名与虚拟机相同，格式为XML。

virt-install：创建虚拟机，并安装OS；也可创建虚拟机并导入Image文件；

virsh的几个常用命令：
    help：查看 virsh 都有哪些命令可用；
    根据xml文件创建：
        create：创建并启动
        define：创建但不启动
    关闭domain：
        destory
        shutdown
        reboot
    删除domain：
        undefine
    链接至console：
        console
    列出:
        list
    附加或拆除disk：
        attach-disk
        detach-disk
    附加或拆除网卡:
        attach-interface
        detach-interface
    保存状态至磁盘文件或从磁盘文件恢复：
        save
        restore
    暂停于内存或继续运行：
        suspend
        resume  

    list, create, domstate, dominfo, autostart, dommemstat, setmem, vcpuinfo, vcpupin, setvcpus, vncdisplay
    destroy, shutdown
    reset, reboot
    save, restore
    migrate
    console

    Host 和 Hypervisor：
        sysinfo, uri, connect

    网络接口：
        iface-list, iface-bridge

    虚拟网络：
        net-list
```

- `virsh attach-disk` 命令：附加磁盘设备
    ```
        # qemu-img create -f qcow2 -o size=2G,preallocation=metadata /images/kvm/cirros/second.qcow2
        Formatting '/images/kvm/cirros/second.qcow2', fmt=qcow2 size=2147483648 encryption=off cluster_size=65536 preallocation='metadata' lazy_refcounts=off 

        # qemu-img info /images/kvm/cirros/second.qcow2 
        image: /images/kvm/cirros/second.qcow2
        file format: qcow2
        virtual size: 2.0G (2147483648 bytes)
        disk size: 708K
        cluster_size: 65536
        Format specific information:
            compat: 1.1
            lazy refcounts: false

        # virsh attach-disk 7 /images/kvm/cirros/second.qcow2 vda --targetbus virtio     
        Disk attached successfully

        # virsh console 7
        Connected to domain cirros
        Escape character is ^]

        # 
        # 
        # fdisk -l

        Disk /dev/sda: 41 MB, 41126400 bytes
        255 heads, 63 sectors/track, 5 cylinders, total 80325 sectors
        Units = sectors of 1 * 512 = 512 bytes
        Sector size (logical/physical): 512 bytes / 512 bytes
        I/O size (minimum/optimal): 512 bytes / 512 bytes
        Disk identifier: 0x00000000

           Device Boot      Start         End      Blocks   Id  System
        /dev/sda1   *       16065       80324       32130   83  Linux

        Disk /dev/vda: 2148 MB, 2148073472 bytes
        16 heads, 63 sectors/track, 4162 cylinders, total 4195456 sectors
        Units = sectors of 1 * 512 = 512 bytes
        Sector size (logical/physical): 512 bytes / 512 bytes
        I/O size (minimum/optimal): 512 bytes / 512 bytes
        Disk identifier: 0x00000000

        Disk /dev/vda doesn't contain a valid partition table

    ```
- `virsh detach-disk`命令：分离磁盘设备
    ```
        # virsh detach-disk 7 vda
        Disk detached successfully    

        # virsh console 7        
        Connected to domain cirros
        Escape character is ^]

        # fdisk -l

        Disk /dev/sda: 41 MB, 41126400 bytes
        255 heads, 63 sectors/track, 5 cylinders, total 80325 sectors
        Units = sectors of 1 * 512 = 512 bytes
        Sector size (logical/physical): 512 bytes / 512 bytes
        I/O size (minimum/optimal): 512 bytes / 512 bytes
        Disk identifier: 0x00000000

           Device Boot      Start         End      Blocks   Id  System
        /dev/sda1   *       16065       80324       32130   83  Linux        
    ```
- `virsh attach-interface`：附加网络接口
    ```
        # virsh attach-interface 7 bridge virbr0
        Interface attached successfully

        # virsh console 7
        Connected to domain cirros
        Escape character is ^]

        # ifconfig -a
        eth0      Link encap:Ethernet  HWaddr 52:54:00:46:F0:3D  
                  inet6 addr: fe80::5054:ff:fe46:f03d/64 Scope:Link
                  UP BROADCAST RUNNING MULTICAST  MTU:1500  Metric:1
                  RX packets:5 errors:0 dropped:0 overruns:0 frame:0
                  TX packets:8 errors:0 dropped:0 overruns:0 carrier:0
                  collisions:0 txqueuelen:1000 
                  RX bytes:438 (438.0 B)  TX bytes:1394 (1.3 KiB)

        eth1      Link encap:Ethernet  HWaddr 52:54:00:0A:46:56  
                  BROADCAST MULTICAST  MTU:1500  Metric:1
                  RX packets:0 errors:0 dropped:0 overruns:0 frame:0
                  TX packets:0 errors:0 dropped:0 overruns:0 carrier:0
                  collisions:0 txqueuelen:1000 
                  RX bytes:0 (0.0 B)  TX bytes:0 (0.0 B)
                  Interrupt:10 Base address:0x2000 

        lo        Link encap:Local Loopback  
                  inet addr:127.0.0.1  Mask:255.0.0.0
                  inet6 addr: ::1/128 Scope:Host
                  UP LOOPBACK RUNNING  MTU:16436  Metric:1
                  RX packets:0 errors:0 dropped:0 overruns:0 frame:0
                  TX packets:0 errors:0 dropped:0 overruns:0 carrier:0
                  collisions:0 txqueuelen:0 
                  RX bytes:0 (0.0 B)  TX bytes:0 (0.0 B)

        # brctl show
        bridge name     bridge id               STP enabled     interfaces
        br0             8000.6ad7329f2c96       no              veth1.0
                                                                vnet0
        virbr0          8000.5254006919a1       yes             virbr0-nic
                                                                vnet1
    ```
- `virsh detach-interface`：分离指定的网络接口
    ```
        # virsh detach-interface 7 bridge --mac 52:54:00:0A:46:56
        Interface detached successfully

        # brctl show
        bridge name     bridge id               STP enabled     interfaces
        br0             8000.6ad7329f2c96       no              veth1.0
                                                                vnet0
        virbr0          8000.5254006919a1       yes             virbr0-nic

        # virsh console 7
        Connected to domain cirros
        Escape character is ^]

        # ifconfig -a
        eth0      Link encap:Ethernet  HWaddr 52:54:00:46:F0:3D  
                  inet6 addr: fe80::5054:ff:fe46:f03d/64 Scope:Link
                  UP BROADCAST RUNNING MULTICAST  MTU:1500  Metric:1
                  RX packets:5 errors:0 dropped:0 overruns:0 frame:0
                  TX packets:8 errors:0 dropped:0 overruns:0 carrier:0
                  collisions:0 txqueuelen:1000 
                  RX bytes:438 (438.0 B)  TX bytes:1394 (1.3 KiB)

        lo        Link encap:Local Loopback  
                  inet addr:127.0.0.1  Mask:255.0.0.0
                  inet6 addr: ::1/128 Scope:Host
                  UP LOOPBACK RUNNING  MTU:16436  Metric:1
                  RX packets:0 errors:0 dropped:0 overruns:0 frame:0
                  TX packets:0 errors:0 dropped:0 overruns:0 carrier:0
                  collisions:0 txqueuelen:0 
                  RX bytes:0 (0.0 B)  TX bytes:0 (0.0 B)

    ```

- kvm 管理工具：
<https://www.linux-kvm.org/page/Management_Tools>

（完）