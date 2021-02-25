## KVM 技术基础

    第 51 天 【kvm技术基础（01）】

### 回顾
---

- 虚拟化技术有两种类型的实现：
    + Type-I：
        * hypervisor --> vm
    + Type-II：
        * host --> vmm --> vms

- Xen：
    + hypervisor, Dom0 Kernel


### KVM 
---

- KVM：Kernel-based Virtual Machine, Qumranet公司研发，依赖于HVM；Intel VT-x, AMD AMD-V；
- KVM 模块载入后的系统的运行模式：
    + 内核模式：GuestOS执行的I/O类操作，或其它的特殊指令的操作；称作“来宾-内核”模式；
    + 用户模式：代表GuestOS请求I/O类操作；
    + 来宾模式：GuestOS的非I/O类操作；事实上，它被称作“来宾-用户”模式；
    + kvm hypervisor：
- KVM的组件：
    + 两类组件：
        * /dev/kvm：
            - 工作于hypervisor，在用户空间，可通过ioctl()系统调用来完成VM创建、启动等管理功能；它是一个字符设备；
            - 功能：
                + 创建VM，为VM分配内存、读写VCP的寄存器、向VCPU注入中断、运行VCPU等待；
        * qemu进程：
            - 工作于用户空间，主要用于实现模拟PC机的IO设备；

- KVM特性：
    + 内存管理：
        * 支持将分配给VM的内存交换至SWAP；
        * 支持使用Huge Page；
        * 支持使用Intel EPT或AMD RVI技术完成内存地址映射：GVA --> GPA --> HPA
            ![memory_virtualization.png](images/memory_virtualization.png)
        * 支持KSM（Kernel Same-page Merging）;
    + 硬件支持：
        * 取决于Linux内核；
    + 存储：
        * 本地存储；
        * 网络附加存储；
        * 存储区域网络；
        * 分布式存储：例如GlustFS
    + 实时迁移；
    + 支持的GuestOS：
        * Linux, Windows, OpenBSD, FreeBSD, OpenSolaris;
    + 设备驱动：
        * IO设备的完全虚拟化：模拟硬件
        * IO设备的半虚拟化：在GuestOS中安装驱动；virtio
            - virtio-blk, virtio-net, virtio-pci, virt-console, virtio-ballon

- KVM 局限性：
    + 一般局限性：
        * CPU overcommit 有限；
        * 时间记录难以精确，依赖于时间同步机制；
    + MAC地址：
        * VM量特别大时，存在冲突的可能性；
        * 实时迁移；
        * 性能局限性；

- KVM的工具栈：
    + qemu：
        * qemu-kvm
        * qemu-img
    + libvirt：
        * GUI：virt-manager, virt-viewer
        * CLI：virt-install, virsh

    ![kvm_management-tools.png](images/kvm_management-tools.png)

    * QEMU主要提供以下几个部分：
        - 处理器模拟器
        - 仿真IO设备
        - 关联模拟的设备至真实设备
        - 调试器
        - 与模拟器交互的用户接口

- 安装：
    + 确保 CPU 支持 HVM：
    ```
        # grep -E --color=auto '(vmx|svm)' /proc/cpuinfo
    ```
    + 装载模块：
    ```
        # modprobe kvm
        # modprobe kvm-intel
    ```
    + 验证：
    ```
        # lsmod
        Module                  Size  Used by
        kvm_intel              55624  0 
        kvm                   341551  1 kvm_intel
        .
        .
        .

        # ls /dev/kvm
        /dev/kvm
    ```

- 管理工具栈：
```
    # yum grouplist | grep -i "virtualization"
    Virtualization：
        qemu-kvm
    Virtualization Client：
        python-virtinst, virt-manager, virt-viewer
    Virtualization Platform：
        libvirt, libvirt-client
    Virtualization Tools
        libguestfs
```

- 补充资料：KVM内存管理

KVM继承了Linux系统管理内存的诸多特性，比如，分配给虚拟使用的内存可以被交换至交换空间、能够使用大内存页以实现更好的性能，以及对NUMA的支持能够让虚拟机高效访问更大的内存空间等。

KVM基于Intel的EPT（Extended Page Table）或AMD的RVI（Rapid Virtualization Indexing）技术可以支持更新的内存虚拟功能，这可以降低CPU的占用率，并提供较好的吞吐量。

此外，KVM还借助于KSM（Kernel Same-page Merging）这个内核特性实现了内存页面共享。KSM通过扫描每个虚拟机的内存查找各虚拟机间相同的内存页，并将这些内存页合并为一个被各相关虚拟机共享的单独页面。在某虚拟机试图修改此页面中的数据时，KSM会重新为其提供一个新的页面副本。实践中，运行于同一台物理主机上的具有相同GuestOS的虚拟机之间出现相同内存页面的概率是很的，比如共享库、内核或其它内存对象等都有可能表现为相同的内存页，因此，KSM技术可以降低内存占用进而提高整体性能。

- 补充资料：
    + VMM:对IO的驱动有三种模式：
        * 自主VMM：VMM自行提供驱动和控制台；
        * 混合VMM：借助于OS提供驱动；
            - 依赖于外部OS实现特权域
            - 自我提供特权域
        * 寄宿式VMM：

- IO虚拟化模型：
    + 模拟
    + 半虚拟化
    + 透传





