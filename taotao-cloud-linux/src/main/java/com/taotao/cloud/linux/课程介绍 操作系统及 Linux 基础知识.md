## 课程介绍 操作系统及 Linux 基础知识

    第 01 天（半天）【上课环境介绍、Vmware使用、操作系统及Linux基础知识】

### 介绍课程：
- 中级：
    - 初级：系统基础
    - 中级：系统管理、服务安全及服务管理、Shell 脚本编程

    - 高级：
    - MySql 数据库：
        + cache & storage
    - 集群：
        + Cluster
        + LB：
            * 4 layer
            * 7 layer
        + HA：
    - 分布式：
        + zookeeper
        + 分布式文件系统
    - 虚拟化技术：
        + xen
        + kvm
    - Openstack：IAAS云：
    - 运维工具：
        + ansible
        + puppet(ruby), saltstack(python)
    - 监控工具：
        + zabbix
    - 大数据处理：
        + hadoop
        + spark, storm
        + elk：elasticsearch, logstash, kibana
    - docker
    - Python

一万小时定律。

- 认证：
    + RedHat：
        * RHCSA
        * RHCE
        * RHCA
- 培训：
    + 加速知识获取过程；
    + 有人监督，有环境；

- 纪律：

    1. 迟到  
        * 1分钟：2个俯卧撑   
        * 请假一晌：60个俯卧撑 
    2. 教室使用   
        * 全天开放：    
            * 拒绝玩游戏；
    3. 上课期间
        * 手机铃声关闭：一次：30个
- 作业和考试：
    + 作业：
    + 课前提问
    + 博客作业：
        * 5W1H：What,Why,Where,Who,How
    + 考试：
        * 机试、笔试
        * 满分100，及格80分，少一分：5个俯卧撑

### 上课环境
    172.16.0.0/16
        Windows：172.16.250.X
        Linux：172.16.249.X
            X：1-254
        网关：172.16.0.1
        DNS：172.16.0.1
        每一位同学：172.16.Y.1-254
                  172.16.100+Y.1-254
        Server：172.16.0.1，192.168.0.254，192.168.1.254
            允许核心转发；
            DHCP、FTP、HTTP、COBBLER

### VMware Workstation的使用

现在计算机设备的组成部分：  
运算器、控制器、存储器、输入设备、输出设备。 

- CPU
- bus：总线
- memory：编址存储设备
- read ahead (预取)

IO：与外部部件交互

- 磁盘
- 网卡

虚拟机：虚拟计算机

- CPU：运算器、控制器  
    + CPU指令，指令集  
        + 特权指令：OS 运行特权指令；  
        + 普通指令：
    + 程序员

### OS：Operating System  
 
- 是一个软件程序
- 通用目的
    + 硬件驱动
    + 进程管理
    + 内存管理
    + 网络管理
    + 安全管理
- System Call
    + Syscall：系统调用
- 编程层次：
    + 硬件规格：hardware specification。
    + 系统调用：由操作系统提供的子程序。
    + 库调用：library call。

- UI：User Interface  
能够让用户通过鼠标和键盘与操作系统进行交互的应用程序。
	 + GUI：Graphic User Interface
	 + CLI：Command Line Interface

- ABI：Application Binary Interface
- API：Application Interface
- CPU 架构类型：
 	 + x86
 	 + x64
 	 + arm
 	 + m6800,m68k：摩托罗拉生产。
 	 + power：世界上第一个多核 CPU。
 	 + powerpc：Apple + 摩托罗拉 + IBM 研制。
 	 + ultrasparc：sun 公司制造。
 	 + alpha：惠普制造。
 	 + 安腾：康百生产，后来卖给 Intel。
- Windows
- Linux:

	Linus --> Linux。
	
	GNU/Linux
	
- Unix:
	 + System (Bell Lab)
		 + AIX (IBM)
		 + Solaris (SUN)
		 + HP-UX (HP)
	 + BSD：(BSRG) Berkeley System Distribution 
	 	 + NetBSD
	 	 + OpenBSD
	 	 + FreeBSD

- MIT：Richard Stallman
	 + GNU：GNU is Not Unix
	 	 + GPL：General Public License